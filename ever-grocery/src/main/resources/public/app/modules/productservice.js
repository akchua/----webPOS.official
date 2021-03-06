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
    	
    	getProductListWithCategory: function(pageNumber, searchKey, companyId, categoryId, async) {
    		return $.ajax({
    			url: '/services/product/listcategory',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey,
    				companyId: companyId,
    				categoryId: categoryId
    			}
    		});
    	},
    	
    	getProductListWithCategoryAndPromoFilter: function(pageNumber, searchKey, promo, companyId, categoryId, async) {
    		return $.ajax({
    			url: '/services/product/listcategorypromofilter',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey,
    				promo: promo,
    				companyId: companyId,
    				categoryId: categoryId
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
    	
    	getProductListByCategory: function(pageNumber, searchKey, categoryId, async) {
    		return $.ajax({
    			url: '/services/product/listbycategory',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				searchKey: searchKey,
    				categoryId: categoryId
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
    	
    	getProductWholeDetail: function(productId, async) {
    		return $.ajax({
    			url: '/services/product/getwholedetail',
    			async: async,
    			data: {
    				productId: productId
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