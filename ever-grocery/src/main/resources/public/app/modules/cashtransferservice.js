define(['jquery'], function ($) {
    return {
    	getCashTransfer: function(cashTransferId) {
    		return $.ajax({
    			url: '/services/cashtransfer/get',
    			data: {
    				cashTransferId: cashTransferId
    			}
    		});
    	},
    	
    	getMyCashTransfer: function(pageNumber) {
    		return $.ajax({
    			url: '/services/cashtransfer/mycashtransfer',
    			data: {
    				pageNumber: pageNumber - 1
    			}
    		});
    	},
    	
    	getCashTransferList: function(pageNumber, userId, status) {
    		return $.ajax({
    			url: '/services/cashtransfer/list',
    			data: {
    				pageNumber: pageNumber - 1,
    				userId: userId,
    				status: status
    			}
    		});
    	},
    	
    	requestCashTransfer: function(cashTransferFormData) {
    		return $.ajax({
    			url: '/services/cashtransfer/request',
    			method: 'POST',
    			data: {
    				cashTransferFormData: cashTransferFormData
    			}
    		});
    	},
    	
    	acceptCashTransfer: function(cashTransferId, auth) {
    		return $.ajax({
    			url: '/services/cashtransfer/accept',
    			method: 'POST',
    			data: {
    				cashTransferId: cashTransferId,
    				auth: auth
    			}
    		});
    	},
    	
    	cancelCashTransfer: function(cashTransferId) {
    		return $.ajax({
    			url: '/services/cashtransfer/cancel',
    			method: 'POST',
    			data: {
    				cashTransferId: cashTransferId
    			}
    		});
    	},
    	
    	auditUser: function(userId, fullAudit) {
    		return $.ajax({
    			url: '/services/cashtransfer/audit',
    			method: 'POST',
    			data: {
    				userId: userId,
    				fullAudit: fullAudit
    			}
    		});
    	}
    };
});