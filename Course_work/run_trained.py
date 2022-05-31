from ray.rllib.agents.ppo import PPOTrainer
from mountain_car import MountainCarEnv
from train import HILLS_FACTOR

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


trainer.restore('checkpoints/checkpoint_000100/checkpoint-70')
run_human_evaluation()