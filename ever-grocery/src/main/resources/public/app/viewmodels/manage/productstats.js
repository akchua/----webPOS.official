define(['jquery', 'c3', 'durandal/app', 'knockout', 'modules/transactionsummaryservice', 'modules/c3utility'], 
		function ($, c3, app, ko, transactionSummaryService, c3Utility) {
	var ProductStats = function() {
		this.productId = ko.observable();
		
		this.daysAgo = ko.observable(90);
		
		this.dailyChart = null;
		this.dx = null;
		this.dailyNetSales = null;
		this.dailyProfit = null;
		this.averageNetSales = ko.observable(0.0);
		this.averageProfit = ko.observable(0.0);
	};
	
	ProductStats.prototype.activate = function(activationData) {
		var self = this;
		
		self.productId = activationData.productId;
	};
	
	ProductStats.prototype.attached = function() {
		var self = this;
		
		self.refreshDailySalesGraph();
		
		self.daysAgo.subscribe(function() {
			self.refreshDailySalesGraph();
		});
	};
	
	ProductStats.prototype.clearDailySalesGraph = function() {
		var self = this;
		
		self.dailyChart = null;
		self.dx = null;
		self.dailyNetPurchase = null;
		self.dailyProfit = null;
	};
	
	ProductStats.prototype.refreshDailySalesGraph = function() {
		var self = this;
		
		self.clearDailySalesGraph();
		
		transactionSummaryService.getProductDailySalesSummaryList(self.productId(), self.daysAgo()).done(function(dailySalesList) {
			var tempAverageNetSales = 0;
			var tempAverageProfit = 0;
			
			self.dx = ['x'];
			self.dailyNetSales = ['Daily Net Sales'];
			self.dailyProfit = ['Daily Profit'];
			
			for(var i = 0; i < dailySalesList.length; i++) {
				self.dx[i + 1] = dailySalesList[i].formattedSalesDate;
				self.dailyNetSales[i + 1] = dailySalesList[i].netTotal;
				self.dailyProfit[i + 1] = dailySalesList[i].profit;
				
				tempAverageNetSales += dailySalesList[i].netTotal;
				tempAverageProfit += dailySalesList[i].profit;
			}
			
			tempAverageNetSales /= self.daysAgo();
			tempAverageProfit /= self.daysAgo();
			
			self.averageNetSales(tempAverageNetSales.toLocaleString(
					undefined,
					{ minimumFractionDigits: 2,
						maximumFractionDigits: 2 }
				));
			self.averageProfit(tempAverageProfit.toLocaleString(
					undefined,
					{ minimumFractionDigits: 2,
						maximumFractionDigits: 2 }
				));
				
			self.dailyChart = c3.generate({
			    bindto: '#dailyChart',
			    data: {
			    	x: 'x',
			    	columns: [
			    		self.dx,
			    		self.dailyNetSales,
			    		self.dailyProfit
			    	]
			    },
			    axis: c3Utility.getDefaultAxis()
			});
		});
	};
	
    return ProductStats;
});