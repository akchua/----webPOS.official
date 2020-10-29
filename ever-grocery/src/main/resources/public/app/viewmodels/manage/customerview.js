define(['plugins/dialog', 'knockout'], 
			function (dialog, ko) {
    var CustomerView = function(customer) {
    	this.customer = customer;
    	
    	this.customerId = ko.observable();
    	this.customerName = ko.observable();
    };
    
    CustomerView.prototype.activate = function() {
    	var self = this;
    	
    	self.customerId(self.customer.id);
    	self.customerName(self.customer.name);
    };
    
    CustomerView.show = function(customer) {
    	return dialog.show(new CustomerView(customer));
    };
    
    CustomerView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return CustomerView;
});