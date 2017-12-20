define(['durandal/app', 'knockout', 'modules/securityservice', 'modules/customerorderservice', 'viewmodels/cashier/payform', 'viewmodels/report/saleview'], 
		function (app, ko, securityService, customerOrderService, PayForm, SaleView) {
	var Cashier = function() {
		this.customerOrderList = ko.observable();
		
		this.searchKey = ko.observable();
		this.showPaid = ko.observable(false);
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
	};
	
	Cashier.prototype.activate = function() {
		var self = this;

		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCustomerOrderList();
		});
		self.showPaid.subscribe(function() {
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
		
		customerOrderService.getCustomerOrderList(self.currentPage(), self.searchKey(), self.showPaid(), 7).done(function(data) {
			self.customerOrderList(data.list);
			self.totalItems(data.total);
		});
	};
	
	Cashier.prototype.view = function(customerOrderId) {
		var self = this;
		
		customerOrderService.getCustomerOrder(customerOrderId).done(function(data) {
			SaleView.show(data).done(function() {
				self.refreshCustomerOrderList();
			});
		});
	};
	
	Cashier.prototype.pay = function(customerOrderId) {
		var self = this;
		
		customerOrderService.refreshCustomerOrder(customerOrderId).done(function() {
			customerOrderService.getCustomerOrder(customerOrderId).done(function(data) {
				PayForm.show(data).then(function() {
					self.refreshCustomerOrderList();
				});
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