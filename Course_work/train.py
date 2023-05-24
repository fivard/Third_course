from ray.rllib.agents.ppo import PPOTrainer
from mountain_car_spline import MountainCarEnvSpline

config = {
    "framework": "torch",
    "env_config": {"count_vertexes": 20}
}
trainer = PPOTrainer(env=MountainCarEnvSpline, config=config)

for i in range(100):
    info = trainer.train()
    path = trainer.save(f'checkpoints_spline_new_reward/')
    print(i, info['episode_reward_mean'], path, info)

env = MountainCarEnvSpline(config={"count_vertexes": 20})
episode_reward = 0
done = False
obs = env.reset()
while not done:
    action = trainer.compute_single_action(obs)
    obs, reward, done, info = env.step(action)
    env.render()
    episode_reward += reward
print(f"{episode_reward=}")
