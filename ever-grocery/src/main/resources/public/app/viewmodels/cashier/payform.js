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
		
		this.paymentsForm = {
			customerOrderId : ko.observable(customerOrderId),
			cash : ko.observable(0),
			
			checkAccountNumber : ko.observable(),
			checkNumber : ko.observable(),
			checkAmount : ko.observable(0),
			
			cardTransactionNumber : ko.observable(),
			cardAmount : ko.observable(0),
			
			refSIN : ko.observable(0)
		};
		
		this.errors = {
			cash : ko.observable(),
			checkAccountNumber : ko.observable(),
			checkNumber : ko.observable(),
			checkAmount : ko.observable()
		};
		
		this.discounted = ko.observable();
		this.enableButtons = ko.observable(true);
		this.enableQuickPay = ko.observable(false);
	};
	
	PayForm.prototype.activate = function() {
		var self = this;
		
		self.refreshCustomerOrder();
	};
	
	PayForm.show = function(customerOrderId) {
		return dialog.show(new PayForm(customerOrderId));
	};
	
	PayForm.prototype.refreshCustomerOrder = function() {
		var self = this;
		
		customerOrderService.getCustomerOrder(self.paymentsForm.customerOrderId()).done(function(customerOrder) {
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
		
		app.showMessage('Confirm Received Amount : <br>' + 
						'Cash : <strong>&#8369; ' + formattedCash + '</strong><br>' +
						'Check: <strong>&#8369; ' + formattedCheckAmount + '</strong><br>' ,
				'Confirm',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.payCustomerOrder(ko.toJSON(self.paymentsForm)).done(function(result) {
					if(result.success) {
		        		dialog.close(self);
		        		if(self.discounted()) {
		        			customerOrderService.printOriginalReceipt(self.paymentsForm.customerOrderId(), '-- Customer Copy --');
		        			customerOrderService.printOriginalReceipt(self.paymentsForm.customerOrderId(), '-- Accounting Copy --');
		        		} else {
		        			customerOrderService.printOriginalReceipt(self.paymentsForm.customerOrderId(), '');
		        		}
					} else if(result.extras && result.extras.errors) {
		        		self.errors.cash(result.extras.errors.cash);
		        		self.errors.checkAccountNumber(result.extras.errors.checkAccountNumber);
		        		self.errors.checkNumber(result.extras.errors.checkNumber);
		        		self.errors.checkAmount(result.extras.errors.checkAmount);
		        		self.errors.cardTransactionNumber(result.extras.errors.cardTransactionNumber);
		        		self.errors.cardAmount(result.extras.errors.cardAmount);
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