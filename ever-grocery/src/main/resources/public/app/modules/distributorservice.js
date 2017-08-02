define(['jquery'], function ($) {
    return {
    	getDistributorList: function(pageNumber, searchKey) {
    		return $.ajax({
    			url: '/services/distributor/list',
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey
    			}
    		});
    	},
    	
    	getDistributor: function(distributorId) {
    		return $.ajax({
    			url: '/services/distributor/get',
    			data: {
    				distributorId: distributorId
    			}
    		});
    	},
    	
    	saveDistributor: function(distributorFormData) {
    		return $.ajax({
    			url: '/services/distributor/save',
    			method: 'POST',
    			data: {
    				distributorFormData: distributorFormData
    			}
    		});
    	},
    	
    	removeDistributor: function(distributorId) {
    		return $.ajax({
    			url: '/services/distributor/remove',
    			method: 'POST',
    			data: {
    				distributorId: distributorId
    			}
    		});
    	},
    	
    	getDistributorListByName: function() {
    		return $.ajax({
    			url: '/services/distributor/listbyname'
    		});
    	}
    };
});