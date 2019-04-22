define(['durandal/app', 'knockout', 'modules/customerservice', 'viewmodels/manage/customerform'], 
		function (app, ko, customerService, CustomerForm) {
	var Customer = function() {
		this.customerList = ko.observable();
		
		this.searchKey = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
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
		self.enableButtons(false);
		
		CustomerForm.show('Create', new Object()).then(function() {
			self.refreshCustomerList();
			self.enableButtons(true);
		});
	};
	
	Customer.prototype.view = function(customerId) {
    	var self = this;
    	self.enableButtons(false);
    	
    	customerService.getCustomer(customerId).done(function(customer) {
    		alert('here');
    		/*CustomerView.show(customer).then(function() {
    			self.enableButtons(true);
    		});*/
    	});
    };
	
	Customer.prototype.edit = function(customerId) {
		var self = this;
		self.enableButtons(false);
		
		customerService.getCustomer(customerId).done(function(data) {
			CustomerForm.show('Update', data).then(function() {
				self.refreshCustomerList();
				self.enableButtons(true);
			});
		});
	};
	
	Customer.prototype.remove = function(customerId, customerFormattedName) {
		var self = this;
		self.enableButtons(false);
		
		app.showMessage('Are you sure you want to remove Customer "' + customerFormattedName + '"?',
				'Confirm Remove',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerService.removeCustomer(customerId).done(function(result) {
					self.refreshCustomerList();
					app.showMessage(result.message);
				});
			}
			self.enableButtons(true);
		})
	};
	
    return Customer;
});