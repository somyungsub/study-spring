console.log('Inside tasks module');
export default function completeTask(user) {
  console.log(`${user.name} completed a task`);
  completedCount++;
};

export let completedCount = 0;
