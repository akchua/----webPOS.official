define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/promoservice', 'modules/constantsservice'], 
		function (dialog, app, ko, promoService, constantsService) {
    var PromoForm = function(preTitle, promo, product) {
    	this.preTitle = preTitle;
        this.promo = promo;
    	this.product = product;
        
        this.promoFormModel = {
        	id: ko.observable(),
        	productId: ko.observable(),
        	startDate: ko.observable(),
        	endDate: ko.observable(),
        	budget: ko.observable(),
        	usedBudget: ko.observable(0),
        	discountPercent: ko.observable(),
        	promoType: ko.observable()
        };
        
        this.errors = {
    		productId: ko.observable(),
        	startDate: ko.observable(),
        	endDate: ko.observable(),
        	budget: ko.observable(),
        	usedBudget: ko.observable(0),
        	discountPercent: ko.observable(),
        	promoType: ko.observable()
    	};
        
        this.promoTypeList = ko.observable();
    };
 
    PromoForm.prototype.activate = function() {
    	var self = this;
    	
    	self.promoFormModel.id(self.promo.id);
    	self.promoFormModel.productId(self.product.id);
    	self.promoFormModel.startDate(self.promo.startDate);
    	self.promoFormModel.endDate(self.promo.endDate);
    	self.promoFormModel.budget(self.promo.budget);
    	if(self.promo && self.promo.usedBudget) {
    		self.promoFormModel.usedBudget(self.promo.usedBudget);
    	} else {
    		self.promoFormModel.usedBudget(0);
    	}
    	self.promoFormModel.discountPercent(self.promo.discountPercent);
    	
    	constantsService.getPromoTypeList().done(function(promoTypeList) {
    		self.promoTypeList(promoTypeList);
    		if(self.promo.promoType) self.promoFormModel.promoType(self.promo.promoType.name);
    	});
    };
    
    PromoForm.show = function(preTitle, promo, product) {
        return dialog.show(new PromoForm(preTitle, promo, product));
    };
    
    PromoForm.prototype.save = function() {
    	var self = this;
    	
        promoService.savePromo(ko.toJSON(self.promoFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);
        		app.showMessage(result.message);
        	} else {
        		self.errors.productId(result.extras.errors.productId);
        		self.errors.startDate(result.extras.errors.startDate);
        		self.errors.endDate(result.extras.errors.endDate);
        		self.errors.budget(result.extras.errors.budget);
        		self.errors.usedBudget(result.extras.errors.usedBudget);
        		self.errors.discountPercent(result.extras.errors.discountPercent);
        		self.errors.promoType(result.extras.errors.promoType);
        	}
        });
    };
    
    PromoForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return PromoForm;
});