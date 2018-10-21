define(['jquery'], function ($) {
    return {
    	getCompanyMTDPurchaseSummaryList: function(companyId) {
    		return $.ajax({
    			url: '/services/mtdpurchasesummary/companymtdlist',
    			data: {
    				companyId: companyId
    			}
    		});
    	},
    	getMTDPurchaseSummaryList: function() {
    		return $.ajax({
    			url: '/services/mtdpurchasesummary/mtdlist'
    		});
    	}
    };
});