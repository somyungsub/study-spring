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


const advice = "Stay hungry. Stay foolish.";

let advisor = {
  __proto__: new TeamMember("So", ['Consulting']),  // 프로토타입 설정
  advice,       // 축약
  greeting() {  // functoin
    super.greeting();
    console.log(this.advice);
  },
  [advice.split('.')[0]]: 'Always learn more' // 프로퍼티 대괄호
};

console.log(advisor.advice);
console.log(advisor['Stay hungry']);
advisor.greeting();

console.log(TeamMember.prototype.isPrototypeOf(advisor)); // t
console.log(advisor instanceof TeamMember);               // t

const fruits = [
  {
    name: 'apple',
    price: 100
  },
  {
    name: 'orange',
    price: 80
  },
  {
    name: 'banana',
    price: 120
  }
]

// 변형1
console.log("========== 변형1 화살표함수 ==========");
var countFruits = () => fruits.length;
var countFruits2 = function () {
  return fruits.length;
};

console.log(countFruits());
console.log(countFruits2());

// 변형2
console.log("========== 변형2 filter ==========");
let es6overPrice = fruits.filter(fruit => fruit.price > 100);
let es5overPrice = fruits.filter(function (fruit) {
  return fruit.price > 100;
});
console.log(es6overPrice);
console.log(es5overPrice);

// 변형3
console.log("========== 변형3 map ==========");
let es6map = fruits.map(fruit => ({name: fruit.name, storage: 1}));
let es5map = fruits.map(function (fruit) {
  return {
    name: fruit.name,
    storage: 1
  }
});
console.log(es6map);
console.log(es5map);

// 변형4
console.log("========== 변형4 map2 ==========");
let es6map2 = fruits.map(fruit => {
  console.log('Checking ' + fruit.name + ' storage');
  return {name: fruit.name, storage: 1};
});
console.log(es6map2);

console.log("===== sum =====");
let sum = (a, b) => {a+b};
let sum2 = (a, b) => {return a+b};
let sum3 = (a, b) => a+b;
console.log(sum(1, 2));
console.log(sum2(1, 2));
console.log(sum3(1, 2));

var shoppingCart1 = {
  items: ['Apple', 'Orange'],
  inventory: {Apple: 1, Orange: 0},
  checkout() {
    this.items.forEach(item => {
      if (!this.inventory[item]) {
        console.log('Item ' + item + ' has sold out.');
      }
    })
  }
}
shoppingCart1.checkout();

var shoppingCart2 = {
  items: ['Apple', 'Orange'],
  inventory: {Apple: 1, Orange: 0},
  checkout() {
    var that = this;
    this.items.forEach(function (item) {
      if (!that.inventory[item]) {
        console.log('Item ' + item + ' has sold out.');
      }
    })
  }
}
shoppingCart2.checkout();

var name2 = 'Unknow';
var greeting = () => {
  console.log('Hi, Im ' + this.name2);
}

greeting.call({name2: 'Sunny'});
greeting.apply({name2: 'Tod'});
var newGreeting = greeting.bind({name2: 'James'});
newGreeting();

// var shoppingCart3 = {
//   items: ['Apple', 'Orange'],
//   inventory: {Apple: 1, Orange: 0},
//   checkout: () => {
//     this.items.forEach(item => {
//       if (!this.inventory[item]) {
//         console.log('Item ' + item + ' has sold out.');
//       }
//     })
//   }
// };
// shoppingCart3.checkout();

class User2 {
  constructor(name) {
    this.name = name;
  }
}
User2.prototype.swim = () => {
  console.log(this.name + ' is swimming');  //undefined
}

var user2 = new User2();
console.log(user2.swim());

const shoppingCartConst = [];
function addToCart(item, size = 1) {
  shoppingCartConst.push({item: item, count: size});
}

addToCart('Applie');
console.log(shoppingCartConst);
addToCart('Orange', 2);
console.log(shoppingCartConst);

// 매개변수
function workout(exercise1) {
  var todos = Array.prototype.slice.call(arguments, workout.length);
  console.log('Start from ' + exercise1);
  console.log(todos.length + ' more to do');
}

function workoutES6(exercise1, ...todos) {
  console.log('Start from ' + exercise1);
  console.log(todos.length + ' more to do');
  console.log('Args length : ' + workoutES6.length);
}

workout('Treadmill', 'Pushup', 'Spinning');
workoutES6('Treadmill', 'Pushup', 'Spinning');

let urgentTasks = ['Buy three tickets'];
let normalTasks = ['Book a hotel', 'Rent a car'];
let allTasks = [...urgentTasks, ...normalTasks];

console.log(allTasks);

((first, second, third) => {
  console.log('Working on ' + first + ' and ' + second);
  console.log('Working on 3 ' + third);
})(...allTasks);

// object destructuring
console.log('======= object destructuring ========');
let user3 = {name: 'So', interests: ['Traveling', 'Swimming']};
let {name, interests, tasks} = user3;
console.log(name);
console.log(interests);
console.log(tasks);

let {name3, interests3, tasks3=[]} = user3;
console.log(tasks3);  // []

// name 프로퍼티 -> firstName 변수에 할당
let {name: firstName} = user3;
console.log(firstName);

let {name: customName, interests: customInter} = user3;
console.log(customName);
console.log(customInter);

// array destructuring
console.log('======= array destructuring ======');
let [first, second] = ['Traveling', 'Swimming', 'Shopping'];
console.log(first);
console.log(second);
console.log("======");
let [, , third = '', fourth = ''] = ['Traveling', 'Swimming', 'Shopping'];
console.log(third);
console.log(fourth);

let [first2, ...others] = ['T1', 'T2', 'T3'];
console.log(others);

function func({gym}, times) {
  console.log('Working in ' + gym + ' for ' + times + ' times');
}

let thisWeek = {gym: 'Gym A'};
func(thisWeek, 2);

function fun2({gym, todos = ['Treadmill']}) {
  let [first] = todos;
  console.log('Start ' + first + ' in ' + gym);
}

fun2({gym: 'Gym A'});
fun2({gym: 'Gym A', todos: ['First Todo', 'Second Todo']});

function func3({gym, todos = ['Treadmill']} = {}) {
  let [first] = todos;
  console.log('Start ' + first + ' in ' + gym);
}

func3();
let userObj = {
  name: 'So',
  greeting() {
    console.log(`Hello, I'm ${this.name}.`);
  }
};
let greetingString = `Hello, I'm ${userObj.name}`;

userObj.greeting();
console.log(greetingString);

