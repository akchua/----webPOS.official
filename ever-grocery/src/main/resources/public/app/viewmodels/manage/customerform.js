define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerservice'], function (dialog, app, ko, customerService) {
    var CustomerForm = function(preTitle, customer) {
        this.preTitle = preTitle;
        this.customer = customer;
        
        this.customerFormModel = {
        	id : ko.observable(),
        	
        	firstName : ko.observable(),
        	lastName : ko.observable(),
        	contactNumber : ko.observable(),
        	address : ko.observable(),
        	
        	cardId : ko.observable()
        };
        
        this.errors = {
    		firstName : ko.observable(),
        	lastName : ko.observable(),
        	contactNumber : ko.observable(),
        	address : ko.observable(),
        	
        	cardId : ko.observable()	
        };
    };
    
    CustomerForm.prototype.activate = function() {
    	var self = this;
    	
    	self.customerFormModel.id(self.customer.id);
    	self.customerFormModel.firstName(self.customer.firstName);
    	self.customerFormModel.lastName(self.customer.lastName);
    	self.customerFormModel.contactNumber(self.customer.contactNumber);
    	self.customerFormModel.address(self.customer.address);
    	self.customerFormModel.cardId(self.customer.cardId);
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
        		self.errors.firstName(result.extras.errors.firstName);
        		self.errors.lastName(result.extras.errors.lastName);
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