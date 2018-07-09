define(['durandal/app', 'knockout', 'modules/customerorderservice', 'viewmodels/report/salereport', 'viewmodels/report/saleview'], 
		function (app, ko, customerOrderService, SaleReport, SaleView) {
	var CustomerOrder = function() {
		this.customerOrderList = ko.observable();
		
		this.showRecent = ko.observable(true);
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
	};
	
	CustomerOrder.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCustomerOrderList();
		});
		self.showRecent.subscribe(function() {
			self.refreshCustomerOrderList();
		});
		
		self.refreshCustomerOrderList();
	};
	
	CustomerOrder.prototype.refreshCustomerOrderList = function() {
		var self = this;
		
		if(self.showRecent()) {
			customerOrderService.getCustomerOrderList(self.currentPage(), null, true, 7).done(function(data) {
				self.customerOrderList(data.list);
				self.totalItems(data.total);
			});
		} else {
			customerOrderService.getCustomerOrderList(self.currentPage(), null, true, null).done(function(data) {
				self.customerOrderList(data.list);
				self.totalItems(data.total);
			});
		}
	};
	
	CustomerOrder.prototype.generateReport = function() {
    	SaleReport.show()
    };
	
	CustomerOrder.prototype.view = function(customerOrderId) {
		var self = this;
		
		customerOrderService.getCustomerOrder(customerOrderId).done(function(data) {
			SaleView.show(data).done(function() {
				self.refreshCustomerOrderList();
			});
		});
	};
	
	CustomerOrder.prototype.print = function(customerOrderId, customerOrderName) {
		var self = this;
		
		app.showMessage('Are you sure you want to print Customer Order "' + customerOrderName + '"?',
				'Confirm Print',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.printCustomerOrderList(customerOrderId).done(function(result) {
					self.refreshCustomerOrderList();
					app.showMessage(result.message);
				});
			}
		});
	};
	
    return CustomerOrder;
});