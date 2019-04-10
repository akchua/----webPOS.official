define(['durandal/app', 'knockout', 'modules/securityservice', 'modules/constantsservice', 'viewmodels/forgotpassword'], 
		function (app, ko, securityService, constantsService, ForgotPasswordForm) {
	var Login = function() {
		this.username = ko.observable();
		
		this.password = ko.observable();
		
		this.errorMessage = ko.observable();
		
		this.systemVersion = ko.observable();
	};
	
	Login.prototype.activate = function() {
		var self = this;
		
		constantsService.getVersion().done(function(version) {
			self.systemVersion(version);
		});
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