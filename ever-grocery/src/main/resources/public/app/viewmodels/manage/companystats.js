define(['jquery', 'c3', 'durandal/app', 'knockout', 'modules/mtdpurchasesummaryservice', 'modules/c3utility'], 
		function ($, c3, app, ko, mtdPurchaseSummaryService, c3Utility) {
	var CompanyStats = function() {
		this.companyId = ko.observable();
		
		this.chart = null;
		this.x = ['x'];
		this.mtdGrossPurchase = ['Monthly Gross Purchase'];
		this.mtdNetPurchase = ['Monthly Net Purchase'];
	};
	
	CompanyStats.prototype.activate = function(activationData) {
		var self = this;
		
		self.companyId = activationData.companyId;
	};
	
	CompanyStats.prototype.attached = function() {
		var self = this;
		
		mtdPurchaseSummaryService.getCompanyMTDPurchaseSummaryList(self.companyId()).done(function(companyMTDList) {
			var i = 0;
			for (i = 0; i < companyMTDList.length; i++) {
				self.x[i + 1] = companyMTDList[i].formattedMonth;
				self.mtdGrossPurchase[i + 1] = companyMTDList[i].grossTotal;
				self.mtdNetPurchase[i + 1] = companyMTDList[i].netTotal;
			}
			
			self.chart = c3.generate({
			    bindto: '#chart',
			    data: {
			    	x: 'x',
			    	columns: [
			    		self.x,
			    		self.mtdGrossPurchase,
			    		self.mtdNetPurchase
			    	]
			    },
			    axis: c3Utility.getDefaultAxis()
			});
		});
	};
	
    return CompanyStats;
});