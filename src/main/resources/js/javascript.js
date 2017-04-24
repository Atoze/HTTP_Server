function altRan() {
	var r = Math.floor(Math.random() * 6) +1; //乱数の発生

	document.querySelectorAll("dice").innerHTML = r;
	//document.getElementById("dice").innerHTML = r; //値の出力
}
