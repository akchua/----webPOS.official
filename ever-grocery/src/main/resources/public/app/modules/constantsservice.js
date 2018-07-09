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
    	
    	getCashTransferStatusList: function() {
    		return $.ajax({
    			url: '/services/constants/cashtransferstatus'
    		});
    	}
    };
});