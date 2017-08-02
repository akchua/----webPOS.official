define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerservice'], function (dialog, app, ko, customerService) {
    var CustomerForm = function(preTitle, customer) {
        this.preTitle = preTitle;
        this.customer = customer;
        
        this.customerFormModel = {
        	id: ko.observable(),
        	firstName: ko.observable(),
        	lastName: ko.observable(),
        	address: ko.observable()
        };
    };
    
    CustomerForm.prototype.activate = function() {
    	var self = this;
    	
    	self.customerFormModel.id(self.customer.id);
    	self.customerFormModel.firstName(self.customer.firstName);
    	self.customerFormModel.lastName(self.customer.lastName);
    	self.customerFormModel.address(self.customer.address);
    };
 
    CustomerForm.show = function(preTitle, customer) {
        return dialog.show(new CustomerForm(preTitle, customer));
    };
    
    CustomerForm.prototype.save = function() {
    	var self = this;
    	
        customerService.saveCustomer(ko.toJSON(self.customerFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    CustomerForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return CustomerForm;
});