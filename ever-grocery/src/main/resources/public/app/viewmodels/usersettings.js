define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/settingsservice'], function (dialog, app, ko, settingsService) {
    var UserSettings = function(user) {
        this.user = user;
        
        this.itemsPerPageList = ko.observableArray([5, 10, 15, 20]);
        
        this.userSettingsFormModel = {
        	firstName: ko.observable(),
        	lastName: ko.observable(),
        	itemsPerPage: ko.observable(),
        	username: ko.observable()
        };
        
        this.changePasswordModel = {
        	
        	currentPassword: ko.observable(),
        	newPassword: ko.observable(),
        	rePassword: ko.observable()
        };
    };
    
    UserSettings.prototype.activate = function() {
    	var self = this;
    	
    	self.userSettingsFormModel.firstName(self.user.firstName);
    	self.userSettingsFormModel.lastName(self.user.lastName);
    	self.userSettingsFormModel.itemsPerPage(self.user.itemsPerPage);
    	self.userSettingsFormModel.username(self.user.username);
    };
 
    UserSettings.show = function(user) {
        return dialog.show(new UserSettings(user));
    };
    
    UserSettings.prototype.save = function() {
    	var self = this;
		settingsService.saveSettings(ko.toJSON(self.userSettingsFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);
        	} 
        	app.showMessage(result.message);
        });
    };
    
    UserSettings.prototype.changePassword = function() {
    	var self = this;
    	
    	if(self.changePasswordModel.currentPassword() && self.changePasswordModel.newPassword() && self.changePasswordModel.rePassword()) {
	    	settingsService.changePassword(ko.toJSON(self.changePasswordModel)).done(function(result) {
	    		if(result.success) {
	    			dialog.close(self);
	    		}
	    		app.showMessage(result.message);
	    	});
    	}
    };
    
    UserSettings.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return UserSettings;
});