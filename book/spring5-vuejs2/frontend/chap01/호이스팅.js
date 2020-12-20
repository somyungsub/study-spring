travel = 'No Plan';
var travel;
console.log(travel);  // ??  No plan

function travel() {
  console.log("Traveling");
}

// travel(); // type error

function workout() {
  goToGym();    // ? B

  var goToGtm = function () {
    console.log("Working A");
  };

  return;

  function goToGym() {
    console.log("Working B");
  }
}

workout();