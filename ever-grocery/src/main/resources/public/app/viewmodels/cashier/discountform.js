define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice', 'modules/constantsservice'], 
		function (dialog, app, ko, customerOrderService, constantsService) {
	var DiscountForm = function(customerOrder) {
		this.customerOrder = customerOrder;
		
		this.discountTypeList = ko.observable();
		
		this.discountFormModel = {
			customerOrderId : ko.observable(),
			discountType : ko.observable(),
			grossAmountLimit : ko.observable(),
			
			discountIdNumber : ko.observable(),
			name : ko.observable(),
			address : ko.observable(),
			tin : ko.observable()
		};
		
		this.errors = {
			discountType : ko.observable(),
			grossAmountLimit : ko.observable(),
			
			discountIdNumber : ko.observable(),
			name : ko.observable(),
			address : ko.observable(),
			tin : ko.observable()
		};
		
		this.customerOrderNumber = null;
	};
	
	DiscountForm.prototype.activate = function() {
		var self = this;
		
		self.discountFormModel.customerOrderId(self.customerOrder.id);
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
		
		app.showMessage('Applying Discount : <strong>' + self.discountFormModel.discountType() + '</strong>' + ' to order#' + self.customerOrderNumber,
				'Confirm',
				[{ text: 'Confirm', value: true }, { text: 'Cancel', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.applyDiscount(ko.toJSON(self.discountFormModel)).done(function(result) {
					if(result.success) {
		        		dialog.close(self);
		        	} else if(result.extras && result.extras.errors) {
		        		self.errors.discountType(result.extras.errors.discountType);
		        		self.errors.grossAmountLimit(result.extras.errors.grossAmountLimit);
		        		self.errors.discountIdNumber(result.extras.errors.discountIdNumber);
		        		self.errors.name(result.extras.errors.name);
		        		self.errors.address(result.extras.errors.address);
		        		self.errors.tin(result.extras.errors.tin);
		        	}
					if(result.message) app.showMessage(result.message);
				});
			}
		})
		
	};
	
	DiscountForm.prototype.cancel = function() {
        dialog.close(this);
    };
	
	return DiscountForm
});