define(['jquery', 'c3', 'durandal/app', 'knockout', 'modules/customersummaryservice', 'modules/c3utility'], 
		function ($, c3, app, ko, customerSummaryService, c3Utility) {
	var CustomerCategoryStats = function() {
		this.customerCategoryId = ko.observable();
		
		this.chart = null;
		this.x = ['x'];
		this.mtdLuxurySales = ['Monthly Luxury Sales'];
		this.mtdNetSales = ['Monthly Net Sales'];
		this.mtdProfit = ['Monthly Profit'];
	};
	
	CustomerCategoryStats.prototype.activate = function(activationData) {
		var self = this;
		
		self.customerCategoryId = activationData.customerCategoryId;
	};
	
	CustomerCategoryStats.prototype.attached = function() {
		var self = this;
		
		customerSummaryService.getCustomerCategoryMTDSalesSummaryList(self.customerCategoryId()).done(function(customerCategoryMTDSalesList) {
			var i = 0;
			for (i = 0; i < customerCategoryMTDSalesList.length; i++) {
				self.x[i + 1] = customerCategoryMTDSalesList[i].formattedMonth;
				self.mtdLuxurySales[i + 1] = customerCategoryMTDSalesList[i].luxuryTotal;
				self.mtdNetSales[i + 1] = customerCategoryMTDSalesList[i].netTotal;
				self.mtdProfit[i + 1] = customerCategoryMTDSalesList[i].profit;
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
	
    return CustomerCategoryStats;
});