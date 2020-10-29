define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerservice', 'modules/customercategoryservice'], 
			function (dialog, app, ko, customerService, customerCategoryService) {
    var CustomerForm = function(preTitle, customer) {
        this.preTitle = preTitle;
        this.customer = customer;
        
        this.customerFormModel = {
        	id : ko.observable(),
        	
        	customerCategoryId: ko.observable(),
        	name : ko.observable(),
        	storeName : ko.observable(),
        	code: ko.observable(),
        	contactNumber : ko.observable(),
        	address : ko.observable(),
        	
        	cardId : ko.observable()
        };
        
        this.errors = {
        	customerCategoryId: ko.observable(),
    		name : ko.observable(),
        	storeName : ko.observable(),
        	code: ko.observable(),
        	contactNumber : ko.observable(),
        	address : ko.observable(),
        	
        	cardId : ko.observable()	
        };
        
        this.customerCategoryList = ko.observable();
    };
    
    CustomerForm.prototype.activate = function() {
    	var self = this;
    	
    	self.customerFormModel.id(self.customer.id);
    	self.customerFormModel.name(self.customer.name);
    	self.customerFormModel.storeName(self.customer.storeName);
    	self.customerFormModel.code(self.customer.code);
    	self.customerFormModel.contactNumber(self.customer.contactNumber);
    	self.customerFormModel.address(self.customer.address);
    	self.customerFormModel.cardId(self.customer.cardId);
    	
    	customerCategoryService.getCustomerCategoryListByName().done(function(customerCategoryList) {
    		self.customerCategoryList(customerCategoryList);
    		self.customerFormModel.customerCategoryId(self.customer.customerCategory.id);
    	});
    };
 
    CustomerForm.show = function(preTitle, customer) {
        return dialog.show(new CustomerForm(preTitle, customer));
    };
    
    CustomerForm.prototype.save = function() {
    	var self = this;
    	
        customerService.saveCustomer(ko.toJSON(self.customerFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} else {
        		self.errors.customerCategoryId(result.extras.errors.customerCategoryId);
        		self.errors.name(result.extras.errors.name);
        		self.errors.storeName(result.extras.errors.storeName);
        		self.errors.code(result.extras.errors.code);
        		self.errors.contactNumber(result.extras.errors.contactNumber);
        		self.errors.address(result.extras.errors.address);
        		self.errors.cardId(result.extras.errors.cardId);
        	}
        	if(result.message) app.showMessage(result.message);
        });
    };
    
    CustomerForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return CustomerForm;
});