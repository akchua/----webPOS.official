define(['durandal/app', 'knockout', 'modules/promoservice', 'viewmodels/manage/promoform'], 
		function (app, ko, promoService, PromoForm) {
	var Promo = function() {
		this.promoList = ko.observable();
		
		this.showActiveOnly = ko.observable(true);
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
	};
	
	Promo.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshPromoList();
		});
		
		self.showActiveOnly.subscribe(function() {
			self.refreshPromoList();
		});
		
		self.refreshPromoList();
	};
	
	Promo.prototype.refreshPromoList = function() {
		var self = this;
		
		promoService.getPromoList(self.currentPage(), self.showActiveOnly(), true).done(function(data) {
			self.promoList(data.list);
			self.totalItems(data.total);
		});
	};
	
	/*Promo.prototype.view = function(promoId) {
    	var self = this;
    	
    	promoService.getPromo(promoId).done(function(promo) {
    		PromoView.show(promo);
    	});
    };*/
	
	Promo.prototype.edit = function(promoId) {
		var self = this;
		self.enableButtons(false);
		
		promoService.getPromo(promoId).done(function(promo) {
			PromoForm.show('Update', promo, promo.product).then(function() {
				self.refreshPromoList();
				self.enableButtons(true);
			});
		});
	};
	
	Promo.prototype.remove = function(promoId, productName) {
		var self = this;
		self.enableButtons(false);
		
		app.showMessage('Are you sure you want to remove Promo of Product "' + productName + '"?',
				'Confirm Remove',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				promoService.removePromo(promoId).done(function(result) {
					self.refreshPromoList();
					app.showMessage(result.message);
				});
			}
			self.enableButtons(true);
		})
	};
	
    return Promo;
});