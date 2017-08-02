define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice'], function (dialog, app, ko, customerOrderService) {
	var PayForm = function(customerOrder) {
		this.customerOrder = customerOrder;
		
		this.totalAmount = ko.observable();
		this.cash = ko.observable();
	};
	
	PayForm.prototype.activate = function() {
		var self = this;
		
		self.totalAmount(self.customerOrder.totalAmount);
	};
	
	PayForm.show = function(customerOrder) {
		return dialog.show(new PayForm(customerOrder));
	};
	
	PayForm.prototype.pay = function() {
		var self = this;
		
		customerOrderService.payCustomerOrder(self.customerOrder.id, self.cash()).done(function(result) {
			if(result.success) {
        		dialog.close(self);
        		customerOrderService.printReceipt(self.customerOrder.id, self.cash());
        	} 
        	app.showMessage(result.message);
		});
	};
	
	PayForm.prototype.cancel = function() {
        dialog.close(this);
    };
	
	return PayForm
});