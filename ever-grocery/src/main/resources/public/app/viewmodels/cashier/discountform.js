define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice', 'modules/constantsservice'], 
		function (dialog, app, ko, customerOrderService, constantsService) {
	var DiscountForm = function(customerOrder) {
		this.customerOrder = customerOrder;
		
		this.discountTypeList = ko.observable();
		
		this.discountType = ko.observable();
		this.grossAmountLimit = ko.observable();
		
		this.customerOrderId = null;
		this.customerOrderNumber = null;
	};
	
	DiscountForm.prototype.activate = function() {
		var self = this;
		
		self.customerOrderId = self.customerOrder.id;
		self.customerOrderNumber = self.customerOrder.orderNumber;
		
		constantsService.getDiscountTypeList().done(function(discountTypeList) {
			self.discountTypeList(discountTypeList);
		});
	};
	
	DiscountForm.show = function(customerOrder) {
		return dialog.show(new DiscountForm(customerOrder));
	};
	
	DiscountForm.prototype.apply = function() {
		var self = this;
		
		app.showMessage('Applying Discount : <strong>' + self.discountType() + '</strong>' + 'to order#' + self.customerOrderNumber,
				'Confirm',
				[{ text: 'Confirm', value: true }, { text: 'Cancel', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.applyDiscount(self.customerOrderId, self.discountType(), self.grossAmountLimit()).done(function(result) {
					if(result.success) {
		        		dialog.close(self);
		        	} else {
		        		app.showMessage(result.message);
		        	}
				});
			}
		})
		
	};
	
	DiscountForm.prototype.cancel = function() {
        dialog.close(this);
    };
	
	return DiscountForm
});