from mountain_car import MountainCarEnv
env = MountainCarEnv(config={'hills_factor': 3})
observation, info = env.reset()

for _ in range(10000):
    observation, reward, done, info = env.step(env.action_space.sample())

    env.render(mode="human")
    if done:
        print("DONE")
        observation, info = env.reset()

env.close()