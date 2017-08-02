define(['durandal/app', 'knockout', 'modules/customerservice', 'viewmodels/manage/customerform'], function (app, ko, customerService, CustomerForm) {
	var Customer = function() {
		this.customerList = ko.observable();
		
		this.searchKey = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
	};
	
	Customer.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCustomerList();
		});
		
		self.searchKey.subscribe(function(searchKey) {
			if(searchKey.length >= 3) {
				self.search();
			}
		});
		
		self.refreshCustomerList();
	};
	
	Customer.prototype.refreshCustomerList = function() {
		var self = this;
		
		customerService.getCustomerList(self.currentPage(), self.searchKey()).done(function(data) {
			self.customerList(data.list);
			self.totalItems(data.total);
		});
	};
	
	Customer.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshCustomerList();
	};
	
	Customer.prototype.create = function() {
		var self = this;
		
		CustomerForm.show('Create', new Object()).then(function() {
			self.refreshCustomerList();
		});
	};
	
	Customer.prototype.edit = function(customerId) {
		var self = this;
		
		customerService.getCustomer(customerId).done(function(data) {
			CustomerForm.show('Update', data).then(function() {
				self.refreshCustomerList();
			});
		});
	};
	
	Customer.prototype.remove = function(customerId, customerFirstName, customerLastName) {
		var self = this;
		
		app.showMessage('Are you sure you want to remove Customer "' + customerLastName + ", " + customerFirstName + '"?',
				'Confirm Remove',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerService.removeCustomer(customerId).done(function(result) {
					self.refreshCustomerList();
					app.showMessage(result.message);
				});
			}
		})
	};
	
    return Customer;
});