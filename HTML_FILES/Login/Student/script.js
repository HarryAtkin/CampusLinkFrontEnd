const inputBox = document.getElementById("input-box")
const listContainer = document.getElementById("list-container")
function addTask(){
    if(inputBox.value === ''){
        alert("Add a task below!");
    }
    else{
        let li = document.createElement("li");
        li.innerHTML = inputBox.value;
        listContainer.appendChild(li);
        let span = document.createElement("span");
        span.innerHTML = "\u00d7";
        li.appendChild(span);
    }
    inputBox.value = "";
    saveData();
}

listContainer.addEventListener("click", function(e){
    if(e.target.tagName === "LI"){
        e.target.classList.toggle("checked");
        saveData();
    }
    else if(e.target.tagName === "SPAN"){
        e.target.parentElement.remove();
        saveData();
    }
}, false);

function saveData(){
    localStorage.setItem("data", listContainer.innerHTML);
}

function showTask(){
    listContainer.innerHTML = localStorage.getItem("data");
}
showTask();


let display = document.querySelector(".display");
let days = document.querySelector(".days");
let previous = document.querySelector(".left");
let next = document.querySelector(".right");

let date = new Date();
let year = date.getFullYear();
let month = date.getMonth();

function displayCalendar() {
  days.innerHTML = "";

  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);

  const firstDayIndex = firstDay.getDay();
  const numberOfDays = lastDay.getDate();

  let formattedDate = date.toLocaleString("en-US", {
    month: "long",
    year: "numeric",
  });

  display.innerHTML = `${formattedDate}`;

  for (let x = 1; x <= firstDayIndex; x++) {
    const div = document.createElement("div");
    div.innerHTML = "";
    days.appendChild(div);
  }

  for (let i = 1; i <= numberOfDays; i++) {
    let div = document.createElement("div");
    let currentDate = new Date(year, month, i);

    div.dataset.date = currentDate.toDateString();
    div.innerHTML = i;
    days.appendChild(div);

    if (
      currentDate.getFullYear() === new Date().getFullYear() &&
      currentDate.getMonth() === new Date().getMonth() &&
      currentDate.getDate() === new Date().getDate()
    ) {
      div.classList.add("current-date");
    }
  }
}

displayCalendar();

previous.addEventListener("click", () => {
  if (month === 0) {
    month = 11;
    year--;
  } else {
    month--;
  }
  date.setMonth(month);
  displayCalendar();
});

next.addEventListener("click", () => {
  if (month === 11) {
    month = 0;
    year++;
  } else {
    month++;
  }
  date.setMonth(month);
  displayCalendar();
});
