//An excellent choice : http://stevenlevithan.com/assets/misc/date.format.js
function displayDateTime() {

	var weekDay = new Array("Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday");
	var monthName = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
			"Aug", "Sep", "Oct", "Nov", "Dec");

	var currentDateTime = new Date();

	var currentHours = currentDateTime.getHours();
	var currentMinutes = currentDateTime.getMinutes();
	var currentSeconds = currentDateTime.getSeconds();
	var timeMarker = (currentHours < 12) ? "AM" : "PM";

	currentHours = (currentHours > 12) ? currentHours - 12 : currentHours;
	// currentHours = (currentHours == 0) ? 12 : currentHours;
	currentMinutes = (currentMinutes < 10 ? "0" : "") + currentMinutes;
	currentSeconds = (currentSeconds < 10 ? "0" : "") + currentSeconds;

	var currentTimeString = currentHours + ":" + currentMinutes + ":"
			+ currentSeconds + " " + timeMarker;

	var currentWeekDay = currentDateTime.getDay();
	var currentDate = currentDateTime.getDate();
	var currentMonth = currentDateTime.getMonth();
	var currentYear = currentDateTime.getFullYear();

	var currentDateString = weekDay[currentWeekDay] + " " + currentDate + " "
			+ monthName[currentMonth] + "," + currentYear;

	document.getElementById("retaledatetime").innerHTML = currentDateString
			+ " " + currentTimeString;

}
