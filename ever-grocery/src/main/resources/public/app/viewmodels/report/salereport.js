define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice', 'modules/fileservice'],
			function (dialog, app, ko, customerOrderService, fileService) {
    var SalesReport = function() {
    	this.enableGenerateButton = ko.observable(true);
    	
    	this.salesReportQuery = {
    		from: ko.observable(),
    		to: ko.observable(),
    		
    		sendMail: ko.observable(false)
	    };
    	
    	this.downloadFile = ko.observable(true);
    };
    
    SalesReport.show = function() {
    	return dialog.show(new SalesReport());
    };
    
    SalesReport.prototype.generateReport = function() {
    	var self = this;

    	self.enableGenerateButton(false);
    	customerOrderService.generateReport(ko.toJSON(self.salesReportQuery)).done(function(result) {
        	if(result.success) {
        		if(self.downloadFile()) {
        			fileService.downloadSalesReportByFileName(result.extras.fileName);
        		}
        		dialog.close(self);
        	} 
        	self.enableGenerateButton(true);
        	app.showMessage(result.message);
        });
    };
    
    SalesReport.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return SalesReport;
});