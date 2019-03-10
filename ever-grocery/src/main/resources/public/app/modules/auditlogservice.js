define(['jquery'], function ($) {
    return {
    	getAuditLogList: function(pageNumber, subjectId, async) {
    		return $.ajax({
    			url: '/services/auditlog/list',
    			async: async,
    			data: {
    				pageNumber: pageNumber - 1,
    				subjectId: subjectId
    			}
    		});
    	}
    };
});