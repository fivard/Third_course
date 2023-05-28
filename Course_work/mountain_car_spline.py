import random

import math
import numpy as np

import gym
from gym import spaces
from track_generator import generate_track_np
from scipy.optimize import fsolve


class MountainCarEnvSpline(gym.Env):
    metadata = {"render_modes": ["human", "rgb_array"], "render_fps": 2}

    def __init__(self, config=None):
        config = config or {}
        self.steps_count = 0
        self.screen_width = 1200
        self.screen_height = 800
        self.circle_radius = 20

        self.min_position = 0
        self.max_position = self.screen_width
        self.count_vertexes = config.get("count_vertexes", 10)

        self.velocity = 0

        self.goal_position = self.max_position - 50
        self.position = self.min_position + random.randint(50, 150)

        self.poly, self.track_points = generate_track_np(self.min_position, self.max_position, self.count_vertexes,
                                                         self.screen_height)
        self.derivative = self.poly.deriv()     # for finding tangent

        self.force = 1                          # for action
        self.gravity = 1

        self.rays_count = 10
        self.rays = []
        self.intersections = [None] * self.rays_count
        self.vision_range = [180, 360]
        self.ray_max_distance = 300

        self.viewer = None

        self.action_space = spaces.Discrete(3)
        self.observation_space = spaces.Dict(
            {
                "vision": spaces.Box(0, 1, shape=(self.rays_count,), dtype=float),
                "velocity": spaces.Box(-10000, 10000, shape=(1,), dtype=float),
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

        reward = self._calculate_reward()
        self.obs = self._get_obs()

        return self.obs, reward, done, {}

    @staticmethod
    def _dist_2_points(point1, point2):
        return math.sqrt((float(point1[0]) - float(point2[0])) ** 2 + (float(point1[1]) - float(point2[1])) ** 2)

    def _get_ray_collision(self):
        vision = []
        for index, ray in enumerate(self.rays):
            func = np.polyfit([ray[0], ray[2]], [ray[1], ray[3]], 1)
            ray_poly = np.poly1d(func)
            roots = np.roots(self.poly - ray_poly)
            if roots.any():
                closest = roots[0]
                ray_start = [ray[0], ray_poly(ray[0])]
                for root in roots:
                    new_point = [root, ray_poly(root)]
                    if root.imag == 0.0 and \
                            min(ray[0], ray[2]) <= root <= max(ray[0], ray[2]) and \
                            self._dist_2_points(new_point, ray_start) < self._dist_2_points(ray_start, [closest, ray_poly(closest)]):
                        closest = root.real

                if min(ray[0], ray[2]) <= closest <= max(ray[0], ray[2]):
                    self.intersections[index] = [closest, ray_poly(closest)]
                    point1 = [closest, ray_poly(closest)]
                    point2 = [ray[0], ray_poly(ray[0])]
                    vision.append(self._dist_2_points(point1, point2) / self.ray_max_distance)
                    continue

            self.intersections[index] = [None, None]
            vision.append(1.0)
        return vision

    def _get_obs(self):
        return {'vision': self._get_ray_collision(), 'velocity': [self.velocity]}

    def _calculate_reward(self):
        if self.position > self.goal_position:
            return 100
        return -1

    def _update_rays(self):
        self.rays.clear()
        for angle in np.linspace(*self.vision_range, self.rays_count, endpoint=True):
            ray = self._from_point_angle(angle, self.ray_max_distance)
            self.rays.append(ray)

    def _from_point_angle(self, angle, distance):
        angle = np.deg2rad(angle)
        # setting ray start from top of the circle
        point1 = np.array([self.position, self.poly(self.position) + 2 * self.circle_radius])
        point2 = np.array(point1) + np.array([np.cos(angle), np.sin(angle)]) * distance
        return [*point1, *point2]

    def _calculate_new_position(self, old_position, new_velocity):
        return np.clip(old_position + new_velocity, self.min_position, self.max_position)

    def _calculate_new_velocity(self, old_position, old_velocity, action):
        return old_velocity + (action - 1) * self.force + self.derivative(old_position) * (-self.gravity)

    def reset(self):
        self.velocity = 0
        self.position = self.max_reached_position = self.min_position + random.randint(50, 150)
        self.steps_count = 0
        self.poly, self.track_points = generate_track_np(self.min_position, self.max_position, self.count_vertexes,
                                                         self.screen_height)
        self.derivative = self.poly.deriv()
        self._update_rays()

        self.obs = self._get_obs()
        return self.obs

    def _height(self, xs):
        return self.poly(xs)

    def render(self, mode="human"):

        from gym.envs.classic_control import rendering

        if self.viewer is None:

            self.viewer = rendering.Viewer(self.screen_width, self.screen_height)

            xs = np.array([ver[0] for ver in self.track_points])
            ys = np.array([ver[1] for ver in self.track_points])
            xys = list(zip(xs, ys))
            self.track = rendering.make_polyline(xys)
            self.track.set_linewidth(4)
            self.viewer.add_geom(self.track)

            car = rendering.make_circle(self.circle_radius)
            car.add_attr(rendering.Transform(translation=(0, self.circle_radius)))
            self.car_transform = rendering.Transform()
            car.add_attr(self.car_transform)
            self.viewer.add_geom(car)

            flagx = self.goal_position
            flagy1 = self._height(self.goal_position)
            flagy2 = flagy1 + 50
            flagpole = rendering.Line((flagx, flagy1), (flagx, flagy2))
            self.viewer.add_geom(flagpole)
            flag = rendering.FilledPolygon(
                [(flagx, flagy2), (flagx, flagy2 - 10), (flagx + 25, flagy2 - 5)]
            )
            flag.set_color(0.8, 0.8, 0)
            self.viewer.add_geom(flag)

            height = self._height(self.position)

            for ray in self.rays:
                ray = rendering.Line((ray[0] - self.position, ray[1] - height),
                                     (ray[2] - self.position, ray[3] - height))
                ray.set_color(0, 0.8, 0.8)
                ray.add_attr(self.car_transform)
                self.viewer.add_geom(ray)

            self.inersections_transform = []
            for i in range(self.rays_count):
                dot = rendering.make_circle(5)
                dot.set_color(0.15 * i, 0.8, 0.5)
                dot_transform = rendering.Transform()
                dot.add_attr(dot_transform)
                self.inersections_transform.append(dot_transform)
                self.viewer.add_geom(dot)

        self.car_transform.set_translation(
            self.position, self._height(self.position)
        )
        for index, dot in enumerate(self.inersections_transform):
            if self.intersections[index][0] is not None:
                dot.set_translation(
                    self.intersections[index][0], self.intersections[index][1]
                )
            else:
                dot.set_translation(
                    -1000, -1000
                )

        return self.viewer.render(return_rgb_array=mode == "rgb_array")

    def close(self):
        if self.viewer:
            self.viewer.close()
            self.viewer = None
