define(['durandal/app', 'knockout', 'modules/soundutility', 'modules/customerorderservice', 'modules/customerservice', 'viewmodels/customer-order/search'], function (app, ko, soundUtil, customerOrderService, customerService, Search) {
    var CustomerOrderPage = function() {
    	this.customerOrderDetailList = ko.observable();
    	
    	this.barcodeKey = ko.observable();
    	this.barcodeFocus = ko.observable(true);
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.customerOrderPageModel = {
			customerOrderId: ko.observable(),
			totalAmount: ko.observable(),
			customerOrderName: ko.observable(),
			status: ko.observable()
		};
    };
    
    CustomerOrderPage.prototype.activate = function(customerOrderId) {
    	var self = this;
    	
    	self.customerOrderPageModel.customerOrderId(customerOrderId);
    	
    	self.currentPage(1);
    	self.currentPageSubscription = self.currentPage.subscribe(function() {
    		self.refreshCustomerOrderDetailList();
		});
    	
    	customerOrderService.refreshCustomerOrder(self.customerOrderPageModel.customerOrderId()).done(function() {
    		self.refreshCustomerOrderDetailList();
    	});
    };
    
    CustomerOrderPage.prototype.search = function() {
    	var self = this;
    	
    	customerOrderService.getCustomerOrder(self.customerOrderPageModel.customerOrderId()).done(function(data) { 
    		Search.show(data).done(function() {
    			self.currentPage(1);
        		self.refreshCustomerOrderDetailList();
        	});
    	});
    };
    
    CustomerOrderPage.prototype.refreshCustomerOrderDetailList = function() {
    	var self = this;
    	
    	customerOrderService.getCustomerOrder(self.customerOrderPageModel.customerOrderId()).done(function(data) { 
    		self.customerOrderPageModel.totalAmount(data.totalAmount);
    		self.customerOrderPageModel.customerOrderName(data.name);
    		self.customerOrderPageModel.status(data.status);
    	});
    	
    	customerOrderService.getCustomerOrderDetailList(self.currentPage(), self.customerOrderPageModel.customerOrderId(), true).done(function(data) { 
			self.customerOrderDetailList(data.list);
			self.totalItems(data.total);
		});
    	
    	self.barcodeFocus(true);
    };
    
    CustomerOrderPage.prototype.remove = function(customerOrderDetailId, quantity, productName, unitType) {
		var self = this;
		
		app.showMessage('Are you sure you want to remove ' + quantity + ' "' + productName + ' (' + unitType + ')"?',
				'Confirm Remove',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.removeCustomerOrderDetail(customerOrderDetailId).done(function(result) {
					self.refreshCustomerOrderDetailList();
					if(!result.success) {
						app.showMessage(result.message);
					}
				});
			}
		});
	};
    
    CustomerOrderPage.prototype.addItemByBarcode = function() {
    	var self = this;
    	
    	customerOrderService.addItemByBarcode(self.barcodeKey(), self.customerOrderPageModel.customerOrderId()).done(function(result) {	
    		if(result.success) {
    			self.currentPage(1);
    			self.refreshCustomerOrderDetailList();
    		} else {
    			soundUtil.beep();
    			app.showMessage(result.message);
    		}
    	});
    	
    	self.barcodeKey("");
    };
    
    CustomerOrderPage.prototype.changeQuantity = function(customerOrderDetailId, quantity) {
    	var self = this;
    	
    	customerOrderService.changeQuantity(customerOrderDetailId, quantity).done(function(result) {
    		if(result.success) {
    			self.currentPage(1);
    		}
    		self.refreshCustomerOrderDetailList();
    		if(!result.success) {
    			app.showMessage(result.message);
    		}
    	});
    };
    
    return CustomerOrderPage;
});