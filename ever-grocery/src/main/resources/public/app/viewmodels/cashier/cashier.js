define(['durandal/app', 'knockout', 'modules/securityservice', 'modules/customerorderservice', 'viewmodels/cashier/payform', 'viewmodels/report/saleview'], 
		function (app, ko, securityService, customerOrderService, PayForm, SaleView) {
	var Cashier = function() {
		this.customerOrderList = ko.observable();
		
		this.searchKey = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
	};
	
	Cashier.prototype.activate = function() {
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
	
	Cashier.prototype.refreshCustomerOrderList = function() {
		var self = this;
		
		customerOrderService.getCashierCustomerOrderList(self.currentPage(), self.searchKey()).done(function(data) {
			self.customerOrderList(data.list);
			self.totalItems(data.total);
		});
	};
	
	Cashier.prototype.view = function(customerOrderId) {
		var self = this;
		self.enableButtons(false);
		
		customerOrderService.getCustomerOrder(customerOrderId).done(function(data) {
			SaleView.show(data).done(function() {
				self.refreshCustomerOrderList();
				self.enableButtons(true);
			});
		});
	};
	
	Cashier.prototype.pay = function(customerOrderId) {
		var self = this;
		self.enableButtons(false);
		
		customerOrderService.refreshCustomerOrder(customerOrderId).done(function() {
			PayForm.show(customerOrderId).then(function() {
				self.refreshCustomerOrderList();
				self.enableButtons(true);
			});
		});
	};
	
	Cashier.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshCustomerOrderList();
	};
	
    return Cashier;
});