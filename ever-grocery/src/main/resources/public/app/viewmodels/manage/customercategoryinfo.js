define(['knockout', 'modules/customerCategoryservice'], 
		function (ko, customerCategoryService) {
    var CustomerCategoryInfo = function() {
    	this.customerCategoryId = ko.observable();
    	
    	this.customerCategory = {
    		name: ko.observable(),
    		
    		formattedSaleValuePercentage: ko.observable(),
    		formattedProfitPercentage: ko.observable()
	    };
    };
    
    CustomerCategoryInfo.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.customerCategoryId = activationData.customerCategoryId;
    	
    	customerCategoryService.getCustomerCategory(self.customerCategoryId()).done(function(customerCategory) {
    		self.customerCategory.name(customerCategory.name);
    		
    		self.customerCategory.formattedSaleValuePercentage(customerCategory.formattedSaleValuePercentage);
    		self.customerCategory.formattedProfitPercentage(customerCategory.formattedProfitPercentage);
    	});
    };
    
    return CustomerCategoryInfo;
});