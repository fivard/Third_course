from ray.rllib.agents.ppo import PPOTrainer
from mountain_car import MountainCarEnv
config = {
    "framework": "torch",
}
trainer = PPOTrainer(env=MountainCarEnv, config=config)


def run_human_evaluation():
    env = MountainCarEnv()
    episode_reward = 0
    done = False
    obs = env.reset()
    while not done:
        action = trainer.compute_single_action(obs)
        obs, reward, done, info = env.step(action)
        env.render()
        episode_reward += reward


trainer.restore('checkpoints/checkpoint_000100/checkpoint-100')
run_human_evaluation()