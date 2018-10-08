define(['plugins/router', 'plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/companyservice'], 
		function (router, dialog, app, ko, purchaseOrderService, companyService) {
    var PurchaseOrderForm = function(preTitle, purchaseOrder) {
        this.preTitle = preTitle;
        this.purchaseOrder = purchaseOrder;
        
        this.purchaseOrderFormModel = {
        	id: ko.observable(),
        	
        	companyId: ko.observable(),
        	deliveredOn: ko.observable()
        }
        
        this.errors = {
        	companyId: ko.observable(),
        	deliveredOn: ko.observable()
    	};
        
        this.companyList = ko.observable();
    };
 
    PurchaseOrderForm.prototype.activate = function() {
    	var self = this;
    	
    	self.purchaseOrderFormModel.id(self.purchaseOrder.id);
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    		self.purchaseOrderFormModel.companyId(self.purchaseOrder.company.id);
    	});
    };
    
    PurchaseOrderForm.show = function(preTitle, purchaseOrder) {
        return dialog.show(new PurchaseOrderForm(preTitle, purchaseOrder));
    };
    
    PurchaseOrderForm.prototype.save = function() {
    	var self = this;
    	
        purchaseOrderService.savePurchaseOrder(ko.toJSON(self.purchaseOrderFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);
        		router.navigate('#purchaseorderpage/' + result.extras.purchaseOrderId);
        	} else {
        		self.errors.companyId(result.extras.errors.companyId);
        		self.errors.deliveredOn(result.extras.errors.deliveredOn);
        	}
        	
        	if(result.message) app.showMessage(result.message);
        });
    };
    
    PurchaseOrderForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return PurchaseOrderForm;
});