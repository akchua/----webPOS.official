define(['plugins/router', 'durandal/app', 'knockout', 'modules/customerorderservice', 'viewmodels/customer-order/customerorderform', 'viewmodels/customer-order/customerorderpage'], 
		function (router, app, ko, customerOrderService, CustomerOrderForm, CustomerOrderPage) {
	var CustomerOrder = function() {
		this.customerOrderList = ko.observable();
		
		this.searchKey = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
	};
	
	CustomerOrder.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCustomerOrderList();
		});
		
		self.searchKey.subscribe(function(searchKey) {
			if(searchKey.length >= 3) {
				self.search();
			}
		});
		
		self.refreshCustomerOrderList();
	};
	
	CustomerOrder.prototype.refreshCustomerOrderList = function() {
		var self = this;
		
		customerOrderService.getCustomerOrderList(self.currentPage(), self.searchKey(), false, null).done(function(data) {
			self.customerOrderList(data.list);
			self.totalItems(data.total);
		});
	};
	
	CustomerOrder.prototype.create = function() {
		var self = this;
		
		CustomerOrderForm.show('Create', new Object()).then(function() {
			self.refreshCustomerOrderList();
		});
	};
	
	
	CustomerOrder.prototype.edit = function(customerOrderId) {
		var self = this;
		
		customerOrderService.getCustomerOrder(customerOrderId).done(function(data) {
			CustomerOrderForm.show('Update', data).then(function() {
				self.refreshCustomerOrderList();
			});
		});
	};
	
	CustomerOrder.prototype.remove = function(customerOrderId, customerOrderName) {
		var self = this;
		
		app.showMessage('Are you sure you want to cancel Customer Order "' + customerOrderName + '"?',
				'Confirm Remove',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.removeCustomerOrder(customerOrderId).done(function(result) {
					self.refreshCustomerOrderList();
					app.showMessage(result.message);
				});
			}
		})
	};
	
	CustomerOrder.prototype.details = function(customerOrderId) {
		var self = this;
		
		router.navigate('#customerorderpage/' + customerOrderId);
	};
	
	CustomerOrder.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshCustomerOrderList();
	};
	
    return CustomerOrder;
});