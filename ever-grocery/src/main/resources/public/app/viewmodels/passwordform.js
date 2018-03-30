define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice'], function (dialog, app, ko, userService) {
    var PasswordForm = function() {
    	this.userId = app.user.id;
    	
    	this.passwordFormModel = {
    		id: ko.observable(),
    		
    		oldPassword: ko.observable(),
	    	password: ko.observable(),
	    	confirmPassword: ko.observable()
	    };
    	
    	this.errors = {
			oldPassword: ko.observable(),
	    	password: ko.observable(),
	    	confirmPassword: ko.observable()
    	};
    };
    
    PasswordForm.prototype.activate = function() {
    	var self = this;
    	
    	self.passwordFormModel.id(self.userId);
    };
    
    PasswordForm.show = function() {
    	return dialog.show(new PasswordForm());
    };
    
    PasswordForm.prototype.changePassword = function() {
    	var self = this;
    	
        userService.changePassword(ko.toJSON(self.passwordFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	}  else {
        		if(result.extras && result.extras.errors) {
	        		self.passwordFormModel.oldPassword('');
	        		self.passwordFormModel.password('');
	        		self.passwordFormModel.confirmPassword('');
	        		self.errors.oldPassword(result.extras.errors.oldPassword);
	        		self.errors.password(result.extras.errors.password);
	        		self.errors.confirmPassword(result.extras.errors.confirmPassword);
        		}
        	}
        	if(result.message) app.showMessage(result.message);
        });
    };
    
    PasswordForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return PasswordForm;
});