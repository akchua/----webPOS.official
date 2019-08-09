define(['plugins/dialog', 'durandal/app', 'knockout'], 
		function (dialog, app, ko) {
	var InvalidBarcode = function(barcodeKey) {
		this.barcodeKey = barcodeKey;
		
		this.content = ko.observable('');
		this.filter = ko.observable('');
	};
	
	InvalidBarcode.prototype.activate = function() {
		var self = this;
		
		self.content(self.barcodeKey);
	};
	
	InvalidBarcode.show = function(barcodeKey) {
		return dialog.show(new InvalidBarcode(barcodeKey));
	};
	
	InvalidBarcode.prototype.addContent = function() {
		var self = this;
		
		self.content(self.content() + ' - ' + self.filter());
		self.filter('');
	};
	
	InvalidBarcode.prototype.done = function() {
        dialog.close(this);
    };
	
    return InvalidBarcode;
});