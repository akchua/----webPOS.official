define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderservice'], function (dialog, app, ko, purchaseOrderService) {
    var PurchaseView = function(purchaseOrder) {
    	this.purchaseOrder = purchaseOrder;
    	this.purchaseOrderDetailList = ko.observable();
    	
    	this.beforeVatAndDiscount = ko.observable(false);
    	this.beforeVatAfterDiscount = ko.observable(false);
    	this.afterVatBeforeDiscount = ko.observable(false);
    	this.isListing = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.purchaseOrderViewModel = {
			purchaseOrderId: ko.observable(),
			totalAmount: ko.observable(),
			companyName: ko.observable(),
			receiptType: ko.observable(),
			status: ko.observable()
		};
    };
    
    PurchaseView.prototype.activate = function() {
    	var self = this;
    	
    	self.purchaseOrderViewModel.purchaseOrderId(self.purchaseOrder.id);
    	self.purchaseOrderViewModel.totalAmount(self.purchaseOrder.totalAmount);
		self.purchaseOrderViewModel.companyName(self.purchaseOrder.company.name);
		self.purchaseOrderViewModel.receiptType(self.purchaseOrder.company.receiptType);
		self.purchaseOrderViewModel.status(self.purchaseOrder.status);

		switch(self.purchaseOrderViewModel.receiptType()) {
	    	case 'BEFORE_VAT_AND_DISCOUNT':
	    		self.beforeVatAndDiscount(true);
	    		break;
	    	case 'BEFORE_VAT_AFTER_DISCOUNT':
	    		self.beforeVatAfterDiscount(true);
	    		break;
	    	case 'AFTER_VAT_BEFORE_DISCOUNT':
	    		self.afterVatBeforeDiscount(true);
	    		break;
		}
		
		if(self.purchaseOrderViewModel.status() == 'LISTING') {
			self.isListing(true);
		} else {
			self.isListing(false);
		}
    	
    	self.currentPage(1);
    	self.currentPageSubscription = self.currentPage.subscribe(function() {
    		self.refreshPurchaseOrderDetailList();
		});
    	
    	self.refreshPurchaseOrderDetailList();
    };
    
    PurchaseView.show = function(purchaseOrder) {
    	return dialog.show(new PurchaseView(purchaseOrder));
    };
    
    PurchaseView.prototype.refreshPurchaseOrderDetailList = function() {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrderDetailList(self.currentPage(), self.purchaseOrderViewModel.purchaseOrderId(), false).done(function(data) { 
			self.purchaseOrderDetailList(data.list);
			self.totalItems(data.total);
		});
    };
    
    PurchaseView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    PurchaseView.prototype.check = function() {
		var self = this;
		
		app.showMessage('<div class="container-fluid"><dl class="dl-horizontal"><dt>Purchase ID  :</dt><dd>' + self.purchaseOrderViewModel.purchaseOrderId() + '</dd>' +
						'<dt>Company Name :</dt><dd>' + self.purchaseOrderViewModel.companyName() + '</dd>' +
						'<dt>Total Amount :</dt><dd>Php ' + self.purchaseOrderViewModel.totalAmount() + '</dd></div>',
				'Confirm Check',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				purchaseOrderService.checkPurchaseOrder(self.purchaseOrderViewModel.purchaseOrderId()).done(function(result) {
					if(result.success) {
						dialog.close(self);
					}
					app.showMessage(result.message);
				});
			}
		})
	};
    
    return PurchaseView;
});