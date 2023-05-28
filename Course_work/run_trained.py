import time

import numpy as np
from ray.rllib.agents.ppo import PPOTrainer
from mountain_car_spline import MountainCarEnvSpline

config = {
    "framework": "torch",
    "env_config": {"count_vertexes": 20}
}

trainer = PPOTrainer(env=MountainCarEnvSpline, config=config)
trainer.restore('checkpoints_spline_new_reward/checkpoint_000099/checkpoint-99')
env = MountainCarEnvSpline(config={"count_vertexes": 20})
episode_reward = 0
done = False
obs = env.reset()
while not done:
    action = trainer.compute_single_action(obs)
    obs, reward, done, info = env.step(action)
    env.render()
    time.sleep(0.1)
    episode_reward += reward
print(f"{episode_reward=}")
