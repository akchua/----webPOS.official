define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice'], 
		function (dialog, app, ko, userService) {
    var UserView = function(user) {
    	this.user = user;
    	
    	this.userViewModel = {
    		id: ko.observable(),
    		
    		imagePath: ko.observable(),
    		name: ko.observable(),
    		
    		contactNumber: ko.observable(),
    		username: ko.observable(),
    		email: ko.observable(),
    		userType: ko.observable(),
    		
    		formattedCreatedOn: ko.observable()
	    };
    };
    
    UserView.prototype.activate = function() {
    	var self = this;
    	
    	self.userViewModel.id(self.user.id);
    	
    	self.userViewModel.imagePath(userService.getUserImageByFileName(self.user.image));
    	self.userViewModel.name(self.user.formattedName);
    	
    	self.userViewModel.username(self.user.username);
    	self.userViewModel.contactNumber(self.user.contactNumber);
    	self.userViewModel.email(self.user.emailAddress);
    	self.userViewModel.userType(self.user.userType.displayName);
    	
    	self.userViewModel.formattedCreatedOn(self.user.formattedCreatedOn);
    };
    
    UserView.show = function(user) {
    	return dialog.show(new UserView(user));
    };
    
    UserView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return UserView;
});