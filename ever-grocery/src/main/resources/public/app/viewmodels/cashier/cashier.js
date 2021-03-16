define(['durandal/app', 'knockout', 'modules/securityservice', 'modules/customerorderservice', 'modules/inventoryservice', 'modules/fileservice', 'viewmodels/cashier/payform', 'viewmodels/report/saleview'], 
		function (app, ko, securityService, customerOrderService, inventoryService, fileService, PayForm, SaleView) {
	var Cashier = function() {
		this.customerOrderList = ko.observable();
		
		this.searchKey = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.allowedToDeleteOrder = ko.observable(app.user.userType.authority < 3);
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
	
	Cashier.prototype.endOfShift = function() {
		var self = this;
		
		app.showMessage('Confirm End of Shift',
				'Confirm EOS',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.endOfShift().done(function() {
					securityService.logout().done(function() {
			    		location.href = '/';
			    	});
				});
			}
		})
		
	};
	
	Cashier.prototype.generateCashierInventory = function() {
    	var self = this;
    	self.enableButtons(false);
    	
    	app.showMessage('<p>Confirm generate cashier Inventory?',
				'Generate Cashier Inventory',
		[{ text: 'Confirm', value: true }, { text: 'Cancel', value: false }])
		.then(function(confirm) {
			if(confirm) {
		    	inventoryService.generateCashierInventory().done(function(result) {
		        	if(result.success) {
						fileService.downloadInventoryByFileName(result.extras.fileName);
		    		} else {
		    			app.showMessage(result.message);
		    		}
		        	self.enableButtons(true);
		        });
			} else {
				self.enableButtons(true);
			}
		})
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
	
	Cashier.prototype.returnToServer = function(customerOrderId, customerOrderNumber, creatorName) {
    	var self = this;
    	
    	app.showMessage('<p>Return order #<span class="text-primary">' + customerOrderNumber + '</span>?<br><br>' +
				'Make sure to ask the customer to return to <br>Mr./Ms. <span class="text-primary">' + creatorName + '</span> for adjustments or clarifications.' + '</span><br><br>' +
				'By clicking confirm, you will be returning the order to<br><span class="text-primary">Mr./Ms. ' + creatorName + '</span></p>',
				'Return Order',
		[{ text: 'Confirm', value: true }, { text: 'Cancel', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.returnCustomerOrder(customerOrderId).done(function(result) {
					self.refreshCustomerOrderList();
					app.showMessage(result.message);
				});
			}
		})
    };
	
	Cashier.prototype.remove = function(customerOrderId, customerOrderNumber) {
		var self = this;
		
		app.showMessage('Are you sure you want to cancel Customer Order #' + customerOrderNumber + '?',
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
	
	Cashier.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshCustomerOrderList();
	};
	
    return Cashier;
});