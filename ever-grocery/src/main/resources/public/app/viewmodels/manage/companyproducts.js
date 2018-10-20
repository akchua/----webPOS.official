define(['durandal/app', 'knockout', 'modules/companyservice', 'modules/productService'], 
		function (app, ko, companyService, productService) {
    var CompanyProducts = function() {
    	this.companyId = ko.observable();
    	this.productList = ko.observable();
    	
    	this.searchKey = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    CompanyProducts.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.companyId = activationData.companyId;
    	
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
    
    CompanyProducts.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshProductList();
	};
    
    CompanyProducts.prototype.refreshProductList = function() {
		var self = this;
		
		productService.getProductListByRank(self.currentPage(), self.searchKey(), self.companyId(), false).done(function(data) {
			self.productList(data.list);
			self.totalItems(data.total);
		});
	};
    
    return CompanyProducts;
});