define(['plugins/dialog','knockout', 'durandal/app', 'modules/productService'], function (dialog, ko, app, productService) {
	var More = function(product) {
		this.product = product;
		
		this.productDetailList = ko.observable();
	};
	
	More.prototype.activate = function() {
		var self = this;
		
		productService.getProductDetailList(self.product.id).done(function (data) {
    		self.productDetailList(data);
    	});
	};
	
	More.show = function(product) {
		return dialog.show(new More(product));
	};
	
	More.prototype.cancel = function() {
		dialog.close(this);
	};
	
	return More;
});