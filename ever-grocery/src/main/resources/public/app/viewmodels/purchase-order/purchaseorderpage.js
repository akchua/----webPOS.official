define(['durandal/app', 'knockout', 'modules/purchaseorderservice', 'viewmodels/purchase-order/search'], function (app, ko, purchaseOrderService, Search) {
    var PurchaseOrderPage = function() {
    	this.purchaseOrderDetailList = ko.observable();

    	this.beforeVatAndDiscount = ko.observable(false);
    	this.beforeVatAfterDiscount = ko.observable(false);
    	this.afterVatBeforeDiscount = ko.observable(false);
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.purchaseOrderPageModel = {
			purchaseOrderId: ko.observable(),
			formattedTotalAmount: ko.observable(),
			companyName: ko.observable(),
			receiptType: ko.observable(),
			status: ko.observable()
		};
    };
    
    PurchaseOrderPage.prototype.activate = function(purchaseOrderId) {
    	var self = this;
    	
    	self.purchaseOrderPageModel.purchaseOrderId(purchaseOrderId);
    	
    	self.currentPage(1);
    	self.currentPageSubscription = self.currentPage.subscribe(function() {
    		self.refreshPurchaseOrderDetailList();
		});
    	
    	purchaseOrderService.refreshPurchaseOrder(self.purchaseOrderPageModel.purchaseOrderId()).done(function() {
    		self.refreshPurchaseOrderDetailList();
    	});
    };
    
    PurchaseOrderPage.prototype.search = function() {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrder(self.purchaseOrderPageModel.purchaseOrderId()).done(function(data) { 
    		Search.show(data, self.purchaseOrderPageModel.receiptType()).done(function() {
        		self.refreshPurchaseOrderDetailList();
        	});
    	});
    };
    
    PurchaseOrderPage.prototype.refreshPurchaseOrderDetailList = function() {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrder(self.purchaseOrderPageModel.purchaseOrderId()).done(function(data) { 
    		self.purchaseOrderPageModel.formattedTotalAmount(data.formattedTotalAmount);
    		self.purchaseOrderPageModel.companyName(data.company.name);
    		self.purchaseOrderPageModel.receiptType(data.company.receiptType.displayName);
    		self.purchaseOrderPageModel.status(data.status.displayName);
    		
    		switch(self.purchaseOrderPageModel.receiptType()) {
		    	case 'Before VAT and Discount':
		    		self.beforeVatAndDiscount(true);
		    		break;
		    	case 'Before VAT, After Discount':
		    		self.beforeVatAfterDiscount(true);
		    		break;
		    	case 'After VAT, Before Discount':
		    		self.afterVatBeforeDiscount(true);
		    		break;
    		}
    	});
    	
    	purchaseOrderService.getPurchaseOrderDetailList(self.currentPage(), self.purchaseOrderPageModel.purchaseOrderId(), true).done(function(data) { 
			self.purchaseOrderDetailList(data.list);
			self.totalItems(data.total);
		});
    };
    
    PurchaseOrderPage.prototype.remove = function(purchaseOrderDetailId, quantity, productName, unitType) {
		var self = this;
		
		app.showMessage('Are you sure you want to remove ' + quantity + ' "' + productName + ' (' + unitType + ')"?',
				'Confirm Remove',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				purchaseOrderService.removePurchaseOrderDetail(purchaseOrderDetailId).done(function(result) {
					self.refreshPurchaseOrderDetailList();
					app.showMessage(result.message);
				});
			}
		})
	};
	
	PurchaseOrderPage.prototype.changeQuantity = function(purchaseOrderDetailId, quantity) {
    	var self = this;
    	
    	purchaseOrderService.changeQuantity(purchaseOrderDetailId, quantity).done(function(result) {
    		self.refreshPurchaseOrderDetailList();
    		if(!result.success) {
    			app.showMessage(result.message);
    		}
    	});
    };
    
    return PurchaseOrderPage;
});