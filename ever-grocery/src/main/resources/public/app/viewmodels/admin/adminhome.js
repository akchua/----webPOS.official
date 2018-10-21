define(['jquery', 'd3', 'c3', 'durandal/app', 'knockout', 'modules/mtdpurchasesummaryservice'], 
		function ($, d3, c3, app, ko, mtdPurchaseSummaryService) {
	var AdminHome = function() {
		this.chart = null;
		this.x = ['x'];
		this.mtdGrossPurchase = ['MTD Gross Purchase'];
		this.mtdNetPurchase = ['MTD Net Purchase'];
	};
	
	AdminHome.prototype.activate = function() {
		var self = this;
	};
	
	AdminHome.prototype.attached = function() {
		var self = this;
		
		mtdPurchaseSummaryService.getMTDPurchaseSummaryList().done(function(mtdList) {
			var i = 0;
			for (i = 0; i < mtdList.length; i++) {
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
			    		height: 40
			    	},
			    	y: {
			    		tick: {
			    			format: d3.format(",")
			    		}
			    	}
			    }
			});
		});
	};
	
    return AdminHome;
});