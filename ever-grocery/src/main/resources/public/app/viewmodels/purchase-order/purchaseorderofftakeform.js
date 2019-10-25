define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/companyservice', 'modules/fileservice'], 
		function (dialog, app, ko, purchaseOrderService, companyService, fileService) {
    var PurchaseOrderOfftakeForm = function() {
        this.companyId = ko.observable();
        this.offtakeDays = ko.observable(0);
        this.print = ko.observable(true);
        this.download = ko.observable(false);
        
        this.companyList = ko.observable();
        
        this.enableGenerateButton = ko.observable(true);
    };
 
    PurchaseOrderOfftakeForm.prototype.activate = function() {
    	var self = this;
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
    };
    
    PurchaseOrderOfftakeForm.show = function() {
        return dialog.show(new PurchaseOrderOfftakeForm());
    };
    
    PurchaseOrderOfftakeForm.prototype.generate = function() {
    	var self = this;
    	
    	self.enableGenerateButton(false);
        purchaseOrderService.generateOfftake(self.companyId(), self.offtakeDays(), self.download(), self.print()).done(function(result) {
        	if(result.success) {
				if(self.download()) {
					fileService.downloadGeneratedOfftakeByFileName(result.extras.fileName);
				}
    			dialog.close(self);
    		} else {
    			app.showMessage(result.message);
    		}
        	self.enableGenerateButton(true);
        });
    };
    
    PurchaseOrderOfftakeForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return PurchaseOrderOfftakeForm;
});