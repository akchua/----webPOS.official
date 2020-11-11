define(['jquery'], function ($) {
    return {
    	getPromoList: function(pageNumber, showActiveOnly, async) {
    		return $.ajax({
    			url: '/services/promo/list',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				showActiveOnly: showActiveOnly
    			}
    		});
    	},
    	
    	getRecentlyEndedPromoList: function(pageNumber, async) {
    		return $.ajax({
    			url: '/services/promo/recentlyended',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1
    			}
    		});
    	},
    	
    	getPromo: function(promoId) {
    		return $.ajax({
    			url: '/services/promo/get',
    			data: {
    				promoId: promoId
    			}
    		});
    	},
    	
    	savePromo: function(promoFormData) {
    		return $.ajax({
    			url: '/services/promo/save',
    			method: 'POST',
    			data: {
    				promoFormData: promoFormData
    			}
    		});
    	},
    	
    	removePromo: function(promoId) {
    		return $.ajax({
    			url: '/services/promo/remove',
    			method: 'POST',
    			data: {
    				promoId: promoId
    			}
    		});
    	},
    	
    	generateCurrentPromoPDF: function() {
    		return $.ajax({
    			url: '/services/promo/currentpromo',
    			method: 'POST'
    		});
    	}
    };
});