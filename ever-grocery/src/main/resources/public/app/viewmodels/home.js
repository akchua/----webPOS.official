define(['durandal/app', 'knockout', 'modules/productservice', 'modules/promoservice'], 
		function (app, ko, productService, promoService) {
	var Home = function() {
		this.salePriceHistoryList = ko.observable();
		this.promoList = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.totalItems2 = ko.observable();
		this.currentPage2 = ko.observable(1);
		this.currentPageSubscription2 = null;
	};
	
	Home.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshSalePriceHistoryList();
		});
		
		self.currentPage2(1);
		self.currentPageSubscription2 = self.currentPage2.subscribe(function() {
			self.refreshPromoList();
		});
		
		self.refreshSalePriceHistoryList();
		self.refreshPromoList();
	};
	
	Home.prototype.refreshSalePriceHistoryList = function() {
		var self = this;
		
		productService.getSalePriceHistoryList(self.currentPage()).done(function(data) {
			self.salePriceHistoryList(data.list);
			self.totalItems(data.total);
		});
	};
	
	Home.prototype.refreshPromoList = function() {
		var self = this;
		
		promoService.getPromoList(self.currentPage2(), true, true).done(function(data) {
			self.promoList(data.list);
			self.totalItems2(data.total);
		});
	};
	
    return Home;
});