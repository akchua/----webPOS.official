define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice'], function (dialog, app, ko, userService) {
    var Settings = function() {
    	this.user = app.user;
    	
    	this.itemsPerPageList = ko.observableArray([5, 10, 15, 20]);
    	
    	this.settingsFormModel = {
    		id: ko.observable(),
    		
    		itemsPerPage: ko.observable()
	    };
    };
    
    Settings.prototype.activate = function() {
    	var self = this;
    	
    	self.settingsFormModel.id(self.user.id);
    	self.settingsFormModel.itemsPerPage(self.user.itemsPerPage);
    };
    
    Settings.show = function() {
    	return dialog.show(new Settings());
    };
    
    Settings.prototype.save = function() {
    	var self = this;
    	
        userService.changeSettings(ko.toJSON(self.settingsFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    Settings.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return Settings;
});