define(['jquery'], function ($) {
    return {
    	saveSettings: function(settingsFormData) {
    		return $.ajax({
    			url: '/services/settings/save',
    			method: 'POST',
    			data: {
    				settingsFormData: settingsFormData
    			}
    		});
    	},
    	
    	changePassword: function(passwordData) {
    		return $.ajax({
    			url: '/services/settings/changepassword',
    			method: 'POST',
    			data: {
    				passwordData: passwordData
    			}
    		});
    	}
    };
});