define(['durandal/app', 'knockout', 'modules/cashtransferservice', 'viewmodels/cashtransfer/acceptform', 'viewmodels/cashtransfer/cashtransferform'], 
		function (app, ko, cashTransferService, AcceptForm, CashTransferForm) {
	var CashTransfer = function() {
		this.myCashTransfers = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
	};
	
	CashTransfer.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshMyCashTransfers();
		});
		
		self.refreshMyCashTransfers();
	};
	
	CashTransfer.prototype.refreshMyCashTransfers = function() {
		var self = this;
		
		cashTransferService.getMyCashTransfer(self.currentPage()).done(function(data) {
			self.myCashTransfers(data.list);
			self.totalItems(data.total);
		});
	};
	
	CashTransfer.prototype.request = function() {
		var self = this;
		self.enableButtons(false);
		
		CashTransferForm.show().then(function() {
			self.refreshMyCashTransfers();
			self.enableButtons(true);
		});
	};
	
	CashTransfer.prototype.accept = function(cashTransferId) {
		var self = this;
		self.enableButtons(false);
		
		AcceptForm.show(cashTransferId).then(function() {
			self.refreshMyCashTransfers();
			self.enableButtons(true);
		});
	};
	
	CashTransfer.prototype.cancel = function(cashTransferId, cashToFormattedName, formattedAmount) {
		var self = this;
		self.enableButtons(false);
		
		app.showMessage('Are you sure you want to cancel transfer of Php ' + formattedAmount + ' to ' + cashToFormattedName + '?',
				'Confirm Cancel',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				cashTransferService.cancelCashTransfer(cashTransferId).done(function(result) {
					self.refreshMyCashTransfers();
					app.showMessage(result.message);
				});
			}
			self.enableButtons(true);
		})
	};
	
	CashTransfer.prototype.decline = function(cashTransferId, cashFromFormattedName, formattedAmount) {
		var self = this;
		self.enableButtons(false);
		
		app.showMessage('Are you sure you want to decline transfer of Php ' + formattedAmount + ' from ' + cashFromFormattedName + '?',
				'Confirm Decline',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				cashTransferService.cancelCashTransfer(cashTransferId).done(function(result) {
					self.refreshMyCashTransfers();
					app.showMessage(result.message);
				});
			}
			self.enableButtons(true);
		})
	};
	
    return CashTransfer;
});