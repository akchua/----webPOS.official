define(['knockout', 'modules/productservice'], function (ko, productService) {
    var ProductDetails = function(productDetails, enableConfig) {
    	this.productDetails = productDetails;

    	this.unitTypeList = ko.observable();
    	
    	this.enableConfig = enableConfig;
    	
    	this.formModel = {
    		id: ko.observable(),	
    		
    		title: ko.observable(),
    		
    		barcode: ko.observable(),
    		
    		quantity: ko.observable(),
    		
    		unitType: ko.observable(),
    		
    		grossPrice: ko.observable(),
    		
    		discount: ko.observable(),
    		
    		netPrice: ko.observable(),
    		
    		percentProfit: ko.observable(),
    		
    		sellingPrice: ko.observable(),
    		
    		netProfit: ko.observable(),
    		
    		content: ko.observable(),
    		
    		contentUnit: ko.observable()
    	};
    	
    	this.enableBarcode = null;
    	
        this.enableQuantity = null;
        
        this.enableUnitType = null;
        
        this.enableGrossPrice = null;
        
        this.enableDiscount = null;

        this.enableNetPrice = null;
        
        this.enablePercentProfit = null;
        
        this.enableSellingPrice = null;
        
        this.enableNetProfit = null;
    };
    
    ProductDetails.prototype.activate = function() {
    	var self = this;
    	
    	self.formModel.id(self.productDetails.id);
    	self.formModel.title(self.productDetails.title);
    	self.formModel.barcode(self.productDetails.barcode);
    	self.formModel.quantity(self.productDetails.quantity);
    	if(self.productDetails.unitType) self.formModel.unitType(self.productDetails.unitType.name);
    	self.formModel.grossPrice(self.productDetails.grossPrice);
    	self.formModel.discount(self.productDetails.discount);
    	self.formModel.netPrice(self.productDetails.netPrice);
    	self.formModel.percentProfit(self.productDetails.percentProfit);
    	self.formModel.sellingPrice(self.productDetails.sellingPrice);
    	self.formModel.netProfit(self.productDetails.netProfit);
    	self.formModel.content(self.productDetails.content);
    	if(self.productDetails.contentUnit) self.formModel.contentUnit(self.productDetails.contentUnit.name)
    	
    	productService.getUnitTypeList().done(function(unitTypeList) {
    		self.unitTypeList(unitTypeList);
    		if(self.productDetails.unitType) self.formModel.unitType(self.productDetails.unitType.name);
    		if(self.productDetails.contentUnit) self.formModel.contentUnit(self.productDetails.contentUnit.name);
    	});
    	
    	// Set to the passed observable to control enable
    	self.enableBarcode = self.enableConfig.enableBarcode;
    	self.enableQuantity = self.enableConfig.enableQuantity;
    	self.enableUnitType = self.enableConfig.enableUnitType;
    	self.enableGrossPrice = self.enableConfig.enableGrossPrice;
    	self.enableDiscount = self.enableConfig.enableDiscount;
    	self.enableNetPrice = self.enableConfig.enableNetPrice;
    	self.enablePercentProfit = self.enableConfig.enablePercentProfit;
    	self.enableSellingPrice = self.enableConfig.enableSellingPrice;
    	self.enableNetProfit = self.enableConfig.enableNetProfit;
    };
 
    return ProductDetails;
});