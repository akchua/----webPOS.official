define(['jquery'], function ($) {
    return {
    	getVersion: function() {
    		return $.ajax({
    			url: '/services/constants/version'
    		});
    	},
    	
    	getUserTypeList: function() {
    		return $.ajax({
    			url: '/services/constants/usertype'
    		});
    	},
    	
    	getCashTransferStatusList: function() {
    		return $.ajax({
    			url: '/services/constants/cashtransferstatus'
    		});
    	}
    };
});