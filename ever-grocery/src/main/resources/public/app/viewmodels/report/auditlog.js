define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/auditlogservice', 'modules/userservice'], 
		function (dialog, app, ko, auditLogService, userService) {
    var AuditLog = function() {
    	this.auditLogList = ko.observable();
    	this.userList = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.subjectId = ko.observable();
    };
    
    AuditLog.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshAuditLogList();
		});
    	
    	userService.getUserListOrderByName().done(function(userList) {
    		self.userList(userList);
    	});
    	
    	self.refreshAuditLogList();
    };
    
    AuditLog.show = function() {
    	return dialog.show(new AuditLog());
    };
    
    AuditLog.prototype.refreshAuditLogList = function() {
    	var self = this;
    	
    	auditLogService.getAuditLogList(self.currentPage(), self.subjectId(), false).done(function(data) { 
    		for(i = 0; i < data.list.length; i++) {
    			data.list[i].subjectOfAudit.imagePath = userService.getUserImageByFileName(data.list[i].subjectOfAudit.image);
    		}
			self.auditLogList(data.list);
			self.totalItems(data.total);
		});
    };
    
    AuditLog.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return AuditLog;
});