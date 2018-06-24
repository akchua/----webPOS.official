define(['durandal/app', 'knockout', 'modules/productservice', 'viewmodels/search/more'], 
		function (app, ko, productService, More) {
	var Product = function() {
		this.productList = ko.observable();
		
		this.searchKey = ko.observable();
		this.searchFocus = ko.observable(true);
		
		this.moreEnable = true;
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
	};
	
	Product.prototype.activate = function() {
		var self = this;
		
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
	
	Product.prototype.refreshProductList = function() {
		var self = this;
		
		productService.getProductList(self.currentPage(), self.searchKey(), null, true).done(function(data) {
			self.productList(data.list);
			self.totalItems(data.total);
			
			if(self.totalItems() == 1) {
				self.more(self.productList()[0].id);
				self.searchKey("");
			}
		});
	};
	
	Product.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshProductList();
	};
	
	Product.prototype.more = function(productId) {
		var self = this;
		
		if(self.moreEnable) {
			self.moreEnable = false;
			productService.getProduct(productId).done(function(data) {
				More.show(data).done(function() {
					self.searchKey('');
					self.searchFocus(true);
					self.moreEnable = true;
				});
			});
		}
	};
	
    return Product;
});