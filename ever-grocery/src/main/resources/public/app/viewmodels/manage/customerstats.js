define(['jquery', 'c3', 'durandal/app', 'knockout', 'modules/customersummaryservice', 'modules/c3utility'], 
		function ($, c3, app, ko, customerSummaryService, c3Utility) {
	var CustomerStats = function() {
		this.customerId = ko.observable();
		
		this.chart = null;
		this.x = ['x'];
		this.mtdLuxurySales = ['Monthly Luxury Sales'];
		this.mtdNetSales = ['Monthly Net Sales'];
		this.mtdProfit = ['Monthly Profit'];
	};
	
	CustomerStats.prototype.activate = function(activationData) {
		var self = this;
		
		self.customerId = activationData.customerId;
	};
	
	CustomerStats.prototype.attached = function() {
		var self = this;
		
		customerSummaryService.getCustomerMTDSalesSummaryList(self.customerId()).done(function(customerMTDSalesList) {
			var i = 0;
			for (i = 0; i < customerMTDSalesList.length; i++) {
				self.x[i + 1] = customerMTDSalesList[i].formattedMonth;
				self.mtdLuxurySales[i + 1] = customerMTDSalesList[i].luxuryTotal;
				self.mtdNetSales[i + 1] = customerMTDSalesList[i].netTotal;
				self.mtdProfit[i + 1] = customerMTDSalesList[i].profit;
			}
			
			self.chart = c3.generate({
			    bindto: '#chart',
			    data: {
			    	x: 'x',
			    	columns: [
			    		self.x,
			    		self.mtdLuxurySales,
			    		self.mtdNetSales,
			    		self.mtdProfit
			    	]
			    },
			    axis: c3Utility.getDefaultAxis()
			});
		});
	};
	
    return CustomerStats;
});