define(['durandal/app', 'knockout', 'modules/customerorderservice', 'viewmodels/report/salereport', 'viewmodels/report/zreading', 'viewmodels/report/saleview'], 
		function (app, ko, customerOrderService, SaleReport, ZReading, SaleView) {
	var CustomerOrder = function() {
		this.customerOrderList = ko.observable();
		
		this.showRecent = ko.observable(true);
		
		this.quickId = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
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
	
	CustomerOrder.prototype.quickView = function() {
		var self = this;
		
		customerOrderService.getCustomerOrderBySIN(self.quickId()).done(function(customerOrder) {
			if(customerOrder) {
				SaleView.show(customerOrder).done(function() {
					self.refreshCustomerOrderList();
				});
			} else {
				app.showMessage('Serial Invoice Number ' + self.quickId() + ' does not exist.');
			}
		});
		self.quickId('');
	};
	
	CustomerOrder.prototype.generateReport = function() {
    	SaleReport.show()
    };
    
    CustomerOrder.prototype.zReading = function() {
    	ZReading.show()
    };
	
	CustomerOrder.prototype.view = function(customerOrderId) {
		var self = this;
		self.enableButtons(false);
		
		customerOrderService.getCustomerOrder(customerOrderId).done(function(data) {
			SaleView.show(data).done(function() {
				self.refreshCustomerOrderList();
				self.enableButtons(true);
			});
		});
	};
	
	CustomerOrder.prototype.print = function(customerOrderId, customerOrderNumber) {
		var self = this;
		self.enableButtons(false);
		
		app.showMessage('Are you sure you want to print Customer Order #' + customerOrderNumber + '?',
				'Confirm Print',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.printReceiptCopy(customerOrderId).done(function(result) {
					self.refreshCustomerOrderList();
					app.showMessage(result.message);
				});
			}
			self.enableButtons(true);
		});
	};
	
    return CustomerOrder;
});