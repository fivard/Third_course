import time

from mountain_car_spline import MountainCarEnvSpline

env = MountainCarEnvSpline(config={'count_vertexes': 20})
state = env.reset()
done = False
episode_reward = 0

while not done:
    observation, reward, done, info = env.step(env.action_space.sample())
    env.render()
    episode_reward += reward
    time.sleep(1)
print(f"{episode_reward=}")

env.close()
