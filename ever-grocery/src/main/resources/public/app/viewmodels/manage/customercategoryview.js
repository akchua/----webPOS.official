define(['plugins/dialog', 'knockout'], 
			function (dialog, ko) {
    var CustomerCategoryView = function(customerCategory) {
    	this.customerCategory = customerCategory;
    	
    	this.customerCategoryId = ko.observable();
    	this.customerCategoryName = ko.observable();
    };
    
    CustomerCategoryView.prototype.activate = function() {
    	var self = this;
    	
    	self.customerCategoryId(self.customerCategory.id);
    	self.customerCategoryName(self.customerCategory.name);
    };
    
    CustomerCategoryView.show = function(customerCategory) {
    	return dialog.show(new CustomerCategoryView(customerCategory));
    };
    
    CustomerCategoryView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return CustomerCategoryView;
});