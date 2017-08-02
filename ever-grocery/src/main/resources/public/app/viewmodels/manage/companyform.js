define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/companyservice'], function (dialog, app, ko, companyService) {
    var CompanyForm = function(preTitle, company) {
        this.preTitle = preTitle;
        this.company = company;
        
        this.receiptTypeList = ko.observable();
        
        this.companyFormModel = {
        	id: ko.observable(),
        	name: ko.observable(),
        	address: ko.observable(),
        	agent: ko.observable(),
        	phoneNumber: ko.observable(),
        	receiptType: ko.observable()
        };
    };
    
    CompanyForm.prototype.activate = function() {
    	var self = this;
    	
    	self.companyFormModel.id(self.company.id);
    	self.companyFormModel.name(self.company.name);
    	self.companyFormModel.address(self.company.address);
    	self.companyFormModel.agent(self.company.agent);
    	self.companyFormModel.phoneNumber(self.company.phoneNumber);
    	self.companyFormModel.receiptType(self.company.receiptType);
    	
    	companyService.getReceiptTypeList().done(function(receiptTypeList) {
    		self.receiptTypeList(receiptTypeList);
    		self.companyFormModel.receiptType(self.company.receiptType);
    	});
    };
 
    CompanyForm.show = function(preTitle, company) {
        return dialog.show(new CompanyForm(preTitle, company));
    };
 
    CompanyForm.prototype.save = function() {
    	var self = this;
    	
    	companyService.saveCompany(ko.toJSON(self.companyFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    CompanyForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return CompanyForm;
});