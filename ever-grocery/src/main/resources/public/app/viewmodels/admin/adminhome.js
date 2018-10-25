define(['jquery', 'd3', 'c3', 'durandal/app', 'knockout', 'modules/mtdpurchasesummaryservice'], 
		function ($, d3, c3, app, ko, mtdPurchaseSummaryService) {
	var AdminHome = function() {
		this.currentYear = (new Date()).getFullYear();
		
		this.baseYear = ko.observable();
		this.yearErrorMessage = ko.observable();
		this.vsYear = ko.observable();
		
		this.chart = null;
		this.x = null;
		this.mtdGrossPurchase = null;
		this.mtdNetPurchase = null;
		
		this.baseGrossPurchases = null;
		this.baseNetPurchases = null;
		this.vsGrossPurchases = null;
		this.vsNetPurchases = null;
	};
	
	AdminHome.prototype.activate = function() {
		var self = this;
		
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
	
	AdminHome.prototype.compare = function() {
		var self = this;
		
		self.clearGraph();
		
		if(self.baseYear() >= 2016 && self.baseYear() <= self.currentYear && self.vsYear() >= 2016 && self.vsYear() <= self.currentYear) {
			mtdPurchaseSummaryService.getMTDPurchaseSummaryListByYear(self.baseYear()).done(function(baseMTDPurchaseSummaries) {
				mtdPurchaseSummaryService.getMTDPurchaseSummaryListByYear(self.vsYear()).done(function(vsMTDPurchaseSummaries) {
					self.baseGrossPurchases = [self.baseYear() + ' Gross Purchases', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
					self.baseNetPurchases = [self.baseYear() + ' Net Purchases', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
					self.vsGrossPurchases = [self.vsYear() + ' Gross Purchases', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
					self.vsNetPurchases = [self.vsYear() + ' Net Purchases', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
					self.x = ['x', 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
					
					for(var i = 0; i < baseMTDPurchaseSummaries.length; i++) {
						self.baseGrossPurchases[(baseMTDPurchaseSummaries[i].monthId % 12) + 1] = baseMTDPurchaseSummaries[i].grossTotal;
						self.baseNetPurchases[(baseMTDPurchaseSummaries[i].monthId % 12) + 1] = baseMTDPurchaseSummaries[i].netTotal;
					}
					
					for(var i = 0; i < vsMTDPurchaseSummaries.length; i++) {
						self.vsGrossPurchases[(vsMTDPurchaseSummaries[i].monthId % 12) + 1] = vsMTDPurchaseSummaries[i].grossTotal;
						self.vsNetPurchases[(vsMTDPurchaseSummaries[i].monthId % 12) + 1] = vsMTDPurchaseSummaries[i].netTotal;
					}
					
					self.chart = c3.generate({
					    bindto: '#chart',
					    data: {
					    	x: 'x',
					    	columns: [
					    		self.x,
					    		self.baseGrossPurchases,
					    		self.baseNetPurchases,
					    		self.vsGrossPurchases,
					    		self.vsNetPurchases
					    	],
					    	type: 'bar'
					    },
					    axis: {
					    	x: {
					    		type: 'category',
					    		tick: {
					    			rotate: -45,
					    			multiline: false
					    		},
					    		height: 80
					    	},
					    	y: {
					    		tick: {
					    			format: d3.format(",")
					    		}
					    	}
					    },
					    legend: {
					        position: 'right'
					    }
					});
				});
			});
		} else {
			mtdPurchaseSummaryService.getMTDPurchaseSummaryList().done(function(mtdList) {
				self.x = ['x'];
				self.mtdGrossPurchase = ['Monthly Gross Purchase'];
				self.mtdNetPurchase = ['Monthly Net Purchase'];
				
				for(var i = 0; i < mtdList.length; i++) {
					self.x[i + 1] = mtdList[i].formattedMonth;
					self.mtdGrossPurchase[i + 1] = mtdList[i].grossTotal;
					self.mtdNetPurchase[i + 1] = mtdList[i].netTotal;
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
				    axis: {
				    	x: {
				    		type: 'category',
				    		tick: {
				    			rotate: -45,
				    			multiline: false
				    		},
				    		height: 80
				    	},
				    	y: {
				    		tick: {
				    			format: d3.format(",")
				    		}
				    	}
				    },
				    legend: {
				        position: 'right'
				    }
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
		
		self.chart = null;
		self.x = null;
		self.mtdGrossPurchase = null;
		self.mtdNetPurchase = null;
		
		self.baseGrossPurchases = null;
		self.baseNetPurchases = null;
		self.vsGrossPurchases = null;
		self.vsNetPurchases = null;
	};
	
	AdminHome.prototype.attached = function() {
		var self = this;
		
		self.compare();
	};
	
    return AdminHome;
});