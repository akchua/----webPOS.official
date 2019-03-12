define(['jquery', 'c3', 'durandal/app', 'knockout', 'modules/transactionsummaryservice', 'modules/c3utility'], 
		function ($, c3, app, ko, transactionSummaryService, c3Utility) {
	var CompanyStats = function() {
		this.companyId = ko.observable();
		
		this.chart = null;
		this.x = ['x'];
		this.mtdGrossPurchase = ['Monthly Gross Purchase'];
		this.mtdNetPurchase = ['Monthly Net Purchase'];
		this.mtdNetSales = ['Monthly Net Sales'];
		this.mtdProfit = ['Monthly Profit'];
	};
	
	CompanyStats.prototype.activate = function(activationData) {
		var self = this;
		
		self.companyId = activationData.companyId;
	};
	
	CompanyStats.prototype.attached = function() {
		var self = this;
		
		transactionSummaryService.getCompanyMTDPurchaseSummaryList(self.companyId()).done(function(companyMTDPurchaseList) {
			var i = 0;
			for (i = 0; i < companyMTDPurchaseList.length; i++) {
				self.x[i + 1] = companyMTDPurchaseList[i].formattedMonth;
				self.mtdGrossPurchase[i + 1] = companyMTDPurchaseList[i].grossTotal;
				self.mtdNetPurchase[i + 1] = companyMTDPurchaseList[i].netTotal;
			}
			
			transactionSummaryService.getCompanyMTDSalesSummaryList(self.companyId()).done(function(companyMTDSalesList) {
				var j = 0;
				var k = 0;
				for (j = 0; j < companyMTDPurchaseList.length; j++) {
					if(companyMTDSalesList[k].formattedMonth != self.x[j + 1]) {
						self.mtdNetSales[j + 1] = 0;
						self.mtdProfit[j + 1] = 0;
					} else {
						self.mtdNetSales[j + 1] = companyMTDSalesList[k].netTotal;
						self.mtdProfit[j + 1] = companyMTDSalesList[k].profit;
						k++;
					}
				}
				
				self.chart = c3.generate({
				    bindto: '#chart',
				    data: {
				    	x: 'x',
				    	columns: [
				    		self.x,
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
	};
	
    return CompanyStats;
});