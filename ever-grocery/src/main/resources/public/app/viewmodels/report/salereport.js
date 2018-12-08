define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice', 'modules/fileservice', 'modules/constantsservice'],
			function (dialog, app, ko, customerOrderService, fileService, constantsService) {
    var SalesReport = function() {
    	this.enableGenerateButton = ko.observable(true);
    	
    	this.discountTypeList = ko.observable();
    	
    	this.salesReportQuery = {
    		from: ko.observable(),
    		to: ko.observable(),
    		discountType: ko.observable(),
    		
    		sendMail: ko.observable(false)
	    };
    	
    	this.downloadFile = ko.observable(true);
    };
    
    SalesReport.show = function() {
    	return dialog.show(new SalesReport());
    };
    
    SalesReport.prototype.activate = function() {
    	var self = this;
    	
    	constantsService.getDiscountTypeList().done(function(discountTypeList) {
    		self.discountTypeList(discountTypeList);
    	});
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