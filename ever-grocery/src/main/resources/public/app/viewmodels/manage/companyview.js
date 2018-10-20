define(['plugins/dialog', 'knockout'], 
			function (dialog, ko) {
    var CompanyView = function(company) {
    	this.company = company;
    	
    	this.companyId = ko.observable();
    	this.companyName = ko.observable();
    };
    
    CompanyView.prototype.activate = function() {
    	var self = this;
    	
    	self.companyId(self.company.id);
    	self.companyName(self.company.name);
    };
    
    CompanyView.show = function(company) {
    	return dialog.show(new CompanyView(company));
    };
    
    CompanyView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return CompanyView;
});