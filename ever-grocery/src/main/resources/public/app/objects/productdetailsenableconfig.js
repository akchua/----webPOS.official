define(['knockout'], function (ko) {
    var ProductDetailsEnableConfig = function(enableBarcode, enableQuantity, enableUnitType, enableGrossPrice, enableDiscount, enableNetPrice, enablePercentProfit, enableSellingPrice, enableNetProfit, enableStorageStockCount, enableStoreStockCount) {
        this.enableBarcode = ko.observable(enableBarcode);
    	
    	this.enableQuantity = ko.observable(enableQuantity);
    	
    	this.enableUnitType = ko.observable(enableUnitType);
        
        this.enableGrossPrice = ko.observable(enableGrossPrice);
        
        this.enableDiscount = ko.observable(enableDiscount);

        this.enableNetPrice = ko.observable(enableNetPrice);
        
        this.enablePercentProfit = ko.observable(enablePercentProfit);
        
        this.enableSellingPrice = ko.observable(enableSellingPrice);
        
        this.enableNetProfit = ko.observable(enableNetProfit);
        
        this.enableStorageStockCount = ko.observable(enableStorageStockCount);
        
        this.enableStoreStockCount = ko.observable(enableStoreStockCount);
    };
    
    /*ProductDetailsEnableConfig.prototype.update = function(enableQuantity, enableGrossPrice, enableDiscount, enableNetPrice, enablePercentProfit, enableSellingPrice, enableNetProfit) {
    	var self = this;
    	
    	self.enableQuantity(enableQuantity);
    	self.enableGrossPrice(enableGrossPrice);
    	self.enableDiscount(enableDiscount);
    	self.enableNetPrice(enableNetPrice);
    	self.enablePercentProfit(enablePercentProfit);
    	self.enableSellingPrice(enableSellingPrice);
    	self.enableNetProfit(enableNetProfit);
    };
    
    ProductDetailsEnableConfig.prototype.updateAll = function(enableDisable) {
    	var self = this;
    	
    	self.enableQuantity(enableDisable);
    	self.enableGrossPrice(enableDisable);
    	self.enableDiscount(enableDisable);
    	self.enableNetPrice(enableDisable);
    	self.enablePercentProfit(enableDisable);
    	self.enableSellingPrice(enableDisable);
    	self.enableNetProfit(enableDisable);
    };
    
    ProductDetailsEnableConfig.prototype.updateAllExQuantity = function(enableDisable) {
    	var self = this;
    	
    	self.update(self.enableQuantity(), enableDisable, enableDisable, enableDisable, enableDisable, enableDisable, enableDisable);
    };*/
    
    return ProductDetailsEnableConfig;
});