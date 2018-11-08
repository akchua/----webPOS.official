define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/inventoryservice', 'modules/companyservice', 'modules/fileservice'], 
		function (dialog, app, ko, inventoryService, companyService, fileService) {
    var InventoryForm = function() {
        this.companyId = ko.observable();

        this.companyList = ko.observable();
        
        this.enableGenerateButton = ko.observable(true);
    };
 
    InventoryForm.prototype.activate = function() {
    	var self = this;
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
    };
    
    InventoryForm.show = function() {
        return dialog.show(new InventoryForm());
    };
    
    InventoryForm.prototype.generate = function() {
    	var self = this;
    	
    	self.enableGenerateButton(false);
    	inventoryService.generateInventory(self.companyId()).done(function(result) {
        	if(result.success) {
				fileService.downloadInventoryByFileName(result.extras.fileName);
    			dialog.close(self);
    		} else {
    			app.showMessage(result.message);
    		}
        	self.enableGenerateButton(true);
        });
    };
    
    InventoryForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return InventoryForm;
});