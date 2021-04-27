define(['durandal/app', 'knockout', 'modules/categoryservice', 'modules/productservice'], 
		function (app, ko, categoryService, productService) {
    var CategoryProducts = function() {
    	this.categoryId = ko.observable();
    	this.productList = ko.observable();
    	
    	this.searchKey = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
    };
    
    CategoryProducts.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.categoryId = activationData.categoryId;
    	
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
    
    CategoryProducts.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshProductList();
	};
    
    CategoryProducts.prototype.refreshProductList = function() {
		var self = this;
		
		productService.getProductListByCategory(self.currentPage(), self.searchKey(), self.categoryId(), false).done(function(data) {
			self.productList(data.list);
			self.totalItems(data.total);
		});
	};
    
    return CategoryProducts;
});