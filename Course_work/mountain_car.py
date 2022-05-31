
import math

import numpy as np

import gym
from gym import spaces

HILLS_FACTOR = 10
START_POSITION = -0.75


class MountainCarEnv(gym.Env):

    metadata = {"render.modes": ["human", "rgb_array"], "video.frames_per_second": 30}

    def __init__(self, config=None):
        config = config or {}
        self.hills_factor = config.get("hills_factor", 3)
        self.min_position = -1.5
        self.max_position = 0.6
        self.max_speed = 0.07
        self.goal_position = 0.5
        self.start_position = self.max_reached_position = START_POSITION

        self.force = 0.001
        self.gravity = 0.0025

        self.low = np.array([self.min_position, -self.max_speed], dtype=np.float32)
        self.high = np.array([self.max_position, self.max_speed], dtype=np.float32)

        self.viewer = None

        self.action_space = spaces.Discrete(3)
        self.observation_space = spaces.Box(self.low, self.high, dtype=np.float32)

    def step(self, action):
        assert self.action_space.contains(action), "%r (%s) invalid" % (
            action,
            type(action),
        )

        position, velocity = self.state
        new_velocity = self._calculate_new_velocity(position, velocity, action)
        new_position = self._calculate_new_position(position, new_velocity)

        if new_position == self.min_position and new_velocity < 0:
            new_velocity = 0

        done = new_position >= self.goal_position
        reward = self._calculate_reward(new_position)

        self.state = (new_position, new_velocity)
        return np.array(self.state, dtype=np.float32), reward, done, {}

    def _calculate_reward(self, new_position):
        if new_position == self.min_position:
            return -10
        if new_position > self.max_reached_position:
            self.max_reached_position = new_position
            return 1
        return -1

    def _calculate_new_position(self, old_position, new_velocity):
        return np.clip(old_position + new_velocity, self.min_position, self.max_position)

    def _calculate_new_velocity(self, old_position, old_velocity, action):
        new_velocity = old_velocity + (action - 1) * self.force + math.cos(self.hills_factor * old_position) * (-self.gravity)
        return np.clip(new_velocity, -self.max_speed, self.max_speed)

    def reset(self):
        self.state = np.array([self.start_position, 0])
        return np.array(self.state, dtype=np.float32)

    def _height(self, xs):
        return np.sin(self.hills_factor * xs) * 0.45 + 0.55

    def render(self, mode="human"):
        screen_width = 1200
        screen_height = 800

        world_width = self.max_position - self.min_position
        scale = screen_width / world_width
        carwidth = 40
        carheight = 20

        if self.viewer is None:
            from gym.envs.classic_control import rendering

            self.viewer = rendering.Viewer(screen_width, screen_height)
            xs = np.linspace(self.min_position, self.max_position, 100)
            ys = self._height(xs)
            xys = list(zip((xs - self.min_position) * scale, ys * scale))

            self.track = rendering.make_polyline(xys)
            self.track.set_linewidth(4)
            self.viewer.add_geom(self.track)

            clearance = 10

            l, r, t, b = -carwidth / 2, carwidth / 2, carheight, 0
            car = rendering.FilledPolygon([(l, b), (l, t), (r, t), (r, b)])
            car.add_attr(rendering.Transform(translation=(0, clearance)))
            self.cartrans = rendering.Transform()
            car.add_attr(self.cartrans)
            self.viewer.add_geom(car)

            frontwheel = rendering.make_circle(carheight / 2.5)
            frontwheel.set_color(0.5, 0.5, 0.5)
            frontwheel.add_attr(
                rendering.Transform(translation=(carwidth / 4, clearance))
            )
            frontwheel.add_attr(self.cartrans)
            self.viewer.add_geom(frontwheel)

            backwheel = rendering.make_circle(carheight / 2.5)
            backwheel.add_attr(
                rendering.Transform(translation=(-carwidth / 4, clearance))
            )
            backwheel.set_color(0.5, 0.5, 0.5)
            backwheel.add_attr(self.cartrans)
            self.viewer.add_geom(backwheel)

            flagx = (self.goal_position - self.min_position) * scale
            flagy1 = self._height(self.goal_position) * scale
            flagy2 = flagy1 + 50
            flagpole = rendering.Line((flagx, flagy1), (flagx, flagy2))
            self.viewer.add_geom(flagpole)
            flag = rendering.FilledPolygon(
                [(flagx, flagy2), (flagx, flagy2 - 10), (flagx + 25, flagy2 - 5)]
            )
            flag.set_color(0.8, 0.8, 0)
            self.viewer.add_geom(flag)

        pos = self.state[0]
        self.cartrans.set_translation(
            (pos - self.min_position) * scale, self._height(pos) * scale
        )
        self.cartrans.set_rotation(math.cos(self.hills_factor * pos))

        return self.viewer.render(return_rgb_array=mode == "rgb_array")

    def close(self):
        if self.viewer:
            self.viewer.close()
            self.viewer = None
