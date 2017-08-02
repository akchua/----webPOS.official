define(['jquery'], function ($) {
    return {
    	getUserList: function(pageNumber, searchKey) {
    		return $.ajax({
    			url: '/services/user/list',
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey
    			}
    		});
    	},
    	
    	getUser: function(userId) {
    		return $.ajax({
    			url: '/services/user/get',
    			data: {
    				userId: userId
    			}
    		});
    	},
    	
    	saveUser: function(userFormData) {
    		return $.ajax({
    			url: '/services/user/save',
    			method: 'POST',
    			data: {
    				userFormData: userFormData
    			}
    		});
    	},
    	
    	removeUser: function(userId) {
    		return $.ajax({
    			url: '/services/user/remove',
    			method: 'POST',
    			data: {
    				userId: userId
    			}
    		});
    	},
    	
    	getUserListByUsername: function() {
    		return $.ajax({
    			url: '/services/user/listbyusername'
    		});
    	},
    	
    	getUserTypeList: function() {
    		return $.ajax({
    			url: '/services/user/listusertype'
    		});
    	}
    };
});