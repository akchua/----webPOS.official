define(['jquery'], function ($) {
    return {
    	getBrandList: function(pageNumber, searchKey) {
    		return $.ajax({
    			url: '/services/brand/list',
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey
    			}
    		});
    	},
    	
    	getBrand: function(brandId) {
    		return $.ajax({
    			url: '/services/brand/get',
    			data: {
    				brandId: brandId
    			}
    		});
    	},
    	
    	saveBrand: function(brandFormData) {
    		return $.ajax({
    			url: '/services/brand/save',
    			method: 'POST',
    			data: {
    				brandFormData: brandFormData
    			}
    		});
    	},
    	
    	removeBrand: function(brandId) {
    		return $.ajax({
    			url: '/services/brand/remove',
    			method: 'POST',
    			data: {
    				brandId: brandId
    			}
    		});
    	},
    	
    	getBrandListByName: function() {
    		return $.ajax({
    			url: '/services/brand/listbyname'
    		});
    	}
    };
});