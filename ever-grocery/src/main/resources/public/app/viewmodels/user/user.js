define(['durandal/app', 'knockout', 'modules/userservice', 'viewmodels/user/userform'], function (app, ko, userService, UserForm) {
	var User = function() {
		this.userList = ko.observable();
		
		this.searchKey = ko.observable();
		
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
			self.userList(data.list);
			self.totalItems(data.total);
		});
	};
	
	User.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshUserList();
	};
	
	User.prototype.create = function() {
		var self = this;
		
		UserForm.show('Create', new Object()).then(function() {
			self.refreshUserList();
		});
	};
	
	User.prototype.edit = function(userId) {
		var self = this;
		
		userService.getUser(userId).done(function(data) {
			UserForm.show('Update', data).then(function() {
				self.refreshUserList();
			});
		});
	};
	
	User.prototype.remove = function(userId, userName) {
		var self = this;
		
		app.showMessage('Are you sure you want to remove User "' + userName + '"?',
				'Confirm Remove',
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
	
    return User;
});