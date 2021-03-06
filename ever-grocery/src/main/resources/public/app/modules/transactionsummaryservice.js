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
    	},
    	
    	getCompanyMTDSalesSummaryList: function(companyId) {
    		return $.ajax({
    			url: '/services/transactionsummary/companymtdsaleslist',
    			data: {
    				companyId: companyId
    			}
    		});
    	},
    	
    	getMTDSalesSummaryList: function() {
    		return $.ajax({
    			url: '/services/transactionsummary/mtdsaleslist'
    		});
    	},
    	
    	getMTDSalesSummaryListByYear: function(year) {
    		return $.ajax({
    			url: '/services/transactionsummary/mtdsaleslistbyyear',
    			data: {
    				year: year
    			}
    		});
    	},
    	
    	getDailySalesSummaryList: function(daysAgo) {
    		return $.ajax({
    			url: '/services/transactionsummary/dailysaleslist',
    			data: {
    				daysAgo: daysAgo
    			}
    		});
    	},
    	
    	getProductDailySalesSummaryList: function(productId, daysAgo) {
    		return $.ajax({
    			url: '/services/transactionsummary/productdailysaleslist',
    			data: {
    				productId: productId,
    				daysAgo: daysAgo
    			}
    		});
    	},
    	
    	getLiveSalesSummary: function() {
    		return $.ajax({
    			url: '/services/transactionsummary/livesales'
    		});
    	},
    	
    	getPaidCountToday: function() {
    		return $.ajax({
    			url: '/services/transactionsummary/paidcount'
    		});
    	}
    };
});