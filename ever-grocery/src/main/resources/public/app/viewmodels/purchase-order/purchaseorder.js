define(['plugins/router', 'durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/companyservice', 'viewmodels/purchase-order/purchaseorderform', 'viewmodels/purchase-order/purchaseorderpage'], 
		function (router, app, ko, purchaseOrderService, companyService, PurchaseOrderForm, PurchaseOrderPage) {
	var PurchaseOrder = function() {
		this.purchaseOrderList = ko.observable();
		this.companyList = ko.observable();
		
		this.companyId = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
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
		
		self.refreshPurchaseOrderList();
	};
	
	PurchaseOrder.prototype.refreshPurchaseOrderList = function() {
		var self = this;
		
		purchaseOrderService.getPurchaseOrderList(self.currentPage(), self.companyId(), false).done(function(data) {
			self.purchaseOrderList(data.list);
			self.totalItems(data.total);
		});
	};
	
	PurchaseOrder.prototype.create = function() {
		var self = this;
		
		PurchaseOrderForm.show('Create', new Object()).then(function() {
			self.refreshPurchaseOrderList();
		});
	};
	
	PurchaseOrder.prototype.remove = function(purchaseOrderId) {
		var self = this;
		
		app.showMessage('Are you sure you want to cancel Purchase Order "' + purchaseOrderId + '"?',
				'Confirm Remove',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				purchaseOrderService.removePurchaseOrder(purchaseOrderId).done(function(result) {
					self.refreshPurchaseOrderList();
					app.showMessage(result.message);
				});
			}
		})
	};
	
	PurchaseOrder.prototype.details = function(purchaseOrderId) {
		var self = this;
		
		router.navigate('#purchaseorderpage/' + purchaseOrderId);
	};
	
    return PurchaseOrder;
});