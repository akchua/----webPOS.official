define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice', 'modules/purchaseorderservice', 'viewmodels/purchase-order/quantityform'], 
		function (dialog, app, ko, productService, purchaseOrderService, QuantityForm) {
    var SearchDetails = function(product, purchaseOrder) {
    	this.product = product;
    	this.purchaseOrder = purchaseOrder;
    	
    	this.productName = ko.observable();
    	
    	this.productDetailList = ko.observable();
    };
    
    SearchDetails.prototype.activate = function() {
    	var self = this;
    	
    	self.productName(self.product.name);
    	
    	productService.getProductDetailList(self.product.id, false).done(function (data) {
    		self.productDetailList(data);
    	});
    };
    
    SearchDetails.show = function(product, purchaseOrder) {
    	return dialog.show(new SearchDetails(product, purchaseOrder));
    };
    
    SearchDetails.prototype.add = function(productDetailId, productUnitType) {
    	var self = this;
    	
    	QuantityForm.show(productDetailId, productUnitType, self.purchaseOrder.id, self.productName()).then(function() {
    		dialog.close(self);
    	});
    };
    
    SearchDetails.prototype.cancel = function() {
    	dialog.close(this);
    };
    
    return SearchDetails;
});