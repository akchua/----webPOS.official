define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice'], function (dialog, app, ko, userService) {
    var UserForm = function(preTitle, user) {
        this.preTitle = preTitle;
        this.user = user;
        
        this.itemsPerPageList = ko.observableArray([5, 10, 15, 20]);
        
        this.userTypeList = ko.observable();
        
        this.userFormModel = {
        	id: ko.observable(),
        	firstName: ko.observable(),
        	lastName: ko.observable(),
        	itemsPerPage: ko.observable(),
        	username: ko.observable(),
        	password: ko.observable(),
        	userType: ko.observable()
        };
    };
    
    UserForm.prototype.activate = function() {
    	var self = this;
    	
    	self.userFormModel.id(self.user.id);
    	self.userFormModel.firstName(self.user.firstName);
    	self.userFormModel.lastName(self.user.lastName);
    	self.userFormModel.itemsPerPage(self.user.itemsPerPage);
    	self.userFormModel.username(self.user.username);
    	self.userFormModel.userType(self.user.userType);
    	
    	userService.getUserTypeList().done(function(userTypeList) {
    		self.userTypeList(userTypeList);
    		self.userFormModel.userType(self.user.userType);
    	});
    };
 
    UserForm.show = function(preTitle, user) {
        return dialog.show(new UserForm(preTitle, user));
    };
    
    UserForm.prototype.save = function() {
    	var self = this;
    	
        userService.saveUser(ko.toJSON(self.userFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    UserForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return UserForm;
});