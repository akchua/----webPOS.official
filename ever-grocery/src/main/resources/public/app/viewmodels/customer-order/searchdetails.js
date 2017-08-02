define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productService', 'modules/customerorderservice'], function (dialog, app, ko, productService, customerOrderService) {
    var SearchDetails = function(product, customerOrder) {
    	this.product = product;
    	this.customerOrder = customerOrder;
    	
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
    	
    	customerOrderService.addItem(productDetailId, self.customerOrder.id, self.quantity()).done(function (result) {
    		if(result.success) {
        		dialog.close(self);
        	}  else {
        		app.showMessage(result.message);
        	}
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