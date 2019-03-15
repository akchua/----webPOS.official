define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice', 'modules/customerorderservice', 'viewmodels/customer-order/quantityform'], 
		function (dialog, app, ko, productService, customerOrderService, QuantityForm) {
    var SearchDetails = function(product, customerOrder) {
    	this.product = product;
    	this.customerOrder = customerOrder;
    	
    	this.productName = ko.observable();
    	
    	this.productDetailList = ko.observable();
    	
    	this.enableButtons = ko.observable(true);
    };
    
    SearchDetails.prototype.activate = function() {
    	var self = this;
    	
    	self.productName(self.product.name);
    	
    	productService.getProductDetailList(self.product.id, false).done(function (data) {
    		self.productDetailList(data);
    	});
    };
    
    SearchDetails.prototype.add = function(productDetailId, productUnitType) {
    	var self = this;
    	self.enableButtons(false);
    	
    	QuantityForm.show(productDetailId, productUnitType, self.customerOrder.id, self.productName()).then(function() {
    		self.enableButtons(true);
    		dialog.close(self);
    	});
    };
    
    SearchDetails.show = function(product, customerOrder) {
    	return dialog.show(new SearchDetails(product, customerOrder));
    };
    
    SearchDetails.prototype.cancel = function() {
    	dialog.close(this);
    };
    
    return SearchDetails;
});