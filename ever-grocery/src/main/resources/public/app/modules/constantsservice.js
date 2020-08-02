define(['jquery'], function ($) {
    return {
    	getVersion: function() {
    		return $.ajax({
    			url: '/services/constants/version'
    		});
    	},
    	
    	getDefaultTaxType: function() {
    		return $.ajax({
    			url: '/services/constants/taxtype'
    		});
    	},
    	
    	getUserTypeList: function() {
    		return $.ajax({
    			url: '/services/constants/usertype'
    		});
    	},
    	
    	getTaxTypeList: function() {
    		return $.ajax({
    			url: '/services/constants/taxtypelist'
    		});
    	},
    	
    	getDiscountTypeList: function() {
    		return $.ajax({
    			url: '/services/constants/discounttypelist'
    		});
    	},
    	
    	getPromoTypeList: function() {
    		return $.ajax({
    			url: '/services/constants/promotypelist'
    		});
    	},
    	
    	getReportTypeList: function() {
    		return $.ajax({
    			url: '/services/constants/reporttypelist'
    		});
    	},
    	
    	getCashTransferStatusList: function() {
    		return $.ajax({
    			url: '/services/constants/cashtransferstatus'
    		});
    	}
    };
});