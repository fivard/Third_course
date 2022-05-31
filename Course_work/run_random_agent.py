from mountain_car import MountainCarEnv, HILLS_FACTOR

env = MountainCarEnv(config={'hills_factor': HILLS_FACTOR})
state = env.reset()
done = False
episode_reward = 0

while not done:
    observation, reward, done, info = env.step(env.action_space.sample())
    env.render()
    episode_reward += reward
print(episode_reward)

env.close()
