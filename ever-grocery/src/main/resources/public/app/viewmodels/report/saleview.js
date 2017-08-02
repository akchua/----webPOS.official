define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice'], function (dialog, app, ko, customerOrderService) {
    var SaleView = function(customerOrder) {
    	this.customerOrder = customerOrder;
    	this.customerOrderDetailList = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.customerOrderViewModel = {
			customerOrderId: ko.observable(),
			name: ko.observable(),
			totalAmount: ko.observable()
		};
    };
    
    SaleView.prototype.activate = function() {
    	var self = this;
    	
    	self.customerOrderViewModel.customerOrderId(self.customerOrder.id);
    	self.customerOrderViewModel.name(self.customerOrder.name);
    	self.customerOrderViewModel.totalAmount(self.customerOrder.totalAmount);
    	
    	self.currentPage(1);
    	self.currentPageSubscription = self.currentPage.subscribe(function() {
    		self.refreshCustomerOrderDetailList();
		});
    	
    	self.refreshCustomerOrderDetailList();
    };
    
    SaleView.show = function(customerOrder) {
    	return dialog.show(new SaleView(customerOrder));
    };
    
    SaleView.prototype.refreshCustomerOrderDetailList = function() {
    	var self = this;
    	
    	customerOrderService.getCustomerOrderDetailList(self.currentPage(), self.customerOrderViewModel.customerOrderId(), false).done(function(data) { 
			self.customerOrderDetailList(data.list);
			self.totalItems(data.total);
		});
    };
    
    SaleView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return SaleView;
});