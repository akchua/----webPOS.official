define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice', 'modules/utility'], 
		function (dialog, app, ko, customerOrderService, utility) {
	var PayForm = function(customerOrder) {
		this.customerOrder = customerOrder;
		
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
	};
	
	PayForm.prototype.activate = function() {
		var self = this;
		
		self.thousand(utility.ceilToThousand(self.customerOrder.totalAmount));
		self.formattedThousand(self.thousand().toLocaleString(
				undefined,
				{ minimumFractionDigits: 2 }
			)
		);
		self.fiveHundred(utility.ceilToFiveHundred(self.customerOrder.totalAmount));
		self.formattedFiveHundred(self.fiveHundred().toLocaleString(
				undefined,
				{ minimumFractionDigits: 2 }
			)
		);
		self.hundred(utility.ceilToHundred(self.customerOrder.totalAmount));
		self.formattedHundred(self.hundred().toLocaleString(
				undefined,
				{ minimumFractionDigits: 2 }
			)
		);
		
		self.exact(self.customerOrder.totalAmount);
		self.formattedExact(self.exact().toLocaleString(
				undefined,
				{ minimumFractionDigits: 2 }
			)
		);
		
		self.formattedTotalAmount(self.customerOrder.formattedTotalAmount);
	};
	
	PayForm.show = function(customerOrder) {
		return dialog.show(new PayForm(customerOrder));
	};
	
	PayForm.prototype.pay = function(cash, formattedCash) {
		var self = this;
		
		app.showMessage('Confirm Received Amount : <strong>' + formattedCash + '</strong>',
				'Confirm',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.payCustomerOrder(self.customerOrder.id, cash).done(function(result) {
					if(result.success) {
		        		dialog.close(self);
		        		customerOrderService.printReceipt(self.customerOrder.id, cash);
		        	} 
		        	app.showMessage(result.message);
				});
			}
		})
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