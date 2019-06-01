define(['plugins/dialog', 'knockout'], 
			function (dialog, ko) {
    var ProductView = function(product) {
    	this.product = product;
    	
    	this.productId = ko.observable();
    	this.productName = ko.observable();
    };
    
    ProductView.prototype.activate = function() {
    	var self = this;
    	
    	self.productId(self.product.id);
    	self.productName(self.product.name);
    };
    
    ProductView.show = function(product) {
    	return dialog.show(new ProductView(product));
    };
    
    ProductView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ProductView;
});