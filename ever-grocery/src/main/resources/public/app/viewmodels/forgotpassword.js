define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice'], function (dialog, app, ko, userService) {
    var ForgotPassword = function() {
    	this.retrievalKey = ko.observable();
    };
    
    ForgotPassword.show = function() {
    	return dialog.show(new ForgotPassword());
    };
    
    ForgotPassword.prototype.retrieve = function() {
    	var self = this;
    	
        userService.retrieveUser(self.retrievalKey()).done(function(user) {
        	if(user) {
        		app.showMessage('<p>Are you <span class="text-primary">' + user.firstName + ' ' + user.lastName + '</span>?</p>'
	        				+ '<p>By clicking yes, you are resetting your password and the new password will be sent to your email.</p>',
	    				'<p class="text-danger">Confirm Reset Password</p>',
	    				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
	    		.then(function(confirm) {
	    			if(confirm) {
	    				userService.resetPassword(user.id).done(function(result) {
	    					app.showMessage(result.message);
	    				});
	    			}
	    		})
        	} else {
        		app.showMessage("<p>Username / Email Address does not exist. Contact us for support.</p>")
        	}
        });
    };
    
    ForgotPassword.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ForgotPassword;
});