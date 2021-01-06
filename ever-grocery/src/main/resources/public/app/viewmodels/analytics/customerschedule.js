define(['durandal/app', 'knockout', 'modules/customerservice'], 
		function (app, ko, customerService) {
	var CustomerSchedule = function() {
		this.customerList = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
	};
	
	CustomerSchedule.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCustomerList();
		});
		
		self.refreshCustomerList();
	};
	
	CustomerSchedule.prototype.refreshCustomerList = function() {
		var self = this;
		
		customerService.getOutOfScheduleCustomerList(self.currentPage()).done(function(data) {
			self.customerList(data.list);
			self.totalItems(data.total);
		});
	};
	
    return CustomerSchedule;
});