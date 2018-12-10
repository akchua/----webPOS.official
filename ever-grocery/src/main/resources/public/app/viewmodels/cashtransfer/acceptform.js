define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/cashtransferservice'],
		function (dialog, app, ko, cashTransferService) {
    var AcceptForm = function(cashTransferId) {
    	this.cashTransferId = cashTransferId;
    	
    	this.formattedCashFromName = ko.observable();
    	this.formattedAmount = ko.observable();
    	
    	this.auth = ko.observable();
    };
    
    AcceptForm.prototype.activate = function() {
    	var self = this;
    	
    	cashTransferService.getCashTransfer(self.cashTransferId).done(function(cashTransfer) {
    		self.formattedCashFromName(cashTransfer.cashFrom.formattedName);
    		self.formattedAmount(cashTransfer.formattedAmount);
    	});
    };
    
    AcceptForm.show = function(cashTransferId) {
    	return dialog.show(new AcceptForm(cashTransferId));
    };
    
    AcceptForm.prototype.accept = function() {
    	var self = this;
    	
    	cashTransferService.acceptCashTransfer(self.cashTransferId, self.auth()).done(function(result) {
    		if(result.success) {
        		dialog.close(self);
        	} else {
        		self.auth('');
        	}
        	app.showMessage(result.message);
    	});
    };
    
    AcceptForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return AcceptForm;
});