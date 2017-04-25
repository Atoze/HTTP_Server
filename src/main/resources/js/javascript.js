function altRan() {
	var r = Math.floor(Math.random() * 6) +1; //乱数の発生
    document.getElementById("dice").innerHTML = r; //値の出力
}
