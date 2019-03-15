define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/cashtransferservice', 'modules/userservice'], 
		function (dialog, app, ko, cashTransferService, userService) {
    var CashTransferForm = function() {
    	this.managerList = ko.observable();
    	
    	this.cashTransferFormModel = {
    		id: ko.observable(),
    		
    		cashToId: ko.observable(),
	    	amount: ko.observable()
	    };
    	
    	this.errors = {
			cashToId: ko.observable(),
	    	amount: ko.observable()
    	};
    	
    	this.enableButtons = ko.observable(true);
    };
    
    CashTransferForm.prototype.activate = function() {
    	var self = this;
    	
    	userService.getManagerList().done(function(managerList) {
    		self.managerList(managerList);
    	});
    };
    
    CashTransferForm.show = function() {
    	return dialog.show(new CashTransferForm());
    };
    
    CashTransferForm.prototype.request = function() {
    	var self = this;
    	self.enableButtons(false);
    	
        cashTransferService.requestCashTransfer(ko.toJSON(self.cashTransferFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);
        	} else {
        		if(result.extras) {
        			if(result.extras.errors) {
        				self.errors.cashToId(result.extras.errors.cashToId);
                		self.errors.amount(result.extras.errors.amount);
        			}
        		}
        	}
        	self.enableButtons(true);
        	if(result.message) app.showMessage(result.message);
        });
    };
    
    CashTransferForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return CashTransferForm;
});