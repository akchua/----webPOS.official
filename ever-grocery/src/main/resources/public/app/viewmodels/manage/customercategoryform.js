define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customercategoryservice'], function (dialog, app, ko, customerCategoryService) {
    var CustomerCategoryForm = function(preTitle, customerCategory) {
        this.preTitle = preTitle;
        this.customerCategory = customerCategory;
        
        this.customerCategoryFormModel = {
        	id: ko.observable(),
        	name: ko.observable(),
        	code: ko.observable()
        };
        
        this.errors = {
    		name : ko.observable(),
        	code: ko.observable()
        };
    };
    
    CustomerCategoryForm.prototype.activate = function() {
    	var self = this;
    	
    	self.customerCategoryFormModel.id(self.customerCategory.id);
    	self.customerCategoryFormModel.name(self.customerCategory.name);
    	self.customerCategoryFormModel.code(self.customerCategory.code);
    };
 
    CustomerCategoryForm.show = function(preTitle, customerCategory) {
        return dialog.show(new CustomerCategoryForm(preTitle, customerCategory));
    };
 
    CustomerCategoryForm.prototype.save = function() {
    	var self = this;
    	
    	customerCategoryService.saveCustomerCategory(ko.toJSON(self.customerCategoryFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} else {
        		self.errors.name(result.extras.errors.name);
        		self.errors.code(result.extras.errors.code);
        	}
        	app.showMessage(result.message);
        });
    };
    
    CustomerCategoryForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return CustomerCategoryForm;
});