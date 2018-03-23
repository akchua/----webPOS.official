define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/companyservice', 'modules/fileservice'], 
		function (dialog, app, ko, purchaseOrderService, companyService, fileService) {
    var PurchaseOrderGenerateForm = function() {
        this.companyId = ko.observable();
        
        this.companyList = ko.observable();
        
        this.enableGenerateButton = ko.observable(true);
    };
 
    PurchaseOrderGenerateForm.prototype.activate = function() {
    	var self = this;
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
    };
    
    PurchaseOrderGenerateForm.show = function() {
        return dialog.show(new PurchaseOrderGenerateForm());
    };
    
    PurchaseOrderGenerateForm.prototype.generate = function() {
    	var self = this;
    	
    	self.enableGenerateButton(false);
        purchaseOrderService.generatePurchaseOrder(self.companyId()).done(function(result) {
        	if(result.success) {
				fileService.downloadGeneratedPurchaseByFileName(result.extras.fileName);
    			dialog.close(self);
    		} else {
    			app.showMessage(result.message);
    		}
        	self.enableGenerateButton(true);
        });
    };
    
    PurchaseOrderGenerateForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return PurchaseOrderGenerateForm;
});