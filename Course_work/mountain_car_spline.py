import random

import math
import numpy as np

import gym
from gym import spaces
from track_generator import MAX_HEIGHT, generate_track_np
from scipy.optimize import fsolve


class MountainCarEnvSpline(gym.Env):
    metadata = {"render_modes": ["human", "rgb_array"], "render_fps": 2}

    def __init__(self, config=None):
        config = config or {}
        self.steps_count = 0

        self.min_position = 0
        self.max_position = 10
        self.count_vertexes = config.get("count_vertexes", 10)

        self.max_velocity = 0.5
        self.min_velocity = -0.5
        self.velocity = 0

        self.goal_position = self.max_position - 0.5
        self.position = self.max_reached_position = self.min_position + random.uniform(0.5, 1.5)

        self.poly, self.track_points = generate_track_np(self.min_position, self.max_position, self.count_vertexes)
        self.derivative = self.poly.deriv()

        self.force = 0.001
        self.gravity = 0.0025

        self.rays_count = 14
        self.rays = []
        self.intersections = [None] * self.rays_count
        self.vision_range = [180, 360]
        self.ray_max_distance = 5

        self.viewer = None

        self.action_space = spaces.Discrete(3)
        self.observation_space = spaces.Dict(
            {
                "vision": spaces.Box(0, 1, shape=(self.rays_count,), dtype=float),
                "velocity": spaces.Box(self.min_velocity, self.max_velocity, shape=(1,), dtype=float),
            }
        )
        self.obs = {}

    def step(self, action):
        assert self.action_space.contains(action), "%r (%s) invalid" % (
            action,
            type(action),
        )
        self.steps_count += 1

        self.velocity = self._calculate_new_velocity(self.position, self.velocity, action)
        self.position = self._calculate_new_position(self.position, self.velocity)
        self._update_rays()

        if self.steps_count > 1000:
            done = True
        elif self.position >= self.goal_position:
            done = True
        else:
            done = False

        reward = self._calculate_reward(self.position)

        self.obs = self._get_obs()
        return self.obs, reward, done, {}

    @staticmethod
    def _find_intersection(fun1, fun2, x0):
        return fsolve(lambda x: fun1(x) - fun2(x), x0)

    def _get_ray_collision(self):
        vision = []
        for index, ray in enumerate(self.rays):
            func = np.polyfit([ray[0], ray[2]], [ray[1], ray[3]], 1)
            ray_poly = np.poly1d(func)
            result1 = self._find_intersection(self.poly, ray_poly, ray[0])
            result2 = self._find_intersection(self.poly, ray_poly, ray[2])

            if min(ray[0], ray[2]) <= result1[0] <= max(ray[0], ray[2]):
                self.intersections[index] = [result1[0], ray_poly(result1[0])]
                vision.append(math.sqrt((result1[0] - ray[0])**2 + (ray_poly(result1[0]) - ray_poly(ray[0]))**2) / self.ray_max_distance)
                continue

            if min(ray[0], ray[2]) <= result2[0] <= max(ray[0], ray[2]):
                self.intersections[index] = [result2[0], ray_poly(result2[0])]
                vision.append(math.sqrt((result2[0] - ray[0])**2 + (ray_poly(result2[0]) - ray_poly(ray[0]))**2) / self.ray_max_distance)
                continue

            self.intersections[index] = [None, None]
            vision.append(self.ray_max_distance / self.ray_max_distance)
        return vision

    def _get_obs(self):
        return {'vision': self._get_ray_collision(), 'velocity': [self.velocity / self.max_velocity]}

    def _calculate_reward(self, new_position):
        if new_position == self.min_position:
            return -10
        if new_position > self.max_reached_position:
            self.max_reached_position = new_position
            return 10
        return -1

    def _update_rays(self):
        self.rays.clear()
        for angle in np.linspace(*self.vision_range, self.rays_count, endpoint=True):
            ray = self._from_point_angle(angle, self.ray_max_distance)
            self.rays.append(ray)

    def _from_point_angle(self, angle, distance):
        angle = np.deg2rad(angle)
        point1 = np.array([self.position, self.poly(self.position) + 0.16])
        point2 = np.array(point1) + np.array([np.cos(angle), np.sin(angle)]) * distance
        return [*point1, *point2]

    def _calculate_new_position(self, old_position, new_velocity):
        return np.clip(old_position + new_velocity, self.min_position, self.max_position)

    def _calculate_new_velocity(self, old_position, old_velocity, action):
        new_velocity = old_velocity + (action - 1) * self.force + self.derivative(old_position) * (-self.gravity)
        return np.clip(new_velocity, self.min_velocity, self.max_velocity)

    def reset(self):
        self.velocity = 0
        self.position = self.max_reached_position = self.min_position + random.uniform(0.5, 1.5)
        self.steps_count = 0
        self.poly, self.track_points = generate_track_np(self.min_position, self.max_position, self.count_vertexes)
        self.derivative = self.poly.deriv()
        self._update_rays()

        self.obs = self._get_obs()
        return self.obs

    def _height(self, xs):
        return self.poly(xs)

    def render(self, mode="human"):
        screen_width = 1200
        screen_height = 800

        world_width = self.max_position - self.min_position
        x_scale = screen_width / world_width
        y_scale = screen_height / (MAX_HEIGHT + 2)
        carRadius = 20
        vision = self.obs['vision']
        print(vision)

        if self.viewer is None:
            from gym.envs.classic_control import rendering

            self.viewer = rendering.Viewer(screen_width, screen_height)
            xs = np.array([ver[0] for ver in self.track_points])
            ys = np.array([ver[1] for ver in self.track_points])
            xys = list(zip(xs * x_scale, ys * y_scale))
            self.track = rendering.make_polyline(xys)
            self.track.set_linewidth(4)
            self.viewer.add_geom(self.track)

            car = rendering.make_circle(carRadius)
            car.add_attr(rendering.Transform(translation=(0, carRadius)))
            self.car_transform = rendering.Transform()
            car.add_attr(self.car_transform)
            self.viewer.add_geom(car)

            flagx = self.goal_position * x_scale
            flagy1 = self._height(self.goal_position) * y_scale
            flagy2 = flagy1 + 50
            flagpole = rendering.Line((flagx, flagy1), (flagx, flagy2))
            self.viewer.add_geom(flagpole)
            flag = rendering.FilledPolygon(
                [(flagx, flagy2), (flagx, flagy2 - 10), (flagx + 25, flagy2 - 5)]
            )
            flag.set_color(0.8, 0.8, 0)
            self.viewer.add_geom(flag)

            for index, angle in enumerate(np.linspace(*self.vision_range, self.rays_count, endpoint=True)):
                angle = np.deg2rad(angle)
                point1 = np.array([0, carRadius])
                point2 = np.array(point1) + np.array([np.cos(angle), np.sin(angle)]) * self.ray_max_distance * x_scale
                ray = rendering.Line((point1[0], point1[1]), (point2[0], point2[1]))
                ray.set_color(0, 0.8, 0.5)
                ray.add_attr(self.car_transform)
                self.viewer.add_geom(ray)

            self.inersections_transform = []
            for point in self.intersections:
                dot = rendering.make_circle(3)
                dot.set_color(0, 0.8, 0.5)
                dot_transform = rendering.Transform()
                dot.add_attr(dot_transform)
                self.inersections_transform.append(dot_transform)
                self.viewer.add_geom(dot)

        self.car_transform.set_translation(
            self.position * x_scale, self._height(self.position) * y_scale
        )
        for index, dot in enumerate(self.inersections_transform):
            if self.intersections[index][0] is not None:
                dot.set_translation(
                    self.intersections[index][0] * x_scale, self.intersections[index][1] * y_scale
                )
            else:
                dot.set_translation(
                    0, 0
                )

        return self.viewer.render(return_rgb_array=mode == "rgb_array")

    def close(self):
        if self.viewer:
            self.viewer.close()
            self.viewer = None
