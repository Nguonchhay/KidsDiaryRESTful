function checkCookie() {
	var data = window.cookie.getCookie(window.params.cookieKey);
	var user = JSON.parse(data);
	if (user.accessToken === undefined) {
		document.location.href = window.params.baseUrl + '/login.html';
	}
}

function showLoginUser() {
	var data = window.cookie.getCookie(window.params.cookieKey);
	var user = JSON.parse(data);
	if (user.accessToken !== undefined) {
		$('#loginUsername').html(user.username);
	}
}

$(document).ready(function() {
	checkCookie();
	showLoginUser();
});