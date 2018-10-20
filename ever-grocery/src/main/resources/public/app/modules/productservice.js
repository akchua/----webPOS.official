define(['jquery'], function ($) {
    return {
    	getProductList: function(pageNumber, searchKey, companyId, async) {
    		return $.ajax({
    			url: '/services/product/list',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey,
    				companyId: companyId
    			}
    		});
    	},
    	
    	getProductListByRank: function(pageNumber, searchKey, companyId, async) {
    		return $.ajax({
    			url: '/services/product/listbyrank',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey,
    				companyId: companyId
    			}
    		});
    	},
    	
    	getSalePriceHistoryList: function(pageNumber, async) {
    		return $.ajax({
    			url: '/services/product/salepricehistory',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1
    			}
    		});
    	},
    	
    	getProduct: function(productId) {
    		return $.ajax({
    			url: '/services/product/get',
    			data: {
    				productId: productId
    			}
    		});
    	},
    	
    	saveProduct: function(productFormData) {
    		return $.ajax({
    			url: '/services/product/save',
    			method: 'POST',
    			data: {
    				productFormData: productFormData
    			}
    		});
    	},
    	
    	removeProduct: function(productId) {
    		return $.ajax({
    			url: '/services/product/remove',
    			method: 'POST',
    			data: {
    				productId: productId
    			}
    		});
    	},
    	
    	saveProductDetails: function(productId, productDetailsWholeFormData, productDetailsPieceFormData, productDetailsInnerPieceFormData, productDetailsSecondInnerPieceFormData) {
    		return $.ajax({
    			url: '/services/product/savedetails',
    			method: 'POST',
    			data: {
    				productId: productId,
    				productDetailsWholeFormData: productDetailsWholeFormData,
    				productDetailsPieceFormData: productDetailsPieceFormData,
    				productDetailsInnerPieceFormData: productDetailsInnerPieceFormData,
    				productDetailsSecondInnerPieceFormData: productDetailsSecondInnerPieceFormData
    			}
    		});
    	},
    	
    	getProductDetailList: function(productId, async) {
    		return $.ajax({
    			url: '/services/product/getdetails',
    			async: async,
    			data: {
    				productId: productId
    			}
    		});
    	},
    	
    	getUnitTypeList: function() {
    		return $.ajax({
    			url: '/services/product/listunittype'
    		});
    	}
    };
});