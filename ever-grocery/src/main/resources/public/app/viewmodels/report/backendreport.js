define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice', 'modules/fileservice'],
			function (dialog, app, ko, customerOrderService, fileService) {
    var BackendReport = function() {
    	this.enableGenerateButton = ko.observable(true);
    	
		this.dateFrom = ko.observable();
		this.dateTo = ko.observable();
    };
    
    BackendReport.show = function() {
    	return dialog.show(new BackendReport());
    };
    
    BackendReport.prototype.generateBackendReport = function() {
    	var self = this;

    	self.enableGenerateButton(false);
    	customerOrderService.generateBackendReport(self.dateFrom(), self.dateTo()).done(function(result) {
        	if(result.success) {
    			fileService.downloadBackendReportByFileName(result.extras.fileName);
        		dialog.close(self);
        	} 
        	self.enableGenerateButton(true);
        	app.showMessage(result.message);
        });
    };
    
    BackendReport.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return BackendReport;
});