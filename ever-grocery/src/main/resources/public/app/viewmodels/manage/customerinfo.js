define(['knockout', 'modules/customerservice'], 
		function (ko, customerService) {
    var CustomerInfo = function() {
    	this.customerId = ko.observable();
    	
    	this.customer = {
    		customerCategoryName: ko.observable(),
    		code: ko.observable(),
    		name: ko.observable(),
    		storeName: ko.observable(),
    		address: ko.observable(),
    		contactNumber: ko.observable(),
    		cardId: ko.observable(),
    		formattedLastPurchase: ko.observable(),
    		
    		formattedTotalPoints : ko.observable(),
    		formattedUsedPoints : ko.observable(),
    		formattedAvailablePoints : ko.observable(),
    		
    		formattedSaleValuePercentage: ko.observable(),
    		formattedProfitPercentage: ko.observable()
	    };
    };
    
    CustomerInfo.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.customerId = activationData.customerId;
    	
    	customerService.getCustomer(self.customerId()).done(function(customer) {
    		self.customer.customerCategoryName(customer.customerCategory.name);
    		self.customer.code(customer.code);
    		self.customer.name(customer.name);
    		if(customer.storeName) self.customer.storeName(customer.storeName);
    		else self.customer.storeName('-');
    		self.customer.address(customer.address);
    		if(customer.contactNumber) self.customer.contactNumber(customer.contactNumber);
    		else self.customer.contactNumber('-');
    		if(customer.cardId) self.customer.cardId(customer.cardId);
    		else self.customer.cardId('-');
    		self.customer.formattedLastPurchase(customer.formattedLastPurchase);
    		
    		self.customer.formattedTotalPoints(customer.formattedTotalPoints);
    		self.customer.formattedUsedPoints(customer.formattedUsedPoints);
    		self.customer.formattedAvailablePoints(customer.formattedAvailablePoints);
    		
    		self.customer.formattedSaleValuePercentage(customer.formattedSaleValuePercentage);
    		self.customer.formattedProfitPercentage(customer.formattedProfitPercentage);
    	});
    };
    
    return CustomerInfo;
});