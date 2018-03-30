define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice', 'modules/constantsservice'], 
		function (dialog, app, ko, userService, constantsService) {
    var UserForm = function(user, title) {
    	this.user = user;
    	this.title = title;
    	
    	this.userTypeList = ko.observable();
    	
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
	    	
	    	username: ko.observable(),
	    	emailAddress: ko.observable(),
	    	password: ko.observable(),
	    	confirmPassword: ko.observable(),
	    	itemsPerPage: ko.observable(),
	    	userType: ko.observable()
    	};
    };
    
    UserForm.prototype.activate = function() {
    	var self = this;
    	
    	self.userFormModel.id(self.user.id);
    	self.userFormModel.firstName(self.user.firstName);
    	self.userFormModel.lastName(self.user.lastName);
    	self.userFormModel.contactNumber(self.user.contactNumber);
    	self.userFormModel.username(self.user.username);
    	self.userFormModel.emailAddress(self.user.emailAddress);
    	self.userFormModel.itemsPerPage(self.user.itemsPerPage);
    	if(self.user.userType) self.userFormModel.userType(self.user.userType.name);
    	
    	constantsService.getUserTypeList().done(function(userTypeList) {
    		self.userTypeList(userTypeList);
    		if(self.user.userType) self.userFormModel.userType(self.user.userType.name);
    	});
    };
    
    UserForm.show = function(user, title) {
    	return dialog.show(new UserForm(user, title));
    };
    
    UserForm.prototype.save = function() {
    	var self = this;
    	
        userService.saveUser(ko.toJSON(self.userFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);
        	} else {
        		self.errors.firstName(result.extras.errors.firstName);
        		self.errors.lastName(result.extras.errors.lastName);
        		self.errors.contactNumber(result.extras.errors.contactNumber);
        		self.errors.emailAddress(result.extras.errors.emailAddress);
        		self.errors.username(result.extras.errors.username);
        		self.errors.password(result.extras.errors.password);
        		self.errors.confirmPassword(result.extras.errors.confirmPassword);
        		self.errors.userType(result.extras.errors.userType);
        	}
        	if(result.message) app.showMessage(result.message);
        });
    };
    
    UserForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return UserForm;
});