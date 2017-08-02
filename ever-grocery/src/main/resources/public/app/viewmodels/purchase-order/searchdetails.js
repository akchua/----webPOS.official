define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productService', 'modules/purchaseorderservice'], function (dialog, app, ko, productService, purchaseOrderService) {
    var SearchDetails = function(product, purchaseOrder) {
    	this.product = product;
    	this.purchaseOrder = purchaseOrder;
    	
    	this.productName = ko.observable();
    	this.quantity = ko.observable(1);		//setup for dynamic quantity add
    	
    	this.productDetailList = ko.observable();
    };
    
    SearchDetails.prototype.activate = function() {
    	var self = this;
    	
    	self.productName(self.product.name);
    	
    	productService.getProductDetailList(self.product.id).done(function (data) {
    		self.productDetailList(data);
    	});
    };
    
    SearchDetails.prototype.add = function(productDetailId) {
    	var self = this;
    	
    	purchaseOrderService.addItem(productDetailId, self.purchaseOrder.id, self.quantity()).done(function (result) {
    		if(result.success) {
        		dialog.close(self);
        	}  else {
        		app.showMessage(result.message);
        	}
    	});
    };
    
    SearchDetails.show = function(product, purchaseOrder) {
    	return dialog.show(new SearchDetails(product, purchaseOrder));
    };
    
    SearchDetails.prototype.cancel = function() {
    	dialog.close(this);
    };
    
    return SearchDetails;
});