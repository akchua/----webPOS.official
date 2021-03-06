define(['plugins/router', 'durandal/app', 'knockout', 'modules/soundutility', 'modules/customerorderservice', 'modules/customerservice', 'viewmodels/customer-order/search', 'viewmodels/customer-order/invalidbarcode'], 
		function (router, app, ko, soundUtil, customerOrderService, customerService, Search, InvalidBarcode) {
    var CustomerOrderPage = function() {
    	this.customerOrderDetailList = ko.observable();
    	
    	this.customerCode = ko.observable();
    	this.customerFormattedName = ko.observable();
    	this.hasCustomer = ko.observable(false);
    	
    	this.barcodeKey = ko.observable();
    	this.barcodeFocus = ko.observable(true);
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.customerOrderPageModel = {
			customerOrderId: ko.observable(),
			customerOrderNumber: ko.observable(),
			cartonCount: ko.observable(0),
			plasticCount: ko.observable(0),
			bagCount: ko.observable(0),
			formattedGrossAmount: ko.observable(),
			formattedOutrightPromoDiscount: ko.observable(),
			formattedTotalAmount: ko.observable()
		};
		
		this.enableAddByBarcode = ko.observable(true);
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
    
    CustomerOrderPage.prototype.setCustomerByCode = function() {
		var self = this;
		self.enableAddByBarcode(false);
		
		customerOrderService.setCustomerByCode(self.customerOrderPageModel.customerOrderId(), self.customerCode()).done(function(result) {
			if(result.success) {
				self.refreshCustomerOrderDetailList();
				self.barcodeFocus(true);
			} else {
				app.showMessage(result.message);
			}
			self.customerCode('');
			self.enableAddByBarcode(true);
		});
	};
	
	CustomerOrderPage.prototype.removeCustomer = function() {
		var self = this;
		self.enableAddByBarcode(false);
		
		app.showMessage('<p>Are you sure you want to remove/change the customer for this transaction?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.removeCustomer(self.customerOrderPageModel.customerOrderId()).done(function(result) {
					self.customerCode('');
					self.refreshCustomerOrderDetailList();
					app.showMessage(result.message);
				});
			}
			self.enableAddByBarcode(true);
		})
	};
    
    CustomerOrderPage.prototype.search = function() {
    	var self = this;
    	self.enableAddByBarcode(false);
    	
    	customerOrderService.getCustomerOrder(self.customerOrderPageModel.customerOrderId()).done(function(data) { 
    		Search.show(data).done(function() {
    			self.currentPage(1);
        		self.refreshCustomerOrderDetailList();
        		self.barcodeFocus(true);
        		self.enableAddByBarcode(true);
        	});
    	});
    };
    
    CustomerOrderPage.prototype.updatePackageCount = function() {
    	var self = this;
    	self.enableAddByBarcode(false);
    	
    	customerOrderService.updatePackageCount(
    				self.customerOrderPageModel.customerOrderId(), 
    				self.customerOrderPageModel.cartonCount(),
    				self.customerOrderPageModel.plasticCount(),
    				self.customerOrderPageModel.bagCount()).done(function(result) { 
    		self.refreshCustomerOrderDetailList();
    		self.barcodeFocus(true);
    		self.enableAddByBarcode(true);
    		
    		if(!result.success) {
				app.showMessage(result.message);
			}
    	});
    };
    
    CustomerOrderPage.prototype.refreshCustomerOrderDetailList = function() {
    	var self = this;
    	
    	customerOrderService.getCustomerOrder(self.customerOrderPageModel.customerOrderId()).done(function(customerOrder) { 
    		if(customerOrder.customer) {
    			self.hasCustomer(true);
    			self.customerFormattedName(customerOrder.customer.customerCategory.code + ' ' + customerOrder.customer.code + ' - ' + customerOrder.customer.formattedName);
    		} else {
    			self.hasCustomer(false);
    		}
    		self.customerOrderPageModel.customerOrderNumber(customerOrder.orderNumber);
    		self.customerOrderPageModel.cartonCount(customerOrder.cartonCount);
    		self.customerOrderPageModel.plasticCount(customerOrder.plasticCount);
    		self.customerOrderPageModel.bagCount(customerOrder.bagCount);
    		self.customerOrderPageModel.formattedGrossAmount(customerOrder.formattedGrossAmount);
    		self.customerOrderPageModel.formattedOutrightPromoDiscount(customerOrder.formattedOutrightPromoDiscount);
    		self.customerOrderPageModel.formattedTotalAmount(customerOrder.formattedTotalAmount);
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
    	self.enableAddByBarcode(false);
    	
    	if(self.barcodeKey() === 'e') {
    		self.submit(false);
    	} else if(self.barcodeKey() === 'd') {
    		customerOrderService.refreshCustomerOrder(self.customerOrderPageModel.customerOrderId()).done(function() {
    			self.printCopy();
    		});
    	} else if(self.barcodeKey() === 's') {
    		self.search();
    		self.barcodeKey("");
    	} else if(self.barcodeKey() === 'n') {
    		self.submit(true);
    	}  else {
    		customerOrderService.addItemByBarcode(self.barcodeKey(), self.customerOrderPageModel.customerOrderId()).done(function(result) {	
        		if(result.success) {
        			self.currentPage(1);
        			self.refreshCustomerOrderDetailList();
        			self.barcodeKey("");
        			self.enableAddByBarcode(true);
        		} else {
        			soundUtil.beep();
        			InvalidBarcode.show(self.barcodeKey()).done(function() {
        				self.barcodeFocus(true);
        				self.barcodeKey("");
        				self.enableAddByBarcode(true);
        			});
        		}
        	});
    	}
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
    
    CustomerOrderPage.prototype.submit = function(createNew) {
    	var self = this;
    	
    	app.showMessage('<p>Submit order #<span class="text-primary">' + self.customerOrderPageModel.customerOrderNumber() + '</span>?<br>' +
				'Make sure to label all packages with <span class="text-danger">#' + self.customerOrderPageModel.customerOrderNumber() + '</span>.<br><br>' +
				'By clicking confirm, you will be forwarding the order to the cashier.</p>',
		'Submit Order',
		[{ text: 'Confirm', value: true }, { text: 'Cancel', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.submitCustomerOrder(self.customerOrderPageModel.customerOrderId()).done(function(result) {
					if(result.success) {
						if(createNew) {
							customerOrderService.createCustomerOrder().done(function(result) {
				            	if(result.success) {
				            		router.navigate('#customerorderpage/' + result.extras.customerOrderId);
				            	} else {
				            		app.showMessage(result.message);
				            	}
				            });
						} else {
							router.navigate('#customerorder');
						}
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
		
		app.showMessage('<p>Submit order <span class="text-primary">#' + self.customerOrderPageModel.customerOrderNumber() + '</span> with duplicate?',
				'Submit with Duplicate',
				[{ text: 'Confirm', value: true }, { text: 'Cancel', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.printCustomerOrderCopy(self.customerOrderPageModel.customerOrderId()).done(function(result) {
					if(result.success) {
						customerOrderService.submitCustomerOrder(self.customerOrderPageModel.customerOrderId()).done(function(result) {
							if(result.success) {
								customerOrderService.createCustomerOrder().done(function(result) {
					            	if(result.success) {
					            		router.navigate('#customerorderpage/' + result.extras.customerOrderId);
					            	} else {
					            		app.showMessage(result.message);
					            	}
					            });
							} else {
								app.showMessage(result.message).done(function() {
									self.barcodeFocus(true);
								});
							}
						});
					} else {
						app.showMessage(result.message);
					}
				});
			} else {
				self.barcodeFocus(true);
			}
		})
	};
    
    return CustomerOrderPage;
});