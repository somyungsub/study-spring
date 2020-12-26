function User(name, interests) {
  this.name = name;
  this.interests = interests;
}

User.prototype.greeting = function () {
  console.log("hi~ " + this.name);
};

function TeamMember(name, interests, tasks) {
  console.log(this);  // User {}
  User.call(this, name, interests);
  this.tasks = tasks;
}

TeamMember.prototype = Object.create(User.prototype);  // User.prototype을 활용하여 생성
TeamMember.prototype.greeting = function () { // 재정의
  console.log("Team Hi~ " + this.name + " Welcome to the team!!");
};
TeamMember.prototype.work = function () {
  console.log("I'm working on " + this.tasks.length + ' task');
};

console.log(User.__proto__ === User.prototype);// t
console.log(User.prototype === TeamMember.prototype); // f -> 본질적으로 다름을 의미
console.log(User.prototype.constructor === TeamMember.prototype.constructor); // t

// 테스트
let member = new TeamMember("so", ['Traveling, Reading'], ['Buy ticket', 'Book a hotel']);
member.greeting();
member.work();

console.log(member instanceof TeamMember);  // t
console.log(member instanceof User);        // t
console.log(member instanceof Object);      // t
console.log(typeof member);

User.prototype.eat = function () {
  console.log("eating !");
};

member.eat();

// 최상위 객체에 메서드 추가 -> 지양
Object.prototype.move = function () {
  console.log("Every object can move now");
};

member.move();

let alien = {};
alien.move();
User.move();
console.log(typeof User);

console.log("============ 클로저 ==========");

function User2(name) {
  console.log("I'm in " + this.constructor.name + "context.");
  this.name = name;
  this.speak = function () {
    console.log(this.name + " is speaking from " + this.constructor.name + " context.");
    var drink = function () {
      console.log("Drinking in " + this.constructor.name);
    };

    drink();
  };

  function ask() {
    console.log("Asking from " + this.constructor.name + " context.");
    console.log("Who am I? " + this.name);
  }

  ask();
}

var name = 'Unknown';
var user = new User2("So");
user.speak();