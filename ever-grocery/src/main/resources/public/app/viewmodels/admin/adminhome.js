define(['jquery', 'c3', 'durandal/app', 'knockout', 'modules/transactionsummaryservice', 'modules/c3utility'], 
		function ($, c3, app, ko, transactionSummaryService, c3Utility) {
	var AdminHome = function() {
		this.currentYear = (new Date()).getFullYear();
		
		this.baseYear = ko.observable();
		this.yearErrorMessage = ko.observable();
		this.vsYear = ko.observable();
		
		this.monthlyChart = null;
		this.mx = null;
		this.mtdGrossPurchase = null;
		this.mtdNetPurchase = null;
		this.mtdNetSales = null;
		this.mtdProfit = null;
		
		this.baseNetPurchases = null;
		this.baseNetSales = null;
		this.baseProfit = null;
		this.vsNetPurchases = null;
		this.vsNetSales = null;
		this.vsProfit = null;
		
		this.daysAgo = ko.observable(90);
		
		this.dailyChart = null;
		this.dx = null;
		this.dailyNetSales = null;
		this.dailyProfit = null;
		this.averageNetSales = ko.observable(0.0);
		this.averageProfit = ko.observable(0.0);
		
		this.completedSales = ko.observable();
		this.totalSales = ko.observable();
		this.totalProfit = ko.observable();
		
		this.time = ko.observable();
	};
	
	AdminHome.prototype.activate = function() {
		var self = this;
		
		self.daysAgo.subscribe(function() {
			self.refreshDailySalesGraph();
		});
		
		self.refreshLiveSales();
		
		self.baseYear.subscribe(function(newValue) {
			if(newValue < 2016 || newValue > self.currentYear || self.vsYear() < 2016 || self.vsYear() > self.currentYear) {
				self.yearErrorMessage("Valid years : 2016 - " + self.currentYear + " only.")
			} else {
				self.yearErrorMessage('');
			}
		});
		
		self.vsYear.subscribe(function(newValue) {
			if(newValue < 2016 || newValue > self.currentYear || self.baseYear() < 2016 || self.baseYear() > self.currentYear) {
				self.yearErrorMessage("Valid years : 2016 - " + self.currentYear + " only.")
			} else {
				self.yearErrorMessage('');
			}
		});
	};
	
	AdminHome.prototype.refreshLiveSales = function() {
		var self = this;
		
		var today = new Date();
		self.time(today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds());
		
		transactionSummaryService.getPaidCountToday().done(function(paidCount) {
			self.completedSales(paidCount);
		});
		
		transactionSummaryService.getLiveSalesSummary().done(function(liveSalesSummary) {
			self.totalSales(liveSalesSummary.netTotal);
			self.totalProfit(liveSalesSummary.totalProfit);
		});
	};
	
	AdminHome.prototype.refreshDailySalesGraph = function() {
		var self = this;
		
		self.clearDailySalesGraph();
		
		transactionSummaryService.getDailySalesSummaryList(self.daysAgo()).done(function(dailySalesList) {
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
	
	AdminHome.prototype.compare = function() {
		var self = this;
		
		self.clearGraph();
		
		if(self.baseYear() >= 2016 && self.baseYear() <= self.currentYear && self.vsYear() >= 2016 && self.vsYear() <= self.currentYear) {
			transactionSummaryService.getMTDPurchaseSummaryListByYear(self.baseYear()).done(function(baseMTDPurchaseSummaries) {
				transactionSummaryService.getMTDPurchaseSummaryListByYear(self.vsYear()).done(function(vsMTDPurchaseSummaries) {
					transactionSummaryService.getMTDSalesSummaryListByYear(self.baseYear()).done(function(baseMTDSalesSummaries) {
						transactionSummaryService.getMTDSalesSummaryListByYear(self.vsYear()).done(function(vsMTDSalesSummaries) {
							self.baseNetPurchases = [self.baseYear() + ' Net Purchases', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
							self.baseNetSales = [self.baseYear() + ' Net Sales', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
							self.baseProfit = [self.baseYear() + ' Profit', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
							self.vsNetPurchases = [self.vsYear() + ' Net Purchases', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
							self.vsNetSales = [self.vsYear() + ' Net Sales', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
							self.vsProfit = [self.vsYear() + ' Profit', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
							self.mx = ['x', 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
							
							for(var i = 0; i < baseMTDPurchaseSummaries.length; i++) {
								self.baseNetPurchases[(baseMTDPurchaseSummaries[i].monthId % 12) + 1] = baseMTDPurchaseSummaries[i].netTotal;
							}
							
							for(var i = 0; i < vsMTDPurchaseSummaries.length; i++) {
								self.vsNetPurchases[(vsMTDPurchaseSummaries[i].monthId % 12) + 1] = vsMTDPurchaseSummaries[i].netTotal;
							}
							
							for(var i = 0; i < baseMTDSalesSummaries.length; i++) {
								self.baseNetSales[(baseMTDSalesSummaries[i].monthId % 12) + 1] = baseMTDSalesSummaries[i].netTotal;
								self.baseProfit[(baseMTDSalesSummaries[i].monthId % 12) + 1] = baseMTDSalesSummaries[i].profit;
							}
							
							for(var i = 0; i < vsMTDSalesSummaries.length; i++) {
								self.vsNetSales[(vsMTDSalesSummaries[i].monthId % 12) + 1] = vsMTDSalesSummaries[i].netTotal;
								self.vsProfit[(vsMTDSalesSummaries[i].monthId % 12) + 1] = vsMTDSalesSummaries[i].profit;
							}
							
							self.monthlyChart = c3.generate({
							    bindto: '#monthlyChart',
							    data: {
							    	x: 'x',
							    	columns: [
							    		self.mx,
							    		self.baseNetPurchases,
							    		self.vsNetPurchases,
							    		self.baseNetSales,
							    		self.vsNetSales,
							    		self.baseProfit,
							    		self.vsProfit
							    	],
							    	type: 'bar'
							    },
							    axis: c3Utility.getDefaultAxis()
							});
						});
					});
				});
			});
		} else {
			transactionSummaryService.getMTDPurchaseSummaryList().done(function(MTDPurchaseList) {
				self.mx = ['x'];
				self.mtdGrossPurchase = ['Monthly Gross Purchase'];
				self.mtdNetPurchase = ['Monthly Net Purchase'];
				self.mtdNetSales = ['Monthly Net Sales'];
				self.mtdProfit = ['Monthly Profit'];
				
				for(var i = 0; i < MTDPurchaseList.length; i++) {
					self.mx[i + 1] = MTDPurchaseList[i].formattedMonth;
					self.mtdGrossPurchase[i + 1] = MTDPurchaseList[i].grossTotal;
					self.mtdNetPurchase[i + 1] = MTDPurchaseList[i].netTotal;
				}
				
				transactionSummaryService.getMTDSalesSummaryList().done(function(MTDSalesList) {
					var j = 0;
					var k = 0;
					for (j = 0; j < MTDPurchaseList.length; j++) {
						if(MTDSalesList[k].formattedMonth != self.mx[j + 1]) {
							self.mtdNetSales[j + 1] = 0;
							self.mtdProfit[j + 1] = 0;
						} else {
							self.mtdNetSales[j + 1] = MTDSalesList[k].netTotal;
							self.mtdProfit[j + 1] = MTDSalesList[k].profit;
							k++;
						}
					}
					
					self.monthlyChart = c3.generate({
					    bindto: '#monthlyChart',
					    data: {
					    	x: 'x',
					    	columns: [
					    		self.mx,
					    		self.mtdGrossPurchase,
					    		self.mtdNetPurchase,
					    		self.mtdNetSales,
					    		self.mtdProfit
					    	]
					    },
					    axis: c3Utility.getDefaultAxis()
					});
				});
			});
		}
	};
	
	AdminHome.prototype.refresh = function() {
		var self = this;
		
		self.baseYear('');
		self.vsYear('');
		self.compare();
	};
	
	AdminHome.prototype.clearGraph = function() {
		var self = this;
		
		self.monthlyChart = null;
		self.mx = null;
		self.mtdGrossPurchase = null;
		self.mtdNetPurchase = null;
		
		self.baseGrossPurchases = null;
		self.baseNetPurchases = null;
		self.vsGrossPurchases = null;
		self.vsNetPurchases = null;
	};
	
	AdminHome.prototype.clearDailySalesGraph = function() {
		var self = this;
		
		self.dailyChart = null;
		self.dx = null;
		self.dailyNetPurchase = null;
		self.dailyProfit = null;
	};
	
	AdminHome.prototype.attached = function() {
		var self = this;
		
		self.compare();
		self.refreshDailySalesGraph();
	};
	
    return AdminHome;
});