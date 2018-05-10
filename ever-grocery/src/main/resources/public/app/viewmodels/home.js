define(['durandal/app', 'knockout', 'modules/productservice'], function (app, ko, productService) {
	var Home = function() {
		this.salePriceHistoryList = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
	};
	
	Home.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshSalePriceHistoryList();
		});
		
		self.refreshSalePriceHistoryList();
	};
	
	Home.prototype.refreshSalePriceHistoryList = function() {
		var self = this;
		
		productService.getSalePriceHistoryList(self.currentPage()).done(function(data) {
			self.salePriceHistoryList(data.list);
			self.totalItems(data.total);
		});
	};
	
    return Home;
});