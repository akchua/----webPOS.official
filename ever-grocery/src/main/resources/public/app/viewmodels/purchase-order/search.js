define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice', 'viewmodels/purchase-order/searchdetails'], function (dialog, app, ko, productService, SearchDetails) {
	var Search = function(purchaseOrder, receiptType) {
		this.purchaseOrder = purchaseOrder;
		this.productList = ko.observable();
		this.receiptType = receiptType;
		
		this.companyId = ko.observable();
		
		this.searchKey = ko.observable();
		this.searchFocus = ko.observable(true);
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
	};
	
	Search.prototype.activate = function() {
		var self = this;
		
		self.companyId(self.purchaseOrder.company.id);
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshProductList();
		});
		
		self.searchKey.subscribe(function(searchKey) {
			if(searchKey.length >= 3) {
				self.search();
			}
		});
		
		self.refreshProductList();
	};
	
	Search.show = function(purchaseOrder, receiptType) {
		return dialog.show(new Search(purchaseOrder, receiptType));
	};
	
	Search.prototype.refreshProductList = function() {
		var self = this;
		
		productService.getProductList(self.currentPage(), self.searchKey(), self.companyId(), false).done(function(data) {
			self.productList(data.list);
			self.totalItems(data.total);
		});
	};
	
	Search.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshProductList();
	};
	
	Search.prototype.details = function(productId) {
		var self = this;
		self.enableButtons(false);

		productService.getProduct(productId).done(function (data) {
			SearchDetails.show(data, self.purchaseOrder, self.receiptType).then(function() {
				self.searchKey('');
				self.searchFocus(true);
				self.enableButtons(true);
			});
		});
	};
	
	Search.prototype.cancel = function() {
        dialog.close(this);
    };
	
    return Search;
});