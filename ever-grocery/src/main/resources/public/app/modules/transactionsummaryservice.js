define(['jquery'], function ($) {
    return {
    	getCompanyMTDPurchaseSummaryList: function(companyId) {
    		return $.ajax({
    			url: '/services/transactionsummary/companymtdpurchaselist',
    			data: {
    				companyId: companyId
    			}
    		});
    	},
    	
    	getMTDPurchaseSummaryList: function() {
    		return $.ajax({
    			url: '/services/transactionsummary/mtdpurchaselist'
    		});
    	},
    	
    	getMTDPurchaseSummaryListByYear: function(year) {
    		return $.ajax({
    			url: '/services/transactionsummary/mtdpurchaselistbyyear',
    			data: {
    				year: year
    			}
    		});
    	}
    };
});