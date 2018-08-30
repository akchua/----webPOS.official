define(['plugins/router', 'durandal/app', 'knockout', 'modules/soundutility', 'modules/customerorderservice', 'modules/customerservice', 'viewmodels/customer-order/search'], 
		function (router, app, ko, soundUtil, customerOrderService, customerService, Search) {
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
			customerOrderNumber: ko.observable(),
			formattedTotalAmount: ko.observable(),
			customerName: ko.observable()
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
    	
    	customerOrderService.getCustomerOrder(self.customerOrderPageModel.customerOrderId()).done(function(customerOrder) { 
    		self.customerOrderPageModel.customerOrderNumber(customerOrder.orderNumber);
    		self.customerOrderPageModel.formattedTotalAmount(customerOrder.formattedTotalAmount);
    		self.customerOrderPageModel.customerName(customerOrder.formattedName);
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
    	
    	if(self.barcodeKey() === 'e') {
    		self.submit();
    	} else if(self.barcodeKey() === 'p') {
    		self.printCopy();
    	} else if(self.barcodeKey() === 's') {
    		self.search();
    	} else {
    		customerOrderService.addItemByBarcode(self.barcodeKey(), self.customerOrderPageModel.customerOrderId()).done(function(result) {	
        		if(result.success) {
        			self.currentPage(1);
        			self.refreshCustomerOrderDetailList();
        		} else {
        			soundUtil.beep();
        			app.showMessage(result.message).done(function() {
        				self.barcodeFocus(true);
        			});
        		}
        	});
    	}
    	
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
    
    CustomerOrderPage.prototype.submit = function() {
    	var self = this;
    	
    	app.showMessage('<p>Submit order for <span class="text-primary">' + self.customerOrderPageModel.customerName() + '</span>?<br>' +
				'Make sure to label all packages with <span class="text-danger">#' + self.customerOrderPageModel.customerOrderNumber() + '</span>.<br><br>' +
				'By clicking confirm, you will be forwarding the order to the cashier.</p>',
		'Submit Order',
		[{ text: 'Confirm', value: true }, { text: 'Cancel', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.submitCustomerOrder(self.customerOrderPageModel.customerOrderId()).done(function(result) {
					if(result.success) {
						router.navigate('#customerorder');
					} else {
						app.showMessage(result.message).done(function() {
							self.barcodeFocus(true);
						});
					}
				});
			} else {
				self.barcodeFocus(true);
			}
		})
    };
    
    CustomerOrderPage.prototype.printCopy = function() {
		var self = this;
		
		app.showMessage('<p>Confirm print copy of Order <span class="text-danger">#' + self.customerOrderPageModel.customerOrderNumber() + '</span>',
				'Print Order Copy',
				[{ text: 'Confirm', value: true }, { text: 'Cancel', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.printCustomerOrderCopy(self.customerOrderPageModel.customerOrderId()).done(function(result) {
					if(!result.success) {
						app.showMessage(result.message).done(function() {
							self.barcodeFocus(true);
						});
					}
				});
			} else {
				self.barcodeFocus(true);
			}
		})
	};
    
    return CustomerOrderPage;
});