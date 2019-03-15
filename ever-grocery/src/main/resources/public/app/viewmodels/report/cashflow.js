define(['durandal/app', 'knockout', 'modules/cashtransferservice', 'modules/userservice', 'modules/constantsservice', 'viewmodels/report/auditlog'/*, 'viewmodels/report/auditform'*/], 
		function (app, ko, cashTransferService, userService, constantsService, AuditLog/*, AuditForm*/) {
	var CashFlow = function() {
		this.cashTransferList = ko.observable();
		
		this.userList = ko.observable();
		this.statusList = ko.observable();
		
		this.userId = ko.observable();
		this.status = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
	};
	
	CashFlow.prototype.activate = function() {
		var self = this;
		
		userService.getUserListOrderByName().done(function(userList) {
    		self.userList(userList);
    	});
		
		constantsService.getCashTransferStatusList().done(function(statusList) {
			self.statusList(statusList);
		});
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCashTransferList();
		});
		
		self.refreshCashTransferList();
	};
	
	/*CashFlow.prototype.auditUser = function() {
		AuditForm.show();
	};*/
	
	CashFlow.prototype.auditLog = function() {
		var self = this;
		self.enableButtons(false);
		
		AuditLog.show().then(function() {
			self.enableButtons(true);
		});
	};
	
	CashFlow.prototype.refreshCashTransferList = function() {
		var self = this;
		
		cashTransferService.getCashTransferList(self.currentPage(), self.userId(), self.status()).done(function(data) {
			self.cashTransferList(data.list);
			self.totalItems(data.total);
		});
	};
	
    return CashFlow;
});