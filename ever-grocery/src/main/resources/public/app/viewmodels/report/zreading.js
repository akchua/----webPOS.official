define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice'],
			function (dialog, app, ko, customerOrderService) {
    var ZReading = function() {
    	this.enablePrintButton = ko.observable(true);
    	
    	this.readingDate = ko.observable();
    };
    
    ZReading.show = function() {
    	return dialog.show(new ZReading());
    };
    
    ZReading.prototype.printZReading = function() {
    	var self = this;

    	self.enablePrintButton(false);
    	customerOrderService.printZReading(self.readingDate()).done(function(result) {
        	if(result.success) dialog.close(self);
        	if(result.message) app.showMessage(result.message);
        	self.enablePrintButton(true);
        });
    };
    
    ZReading.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ZReading;
});