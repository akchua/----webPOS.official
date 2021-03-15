define(['durandal/app', 'knockout', 'modules/inventoryservice', 'modules/purchaseorderservice', 'modules/customerorderservice'], 
		function (app, ko, inventoryService, purchaseOrderService, customerOrderService) {
    var ProductInventory = function() {
    	this.productId = ko.observable();
    	
    	this.formattedInventory = ko.observable();
    	
    	this.time = ko.observable();
    	
    	this.purchaseOrderDetailList = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.customerOrderDetailList = ko.observable();
		
		this.itemsPerPage2 = ko.observable(app.user.itemsPerPage);
		this.totalItems2 = ko.observable();
		this.currentPage2 = ko.observable(1);
		this.currentPageSubscription2 = null;
		
		this.allowStats = app.user.userType.authority < 3;
    };
    
    ProductInventory.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.currentPage(1);
    	self.currentPageSubscription = self.currentPage.subscribe(function() {
    		self.refreshPurchaseOrderDetailList();
		});
    	
    	self.currentPage2(1);
    	self.currentPageSubscription2 = self.currentPage2.subscribe(function() {
    		self.refreshCustomerOrderDetailList();
		});
    	
    	self.productId = activationData.productId;
    	self.refreshInventory();
    	self.refreshPurchaseOrderDetailList();
    	self.refreshCustomerOrderDetailList();
    };
    
    ProductInventory.prototype.refreshInventory = function() {
    	var self = this;
    	
    	var today = new Date();
		self.time(today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds());
		
		inventoryService.getProductInventory(self.productId()).done(function(inventory) {
			self.formattedInventory(inventory.formattedInventory);
		});
    };
    
    ProductInventory.prototype.refreshPurchaseOrderDetailList = function() {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrderDetailListByProduct(self.currentPage(), self.productId(), false).done(function(data) { 
			self.purchaseOrderDetailList(data.list);
			self.totalItems(data.total);
		});
    };
    
    ProductInventory.prototype.refreshCustomerOrderDetailList = function() {
    	var self = this;
    	
    	customerOrderService.getCustomerOrderDetailListByProduct(self.currentPage2(), self.productId(), true).done(function(data) { 
			self.customerOrderDetailList(data.list);
			self.totalItems2(data.total);
		});
    };
    
    return ProductInventory;
});