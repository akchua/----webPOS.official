define(['jquery'], function ($) {
    return {
    	getCompanyList: function(pageNumber, searchKey) {
    		return $.ajax({
    			url: '/services/company/list',
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey
    			}
    		});
    	},
    	
    	getCompany: function(companyId) {
    		return $.ajax({
    			url: '/services/company/get',
    			data: {
    				companyId: companyId
    			}
    		});
    	},
    	
    	saveCompany: function(companyFormData) {
    		return $.ajax({
    			url: '/services/company/save',
    			method: 'POST',
    			data: {
    				companyFormData: companyFormData
    			}
    		});
    	},
    	
    	removeCompany: function(companyId) {
    		return $.ajax({
    			url: '/services/company/remove',
    			method: 'POST',
    			data: {
    				companyId: companyId
    			}
    		});
    	},
    	
    	getCompanyListByName: function() {
    		return $.ajax({
    			url: '/services/company/listbyname'
    		});
    	},
    	
    	getReceiptTypeList: function() {
    		return $.ajax({
    			url: '/services/company/listreceipttype'
    		});
    	}
    };
});