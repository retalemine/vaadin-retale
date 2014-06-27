function setTime() {
	var c = new Date();
	var date = fm(c.getDate()) + "." + fm(c.getMonth() + 1) + "."
			+ (c.getYear() + 1900);
	var time = c.getHours() + ':' + fm(c.getMinutes()) + ':'
			+ fm(c.getSeconds());
	document.getElementById('theDate').innerHTML = date;
	document.getElementById('theTime').innerHTML = time;
}

function fm(num) {
	if (num < 10) {
		return '0' + num;
	}
	return num;
}
