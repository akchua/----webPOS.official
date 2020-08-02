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
    	}
    };
});