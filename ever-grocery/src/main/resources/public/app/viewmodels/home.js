﻿define(['durandal/app', 'knockout', 'modules/productservice', 'modules/promoservice', 'modules/fileservice'], 
		function (app, ko, productService, promoService, fileService) {
	var Home = function() {
		this.salePriceHistoryList = ko.observable();
		this.promoList = ko.observable();
		this.recentlyEndedPromoList = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.totalItems2 = ko.observable();
		this.currentPage2 = ko.observable(1);
		this.currentPageSubscription2 = null;
		
		this.totalItems3 = ko.observable();
		this.currentPage3 = ko.observable(1);
		this.currentPageSubscription3 = null;
		
		this.enableDownloadButton = ko.observable(true);
		this.allowDownload = app.user.userType.authority < 9;
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
		
		self.currentPage3(1);
		self.currentPageSubscription3 = self.currentPage3.subscribe(function() {
			self.refreshRecentlyEndedPromoList();
		});
		
		self.refreshSalePriceHistoryList();
		self.refreshPromoList();
		self.refreshRecentlyEndedPromoList();
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
	
	Home.prototype.printCurrentPromo = function() {
    	var self = this;
    	
    	self.enableDownloadButton(false);
    	promoService.generateCurrentPromoPDF().done(function(result) {
        	if(result.success) {
				fileService.downloadCurrentPromoByFileName(result.extras.fileName);
    			dialog.close(self);
    		} else {
    			app.showMessage(result.message);
    		}
        	self.enableDownloadButton(true);
        });
    };
	
	Home.prototype.refreshRecentlyEndedPromoList = function() {
		var self = this;
		
		promoService.getRecentlyEndedPromoList(self.currentPage3(), true, true).done(function(data) {
			self.recentlyEndedPromoList(data.list);
			self.totalItems3(data.total);
		});
	};
	
    return Home;
});