define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/brandservice'], function (dialog, app, ko, brandService) {
    var BrandForm = function(preTitle, brand) {
        this.preTitle = preTitle;
        this.brand = brand;
        
        this.brandFormModel = {
        	id: ko.observable(),
        	name: ko.observable()	
        };
    };
    
    BrandForm.prototype.activate = function() {
    	var self = this;
    	
    	self.brandFormModel.id(self.brand.id);
    	self.brandFormModel.name(self.brand.name);
    };
 
    BrandForm.show = function(preTitle, brand) {
        return dialog.show(new BrandForm(preTitle, brand));
    };
    
    BrandForm.prototype.save = function() {
    	var self = this;
    	
        brandService.saveBrand(ko.toJSON(self.brandFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);
        	} 
        	app.showMessage(result.message);
        });
    };
    
    BrandForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return BrandForm;
});