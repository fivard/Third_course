from ray.rllib.agents.ppo import PPOTrainer
from mountain_car import MountainCarEnv

HILLS_FACTOR = 10

config = {
    "framework": "torch",
    "env_config": {"hills_factor": HILLS_FACTOR}
}
trainer = PPOTrainer(env=MountainCarEnv, config=config)


def run_human_evaluation():
    env = MountainCarEnv(config={"hills_factor": HILLS_FACTOR})
    episode_reward = 0
    done = False
    obs = env.reset()
    while not done:
        action = trainer.compute_single_action(obs)
        obs, reward, done, info = env.step(action)
        env.render()
        episode_reward += reward


for i in range(100):
    print(i, trainer.train()['episode_reward_mean'])
    path = trainer.save(f'checkpoints/')
    print(path)

input('Ready?')
run_human_evaluation()
