define(['knockout', 'modules/productservice'], 
		function (ko, productService) {
    var ProductInfo = function() {
    	this.productId = ko.observable();
    	
    	this.product = {
    		name : ko.observable(),
    		displayName : ko.observable(),
    		code : ko.observable(),
    		
    		category : ko.observable(),
    		company : ko.observable(),
    		
    		taxType : ko.observable(),
    		allowSeniorDiscount : ko.observable(),
    		allowPWDDiscount : ko.observable(),
    		
    		formattedOverallPurchaseValuePercentage: ko.observable(),
    		formattedOverallSaleValuePercentage: ko.observable(),
    		formattedOverallProfitPercentage: ko.observable()
	    };
    };
    
    ProductInfo.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.productId = activationData.productId;
    	
    	productService.getProduct(self.productId()).done(function(product) {
    		self.product.name(product.name);
    		if(product.displayName) self.product.displayName(product.displayName);
    		else self.product.displayName('-');
    		if(product.code) self.product.code(product.code);
    		else self.product.code('-');
    		
    		self.product.category(product.category.name);
    		self.product.company(product.company.name);
    		
    		self.product.taxType(product.taxType.displayName);
    		self.product.allowSeniorDiscount(product.allowSeniorDiscount ? "YES" : "NO");
    		self.product.allowPWDDiscount(product.allowPWDDiscount ? "YES" : "NO");
    		
    		self.product.formattedOverallPurchaseValuePercentage(product.formattedOverallPurchaseValuePercentage + ' (' + product.formattedPurchaseValuePercentage + ' of ' + product.company.formattedPurchaseValuePercentage + ')');
    		self.product.formattedOverallSaleValuePercentage(product.formattedOverallSaleValuePercentage + ' (' + product.formattedSaleValuePercentage + ' of ' + product.company.formattedSaleValuePercentage + ')');
    		self.product.formattedOverallProfitPercentage(product.formattedOverallProfitPercentage + ' (' + product.formattedProfitPercentage + ' of ' + product.company.formattedProfitPercentage + ')');
    	});
    };
    
    return ProductInfo;
});