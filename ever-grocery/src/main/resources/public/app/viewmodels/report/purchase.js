define(['durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/companyservice', 'viewmodels/report/purchaseview'], function (app, ko, purchaseOrderService, companyService, PurchaseView) {
	var PurchaseOrder = function() {
		this.purchaseOrderList = ko.observable();
		this.companyList = ko.observable();
		
		this.showChecked = ko.observable(false);
		
		this.companyId = ko.observable();
		this.quickId = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
	};
	
	PurchaseOrder.prototype.activate = function() {
		var self = this;
		
		companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshPurchaseOrderList();
		});
		
		self.showChecked.subscribe(function() {
			self.refreshPurchaseOrderList();
		});
		
		self.refreshPurchaseOrderList();
	};
	
	PurchaseOrder.prototype.refreshPurchaseOrderList = function() {
		var self = this;
		
		purchaseOrderService.getPurchaseOrderList(self.currentPage(), self.companyId(), self.showChecked()).done(function(data) {
			self.purchaseOrderList(data.list);
			self.totalItems(data.total);
		});
	};
	
	PurchaseOrder.prototype.quickView = function() {
		var self = this;
		
		self.view(self.quickId());
		self.quickId('');
	};
	
	PurchaseOrder.prototype.view = function(purchaseOrderId) {
		var self = this;
		self.enableButtons(false)
		
		purchaseOrderService.getPurchaseOrder(purchaseOrderId).done(function(purchaseOrder) {
			if(purchaseOrder) {
				PurchaseView.show(purchaseOrder).done(function() {
					self.refreshPurchaseOrderList();
					self.enableButtons(true);
				});
			} else {
				self.enableButtons(true);
				app.showMessage('Purchase Order ID ' + purchaseOrderId + ' does not exist.');
			}
		});
	};

	PurchaseOrder.prototype.check = function(purchaseOrderId, companyName, formattedTotalAmount, formattedTerms) {
		var self = this;
		self.enableButtons(false);
		
		app.showMessage('<div class="container-fluid"><dl class="dl-horizontal"><dt>Purchase ID  :</dt><dd>' + purchaseOrderId + '</dd>' +
						'<dt>Company Name :</dt><dd>' + companyName + '</dd>' +
						'<dt>Total Amount :</dt><dd>Php ' + formattedTotalAmount + '</dd><br>' +
						'<dt>Check Date :</dt><dd>' + formattedTerms + '</dd></div>',
				'Confirm Check',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				purchaseOrderService.checkPurchaseOrder(purchaseOrderId).done(function(result) {
					self.refreshPurchaseOrderList();
					app.showMessage(result.message);
				});
			}
			self.enableButtons(true);
		})
	};
	
    return PurchaseOrder;
});