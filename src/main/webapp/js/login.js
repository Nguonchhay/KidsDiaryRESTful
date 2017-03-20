$(document).ready(function() {
	$.ajaxSetup({
	    cache: false,
	    data: null
	});
	
	/**
	 * Clear all cookies
	 */
	window.cookie.setCookie(window.params.cookieKey, '{}', 0);
	window.cookie.setCookie('verify', '', 0);

	/**
	 * @returns {String}
	 */
	function getBaseUrl() {
		var pathArray = location.href.split( '/' );
		var protocol = pathArray[0];
		var host = pathArray[2];
		var url = protocol + '//' + host;
		return url;
	}

	var apiBaseUrl = getBaseUrl() + "/api";
	
	/**
     * Get UserType to select
     */
    $.ajax({
        type        : "GET",
        url         : apiBaseUrl + "/usertypes/v1/all",
        data        : {},
        dataType    : "text",
        error       : function(jqXHR, textStatus, errorThrown) {
            console.log('getJSON request failed! ' + textStatus);
            var json = JSON.parse(jqXHR.responseText);
            alert(json.message);
        },
        success     : function(data, textStatus, jqXHR) {
          var userTypes = JSON.parse(data);
      	var options = '';
      	if (userTypes.length) {
      		$.each(userTypes, function(index, userType) {
      			if (userType.id != 3) {
      				options += '<option value="' + userType.id + '">' + userType.type + '</option>';
      			}
            });
      	}
        	$('#userType').html(options);
        }
    });
    
    /**
     * Get Countries to select
     */
    $.ajax({
        type        : "GET",
        url         : apiBaseUrl + "/countries/v1/all",
        data        : {},
        dataType    : "text",
        error       : function(jqXHR, textStatus, errorThrown) {
            console.log('getJSON request failed! ' + textStatus);
            var json = JSON.parse(jqXHR.responseText);
            alert(json.message);
        },
        success     : function(data, textStatus, jqXHR) {
          var countries = JSON.parse(data);
      	var options = '';
      	if (countries.length) {
      		$.each(countries, function(index, country) {
      			if (userType.id != 3) {
      				options += '<option value="' + country.id + '">' + country.name + '</option>';
      			}
            });
      	}
        	$('#country').html(options);
        }
    });

    /**
     * Register user account
     */
	$('#btnCreateAccount').click(function() {
		var data = {
            username   : $("#username").val(),
            password   : $("#password").val(),
            firstName  : $("#firstName").val(),
            lastName   : $("#lastName").val(),
            country    : parseInt($("#country").val()),
            userType   : parseInt($("#userType").val()),
            sex        : $("#sex").val(),
            email      : $("#email").val(),
            birthDate  : '1970-01-01',
            phone      : '',
            accessToken: '0'
        };
		
	    $.ajax({
	        type        : "POST",
	        url         : apiBaseUrl + "/users/v1",
	        data        : data,
	        dataType    : "text",
	        error       : function(jqXHR, textStatus, errorThrown) {
	            console.log('getJSON request failed! ' + textStatus);
	            var json = JSON.parse(jqXHR.responseText);
	            alert(json.message);
	        },
	        success     : function(data, textStatus, jqXHR) {
	        	var user = JSON.parse(data);
	        	if (user.accessToken !== undefined) {
	        		window.cookie.setCookie(window.params.cookieKey, data, window.params.expireDay * 24);
	        		window.cookie.setCookie('verify', user.accessToken, window.params.expireDay * 24);
	        		document.location.href = 'verify-account.html';
	        	} else {
	        		alert('Something went wrong while trying to create acount. Please try again!');
	        	}
	        }
	    });
	});

	$('#passwordUtil').click(function() {
		var item = $(this).find('i.fa');
		var password = $('#password');
		
		if (item.hasClass('fa-eye')) {
			password.attr('type', 'password');
			item.removeClass('fa-eye');
			item.addClass('fa-eye-slash');
		} else {
			password.attr('type', 'text');
			item.addClass('fa-eye');
			item.removeClass('fa-eye-slash');
		}
	});
	
	/**
     * Register user account
     */
	$('#btnLogin').click(function() {
		var data = {
            username   : $("#loginUsername").val(),
            password   : $("#loginPassword").val()
        };
		
	    $.ajax({
	        type        : "POST",
	        url         : apiBaseUrl + "/users/v1/login",
	        data        : data,
	        dataType    : "text",
	        error       : function(jqXHR, textStatus, errorThrown) {
	            console.log('getJSON request failed! ' + textStatus);
	            var json = JSON.parse(jqXHR.responseText);
	            alert(json.message);
	        },
	        success     : function(data, textStatus, jqXHR) {
	        	var user = JSON.parse(data);
	        	if (user.accessToken !== undefined) {
	        		window.cookie.setCookie(window.params.cookieKey, data, window.params.expireDay * 24);
	        		document.location.href = 'index.html';
	        	} else {
	        		alert('Username or passsword is incorrect!');
	        	}
	        }
	    });
	});
});