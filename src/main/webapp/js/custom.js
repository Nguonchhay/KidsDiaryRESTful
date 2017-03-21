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

window.getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

$(document).ready(function() {
	checkCookie();
	showLoginUser();
});