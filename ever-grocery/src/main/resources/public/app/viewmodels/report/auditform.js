define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/cashtransferservice', 'modules/userservice'], 
		function (dialog, app, ko, cashTransferService, userService) {
	var AuditForm = function() {
		this.lesserUserList = ko.observable();
		
		this.userId = ko.observable();
		this.fullAudit = ko.observable(false);
		
		this.auditResult = {
			formattedWithheldCash: ko.observable(),
			formattedCashTransferOut: ko.observable(),
			formattedCashTransferReceived: ko.observable(),
			formattedSales: ko.observable(),
			formattedLastAudit: ko.observable()
		};
	};
	
	AuditForm.prototype.activate = function() {
		var self = this;
		
		userService.getLesserUserListOrderByName().done(function(lesserUserList) {
    		self.lesserUserList(lesserUserList);
    	});
		
		self.auditResult.formattedWithheldCash('0.00');
		self.auditResult.formattedCashTransferOut('0.00');
		self.auditResult.formattedCashTransferReceived('0.00');
		self.auditResult.formattedSales('0.00');
		self.auditResult.formattedLastAudit('n/a');
	};
	
	AuditForm.show = function() {
    	return dialog.show(new AuditForm());
    };
	
	AuditForm.prototype.auditUser = function() {
		var self = this;
		
		cashTransferService.auditUser(self.userId(), self.fullAudit()).done(function(result) {
			if(result.success) {
				self.auditResult.formattedWithheldCash(result.extras.auditResult.formattedWithheldCash);
				self.auditResult.formattedCashTransferOut(result.extras.auditResult.formattedCashTransferOut);
				self.auditResult.formattedCashTransferReceived(result.extras.auditResult.formattedCashTransferReceived);
				self.auditResult.formattedSales(result.extras.auditResult.formattedSales);
				self.auditResult.formattedLastAudit(result.extras.auditResult.formattedLastAudit);
			} else if(result.message) {
				app.showMessage(result.message);
			} else {
			}
		});
	};
	
	AuditForm.prototype.cancel = function() {
        dialog.close(this);
    };
	
    return AuditForm;
});