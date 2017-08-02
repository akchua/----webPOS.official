define(['jquery'], function ($) {
    return {
    	getUser: function() {
    		return $.ajax({
    			url: '/services/security/user'
    		});
    	},
    	
    	login: function(username, password) {
    		return $.ajax({
    			url: '/login',
    			method: 'POST',
    			data: {
    				username: username,
    				password: password
    			}
    		});
    	},
    	
    	logout: function() {
    		return $.ajax({
    			url: '/services/security/logout',
    			method: 'POST'
    		});
    	}
    };
});