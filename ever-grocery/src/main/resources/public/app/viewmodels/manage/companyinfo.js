define(['knockout', 'modules/companyservice'], 
		function (ko, companyService) {
    var CompanyInfo = function() {
    	this.companyId = ko.observable();
    	
    	this.company = {
    		name: ko.observable(),
    		address: ko.observable(),
    		agent: ko.observable(),
    		phoneNumber: ko.observable(),
    		receiptType: ko.observable(),
    		formattedLastPurchaseOrderDate: ko.observable(),
    		formattedPurchaseValuePercentage: ko.observable(),
    		formattedSaleValuePercentage: ko.observable(),
    		formattedProfitPercentage: ko.observable()
	    };
    };
    
    CompanyInfo.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.companyId = activationData.companyId;
    	
    	companyService.getCompany(self.companyId()).done(function(company) {
    		self.company.name(company.name);
    		if(company.address) self.company.address(company.address);
    		else self.company.address('-');
    		if(company.agent) self.company.agent(company.agent);
    		else self.company.agent('-');
    		if(company.phoneNumber) self.company.phoneNumber(company.phoneNumber);
    		else self.company.phoneNumber('-');
    		self.company.receiptType(company.receiptType.displayName);
    		self.company.formattedLastPurchaseOrderDate(company.formattedLastPurchaseOrderDate);
    		self.company.formattedPurchaseValuePercentage(company.formattedPurchaseValuePercentage);
    		self.company.formattedSaleValuePercentage(company.formattedSaleValuePercentage);
    		self.company.formattedProfitPercentage(company.formattedProfitPercentage);
    	});
    };
    
    return CompanyInfo;
});