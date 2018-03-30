define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice', 'viewmodels/user/usergallery'], 
		function (dialog, app, ko, userService, UserGallery) {
    var Profile = function(user) {
    	this.user = user;
    	
    	this.imagePath = ko.observable()
    	
    	this.userFormModel = {
    		id: ko.observable(),
    		
    		firstName: ko.observable(),
	    	lastName: ko.observable(),
	    	contactNumber: ko.observable(),
	    	
	    	username: ko.observable(),
	    	emailAddress: ko.observable(),
	    	password: ko.observable(),
	    	confirmPassword: ko.observable(),
	    	itemsPerPage: ko.observable(),
	    	userType: ko.observable()
	    };
    	
    	this.errors = {
			firstName: ko.observable(),
	    	lastName: ko.observable(),
	    	contactNumber: ko.observable(),
	    	emailAddress: ko.observable()
    	};
    };
    
    Profile.prototype.activate = function() {
    	var self = this;
    	
    	self.userFormModel.id(self.user.id);
    	
    	self.imagePath(userService.getUserImageByFileName(self.user.image));
    	
    	self.userFormModel.firstName(self.user.firstName);
    	self.userFormModel.lastName(self.user.lastName);
    	self.userFormModel.contactNumber(self.user.contactNumber);
    	
    	self.userFormModel.username(self.user.username);
    	self.userFormModel.emailAddress(self.user.emailAddress);
    	self.userFormModel.itemsPerPage(self.user.itemsPerPage);
    	if(self.user.userType) self.userFormModel.userType(self.user.userType.name);
    };
    
    Profile.show = function(user) {
    	return dialog.show(new Profile(user));
    };
    
    Profile.prototype.save = function() {
    	var self = this;
    	
        userService.saveUser(ko.toJSON(self.userFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} else {
        		if(result.extras && result.extras.errors) {
	        		self.errors.firstName(result.extras.errors.firstName);
	        		self.errors.lastName(result.extras.errors.lastName);
	        		self.errors.contactNumber(result.extras.errors.contactNumber);
	        		self.errors.emailAddress(result.extras.errors.emailAddress);
        		}
        	}
        	if(result.message) app.showMessage(result.message);
        });
    };
    
    Profile.prototype.editImage = function() {
    	var self = this;
    	
    	UserGallery.show(app.user.id).done(function() {
    		userService.getUser(self.userFormModel.id()).done(function(user) {
    			self.imagePath(userService.getUserImageByFileName(user.image));
    		});
    	});
    };
    
    Profile.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return Profile;
});