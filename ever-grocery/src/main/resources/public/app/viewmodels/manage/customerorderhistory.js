define(['durandal/app', 'knockout', 'modules/customerservice', 'modules/customerorderservice', 'viewmodels/report/saleview'], 
		function (app, ko, customerService, customerOrderService, SaleView) {
    var CustomerOrderHistory = function() {
    	this.customerId = ko.observable();
    	this.customerOrderList = ko.observable();
    	
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
    };
    
    CustomerOrderHistory.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.customerId = activationData.customerId;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCustomerOrderList();
		});
		
    	self.refreshCustomerOrderList();
    };
    
	CustomerOrderHistory.prototype.refreshCustomerOrderList = function() {
		var self = this;
		
		customerOrderService.getCustomerOrderListByCustomer(self.currentPage(), self.customerId(), false).done(function(data) {
			self.customerOrderList(data.list);
			self.totalItems(data.total);
		});
	};
    
	CustomerOrderHistory.prototype.viewCustomerOrder = function(customerOrderId) {
		var self = this;
		self.enableButtons(false);
		
		customerOrderService.getCustomerOrder(customerOrderId).done(function(data) {
			SaleView.show(data).done(function() {
				self.refreshCustomerOrderList();
				self.enableButtons(true);
			});
		});
	};
    
    return CustomerOrderHistory;
});