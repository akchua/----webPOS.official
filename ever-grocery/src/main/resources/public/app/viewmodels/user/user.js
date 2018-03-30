define(['durandal/app', 'knockout', 'modules/userservice', 'viewmodels/user/userform', 'viewmodels/user/userview'],
		function (app, ko, userService, UserForm, UserView) {
    var User = function() {
    	this.userList = ko.observable();
    	
    	this.searchKey = ko.observable();
    	
    	this.enableReset = ko.observable(true);
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    User.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshUserList();
		});
		
		self.searchKey.subscribe(function(searchKey) {
			if(searchKey.length >= 3) {
				self.search();
			}
		});
		
		self.refreshUserList();
    };
    
    User.prototype.refreshUserList = function() {
    	var self = this;
    	
    	userService.getUserList(self.currentPage(), self.searchKey()).done(function(data) {
    		for(i = 0; i < data.list.length; i++) {
    			data.list[i].imagePath = userService.getUserImageByFileName(data.list[i].image);
    		}
    		self.userList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    User.prototype.add = function() {
    	var self = this;
    	
    	UserForm.show(new Object(),  'New User').done(function() {
    		self.refreshUserList();
    	});
    };
    
    User.prototype.view = function(userId) {
    	var self = this;
    	
    	userService.getUser(userId).done(function(user) {
    		UserView.show(user);
    	});
    };
    
    User.prototype.resetPassword = function(userId, formattedName) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to reset password of User <span class="text-primary">' + formattedName + '</span>?</p>'
    				+ '<p>The new password will be sent to the user\'s email.</p>',
				'<p class="text-danger">Confirm Reset Password</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				self.enableReset(false);
				userService.resetPassword(userId).done(function(result) {
					self.refreshUserList();
					app.showMessage(result.message);
					self.enableReset(true);
				});
			}
		})
    };
    
    User.prototype.edit = function(userId) {
    	var self = this;
    	
    	userService.getUser(userId).done(function(user) {
    		UserForm.show(user,  'Edit User').done(function() {
        		self.refreshUserList();
        	});
    	});
    };
    
    User.prototype.remove = function(userId, formattedName) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove User <span class="text-primary">' + formattedName + '</span>?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				userService.removeUser(userId).done(function(result) {
					self.refreshUserList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    User.prototype.search = function() {
    	var self = this;
    	
    	self.currentPage(1);
    	self.refreshUserList();
    };
    
    return User;
});