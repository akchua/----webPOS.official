define(['durandal/app', 'knockout', 'modules/customerCategoryservice', 'modules/customerservice'], 
		function (app, ko, customerCategoryService, customerService) {
    var CustomerCategoryCustomers = function() {
    	this.customerCategoryId = ko.observable();
    	this.customerList = ko.observable();
    	
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
    };
    
    CustomerCategoryCustomers.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.customerCategoryId = activationData.customerCategoryId;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCustomerList();
		});
		
    	self.refreshCustomerList();
    };
    
    CustomerCategoryCustomers.prototype.refreshCustomerList = function() {
		var self = this;
		
		customerService.getCustomerListByCategory(self.currentPage(), self.customerCategoryId(), false).done(function(data) {
			self.customerList(data.list);
			self.totalItems(data.total);
		});
	};
    
    return CustomerCategoryCustomers;
});