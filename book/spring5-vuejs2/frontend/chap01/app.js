import User from './user';
import * as Roles from './role';
import completeTask, {completedCount} from './task';

let user = new User('So', Roles.USER);
completeTask(user);

console.log(`Total completed ${completedCount}`);

User.prototype.walk = function () {
  console.log(`${this.name} walks.!!`);
};

user.walk();