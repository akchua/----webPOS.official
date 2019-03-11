define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/auditlogservice', 'modules/userservice'], 
		function (dialog, app, ko, auditLogService, userService) {
    var RecentTransactions = function() {
    	this.auditLogList = ko.observable();
    	
		this.subjectId = ko.observable();
    };
    
    RecentTransactions.prototype.activate = function() {
    	var self = this;
    	
    	auditLogService.getAuditLogList(1, app.user.id, false).done(function(data) { 
			self.auditLogList(data.list);
		});
    };
    
    RecentTransactions.show = function() {
    	return dialog.show(new RecentTransactions());
    };
    
    RecentTransactions.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return RecentTransactions;
});