define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice', 'modules/utility', 'viewmodels/cashier/discountform'], 
		function (dialog, app, ko, customerOrderService, utility, DiscountForm) {
	var PayForm = function(customerOrderId) {
		this.title = ko.observable();
		
		this.thousand = ko.observable();
		this.formattedThousand = ko.observable();
		this.fiveHundred = ko.observable();
		this.formattedFiveHundred = ko.observable();
		this.hundred = ko.observable();
		this.formattedHundred = ko.observable();
		this.exact = ko.observable();
		this.formattedExact = ko.observable();
		
		this.formattedTotalAmount = ko.observable();
		this.orderNumber = ko.observable();
		
		this.paymentsForm = {
			customerOrderId : ko.observable(customerOrderId),
			cash : ko.observable(0),
			
			checkAccountNumber : ko.observable(),
			checkNumber : ko.observable(),
			checkAmount : ko.observable(0),
			
			cardTransactionNumber : ko.observable(),
			cardAmount : ko.observable(0),
			
			pointsAmount : ko.observable(0),
			
			refSIN : ko.observable(0)
		};
		
		this.cardId = ko.observable();
		this.hasCustomer = ko.observable(false);
		this.hasPoints = ko.observable(false);
		
		this.customer = {
			id : ko.observable(),
			
			formattedName : ko.observable(),
			formattedCardId : ko.observable(),
			availablePoints : ko.observable()
		};
		
		this.errors = {
			cash : ko.observable(),
			checkAccountNumber : ko.observable(),
			checkNumber : ko.observable(),
			checkAmount : ko.observable(),
			
			cardTransactionNumber : ko.observable(),
			cardAmount : ko.observable(),
			
			pointsAmount : ko.observable()
		};
		
		this.discounted = ko.observable();
		this.enableButtons = ko.observable(true);
		this.enableQuickPay = ko.observable(false);
		
		this.enablePrintReceipt = ko.observable(true);
	};
	
	PayForm.prototype.activate = function() {
		var self = this;
		
		self.refreshCustomerOrder();
	};
	
	PayForm.show = function(customerOrderId) {
		return dialog.show(new PayForm(customerOrderId));
	};
	
	PayForm.prototype.setCustomer = function() {
		var self = this;
		self.enableButtons(false);
		
		customerOrderService.setCustomer(self.paymentsForm.customerOrderId(), self.cardId()).done(function(result) {
			self.cardId('');
			if(result.success) {
				self.refreshCustomerOrder();
			} else {
				app.showMessage(result.message);
			}
			self.enableButtons(true);
		});
	};
	
	PayForm.prototype.removeCustomer = function() {
		var self = this;
		self.enableButtons(false);
		
		app.showMessage('<p>Are you sure you want to remove/change the customer for this transaction?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				self.paymentsForm.pointsAmount(0);
				customerOrderService.removeCustomer(self.paymentsForm.customerOrderId()).done(function(result) {
					self.refreshCustomerOrder();
					app.showMessage(result.message);
				});
			}
			self.enableButtons(true);
		})
	};
	
	PayForm.prototype.refreshCustomerOrder = function() {
		var self = this;
		
		customerOrderService.getCustomerOrder(self.paymentsForm.customerOrderId()).done(function(customerOrder) {
			self.orderNumber(customerOrder.orderNumber);
			
			self.thousand(utility.ceilToThousand(customerOrder.totalAmount));
			self.formattedThousand(self.thousand().toLocaleString(
					undefined,
					{ minimumFractionDigits: 2 }
				)
			);
			self.fiveHundred(utility.ceilToFiveHundred(customerOrder.totalAmount));
			self.formattedFiveHundred(self.fiveHundred().toLocaleString(
					undefined,
					{ minimumFractionDigits: 2 }
				)
			);
			self.hundred(utility.ceilToHundred(customerOrder.totalAmount));
			self.formattedHundred(self.hundred().toLocaleString(
					undefined,
					{ minimumFractionDigits: 2 }
				)
			);
			
			self.exact(customerOrder.totalAmount);
			self.formattedExact(self.exact().toLocaleString(
					undefined,
					{ minimumFractionDigits: 2 }
				)
			);
		
			self.formattedTotalAmount(customerOrder.formattedTotalAmount);
			
			self.discounted(customerOrder.status.name === 'DISCOUNTED')
			self.title(customerOrder.totalAmount > 0 ? 'Total' : 'Refund')
			
			if(customerOrder.totalAmount < 0) {
				self.enableQuickPay(false);
			} else {
				self.enableQuickPay(true);
			}
			
			if(customerOrder.customer) {
				self.hasCustomer(true);
				self.customer.formattedName(customerOrder.customer.formattedName);
				self.customer.formattedCardId(customerOrder.customer.formattedCardId);
				self.customer.availablePoints(customerOrder.customer.availablePoints);
				if(self.customer.availablePoints() > 0) self.hasPoints(true);
				else self.hasPoints(false);
			} else {
				self.hasCustomer(false);
				self.hasPoints(false);
				self.customer.formattedName('');
				self.customer.formattedCardId('');
				self.customer.availablePoints('');
			}
		});
	};
	
	PayForm.prototype.pay = function() {
		var self = this;
		self.enableButtons(false);
		
		var formattedCash = utility.roundToCent(self.paymentsForm.cash()).toLocaleString(
				undefined,
				{ minimumFractionDigits: 2 });
		var formattedCheckAmount = utility.roundToCent(self.paymentsForm.checkAmount()).toLocaleString(
				undefined,
				{ minimumFractionDigits: 2 });
		var formattedCardAmount = utility.roundToCent(self.paymentsForm.cardAmount()).toLocaleString(
				undefined,
				{ minimumFractionDigits: 2 });
		var formattedPointsAmount = utility.roundToCent(self.paymentsForm.pointsAmount()).toLocaleString(
				undefined,
				{ minimumFractionDigits: 2 });
		
		app.showMessage('Confirm Received Amount : <br>' + 
						'Cash   : <strong>&#8369; ' + formattedCash + '</strong><br>' +
						'Check  : <strong>&#8369; ' + formattedCheckAmount + '</strong><br>' +
						'Card   : <strong>&#8369; ' + formattedCardAmount + '</strong><br>' + 
						'Points : <strong>&#8369; ' + formattedPointsAmount + '</strong><br>',
				'Confirm',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.payCustomerOrder(ko.toJSON(self.paymentsForm)).done(function(result) {
					if(result.success) {
		        		dialog.close(self);
		        		if(self.enablePrintReceipt()) {
		        			if(self.discounted()) {
			        			customerOrderService.printOriginalReceipt(self.paymentsForm.customerOrderId(), '-- Customer Copy --');
			        			customerOrderService.printOriginalReceipt(self.paymentsForm.customerOrderId(), '-- Accounting Copy --');
			        		} else {
			        			customerOrderService.printOriginalReceipt(self.paymentsForm.customerOrderId(), '');
			        		}
		        		}
					} else if(result.extras && result.extras.errors) {
		        		self.errors.cash(result.extras.errors.cash);
		        		self.errors.checkAccountNumber(result.extras.errors.checkAccountNumber);
		        		self.errors.checkNumber(result.extras.errors.checkNumber);
		        		self.errors.checkAmount(result.extras.errors.checkAmount);
		        		self.errors.cardTransactionNumber(result.extras.errors.cardTransactionNumber);
		        		self.errors.cardAmount(result.extras.errors.cardAmount);
		        		self.errors.pointsAmount(result.extras.errors.pointsAmount);
		        	} else {
		        		self.errors.cash('');
		        		self.errors.checkAccountNumber('');
		        		self.errors.checkNumber('');
		        		self.errors.checkAmount('');
		        		self.errors.cardTransactionNumber('');
		        		self.errors.cardAmount('');
		        		self.errors.pointsAmount('');
		        	}
					if(result.message) app.showMessage(result.message);
				});
			}
			self.enableButtons(true);
		})
	};
	
	PayForm.prototype.discount = function() {
		var self = this;
		self.enableButtons(false);
		
		customerOrderService.getCustomerOrder(self.paymentsForm.customerOrderId()).done(function(customerOrder) {
			DiscountForm.show(customerOrder).then(function() {
				self.refreshCustomerOrder();
				self.enableButtons(true);
			});
		});
	};
	
	PayForm.prototype.payThousand = function() {
		var self = this;
		
		self.paymentsForm.cash(self.thousand());
		self.pay();
	};
	
	PayForm.prototype.payFiveHundred = function() {
		var self = this;
		
		self.paymentsForm.cash(self.fiveHundred());
		self.pay();
	};
	
	PayForm.prototype.payHundred = function() {
		var self = this;
		
		self.paymentsForm.cash(self.hundred());
		self.pay();
	};
	
	PayForm.prototype.payExact = function() {
		var self = this;
		
		self.paymentsForm.cash(self.exact());
		self.pay();
	};
	
	PayForm.prototype.cancel = function() {
        dialog.close(this);
    };
	
	return PayForm;
});