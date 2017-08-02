define(['jquery'], function ($) {
    return {
    	getCategoryList: function(pageNumber, searchKey) {
    		return $.ajax({
    			url: '/services/category/list',
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey
    			}
    		});
    	},
    	
    	getCategory: function(categoryId) {
    		return $.ajax({
    			url: '/services/category/get',
    			data: {
    				categoryId: categoryId
    			}
    		});
    	},
    	
    	saveCategory: function(categoryFormData) {
    		return $.ajax({
    			url: '/services/category/save',
    			method: 'POST',
    			data: {
    				categoryFormData: categoryFormData
    			}
    		});
    	},
    	
    	removeCategory: function(categoryId) {
    		return $.ajax({
    			url: '/services/category/remove',
    			method: 'POST',
    			data: {
    				categoryId: categoryId
    			}
    		});
    	},
    	
    	getCategoryListByName: function() {
    		return $.ajax({
    			url: '/services/category/listbyname'
    		});
    	}
    };
});