define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice'], 
		function (dialog, app, ko, customerOrderService) {
	var QuantityForm = function(productDetailId, productUnitType, customerOrderId, productName) {
		this.productDetailId = productDetailId;
		this.productUnitType = productUnitType;
		this.customerOrderId = customerOrderId;
		
		this.productName = productName;
		
		this.quantity = ko.observable();
		this.quantityFocus = ko.observable(true);
		
		this.enableButtons = ko.observable(true);
	};
	
	QuantityForm.prototype.activate = function() {
	};
	
	QuantityForm.show = function(productDetailId, productUnitType, customerOrderId, productName) {
		return dialog.show(new QuantityForm(productDetailId, productUnitType, customerOrderId, productName));
	};
	
	QuantityForm.prototype.add = function() {
		var self = this;
		self.enableButtons(false);
		
		customerOrderService.addItem(self.productDetailId, self.customerOrderId, self.quantity()).done(function (result) {
    		if(result.success) {
        		dialog.close(self);
        	}  else {
        		app.showMessage(result.message).done(function() {
					self.quantityFocus(true);
				});
        	}
    		self.enableButtons(true);
    	});
	};
	
	QuantityForm.prototype.cancel = function() {
        dialog.close(this);
    };
	
	return QuantityForm
});