define(['durandal/app', 'knockout', 'modules/securityservice', 'viewmodels/forgotpassword'], 
		function (app, ko, securityService, ForgotPasswordForm) {
	var Login = function() {
		this.username = ko.observable();
		
		this.password = ko.observable();
		
		this.errorMessage = ko.observable();
	};
	
	Login.prototype.login = function() {
		var self = this;
		
		self.errorMessage('');
		securityService.login(self.username(), self.password()).done(function(data) {
			if(data == 'SUCCESS') {
				securityService.getUser().done(function(user) {
	        		app.user = user;
	        		//Show the app by setting the root view model for our application with a transition.
	                app.setRoot('viewmodels/shell');
		        });
			} else { // FAILURE
				self.password('');
				self.errorMessage('Invalid Username / Password!');
			}
		});
	};
	
	Login.prototype.forgotPassword = function() {
		var self = this;
		
		ForgotPasswordForm.show();
	};
	
    return Login;
});