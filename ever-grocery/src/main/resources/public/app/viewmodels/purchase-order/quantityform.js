define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderservice'], 
		function (dialog, app, ko, purchaseOrderService) {
	var QuantityForm = function(productDetailId, productUnitType, purchaseOrderId, productName) {
		this.productDetailId = productDetailId;
		this.productUnitType = productUnitType;
		this.purchaseOrderId = purchaseOrderId;
		
		this.productName = productName;
		
		this.quantity = ko.observable();
		this.quantityFocus = ko.observable(true);
		
		this.enableButtons = ko.observable(true);
	};
	
	QuantityForm.prototype.activate = function() {
	};
	
	QuantityForm.show = function(productDetailId, productUnitType, purchaseOrderId, productName) {
		return dialog.show(new QuantityForm(productDetailId, productUnitType, purchaseOrderId, productName));
	};
	
	QuantityForm.prototype.add = function() {
		var self = this;
		self.enableButtons(false);
		
		purchaseOrderService.addItem(self.productDetailId, self.purchaseOrderId, self.quantity()).done(function (result) {
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