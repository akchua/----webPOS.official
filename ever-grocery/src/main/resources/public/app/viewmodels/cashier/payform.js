define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice', 'modules/utility', 'viewmodels/cashier/discountform'], 
		function (dialog, app, ko, customerOrderService, utility, DiscountForm) {
	var PayForm = function(customerOrderId) {
		this.customerOrderId = customerOrderId;
		
		this.thousand = ko.observable();
		this.formattedThousand = ko.observable();
		this.fiveHundred = ko.observable();
		this.formattedFiveHundred = ko.observable();
		this.hundred = ko.observable();
		this.formattedHundred = ko.observable();
		this.exact = ko.observable();
		this.formattedExact = ko.observable();
		
		this.formattedTotalAmount = ko.observable();
		this.cash = ko.observable();
		
		this.enableButtons = ko.observable(true);
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
		
		customerOrderService.getCustomerOrder(self.customerOrderId).done(function(customerOrder) {
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
		});
	};
	
	PayForm.prototype.pay = function(cash, formattedCash) {
		var self = this;
		self.enableButtons(false);
		
		app.showMessage('Confirm Received Amount : <strong>' + formattedCash + '</strong>',
				'Confirm',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.payCustomerOrder(self.customerOrderId, cash).done(function(result) {
					if(result.success) {
		        		dialog.close(self);
		        		customerOrderService.printReceipt(self.customerOrderId);
		        	} 
					self.enableButtons(true);
		        	app.showMessage(result.message);
				});
			}
		})
	};
	
	PayForm.prototype.discount = function() {
		var self = this;
		self.enableButtons(false);
		
		customerOrderService.getCustomerOrder(self.customerOrderId).done(function(customerOrder) {
			DiscountForm.show(customerOrder).then(function() {
				self.refreshCustomerOrder();
				self.enableButtons(true);
			});
		});
	};
	
	PayForm.prototype.payAmount = function() {
		var self = this;
		
		self.pay(self.cash(), self.cash().toLocaleString(
				undefined,
				{ minimumFractionDigits: 2 }
			));
	};
	
	PayForm.prototype.payThousand = function() {
		var self = this;
		
		self.pay(self.thousand(), self.formattedThousand());
	};
	
	PayForm.prototype.payFiveHundred = function() {
		var self = this;
		
		self.pay(self.fiveHundred(), self.formattedFiveHundred());
	};
	
	PayForm.prototype.payHundred = function() {
		var self = this;
		
		self.pay(self.hundred(), self.formattedHundred());
	};
	
	PayForm.prototype.payExact = function() {
		var self = this;
		
		self.pay(self.exact(), self.formattedExact());
	};
	
	PayForm.prototype.cancel = function() {
        dialog.close(this);
    };
	
	return PayForm
});