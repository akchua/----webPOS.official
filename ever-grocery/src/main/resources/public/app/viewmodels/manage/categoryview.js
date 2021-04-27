define(['plugins/dialog', 'knockout'], 
			function (dialog, ko) {
    var CategoryView = function(category) {
    	this.category = category;
    	
    	this.categoryId = ko.observable();
    	this.categoryName = ko.observable();
    };
    
    CategoryView.prototype.activate = function() {
    	var self = this;
    	
    	self.categoryId(self.category.id);
    	self.categoryName(self.category.name);
    };
    
    CategoryView.show = function(category) {
    	return dialog.show(new CategoryView(category));
    };
    
    CategoryView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return CategoryView;
});