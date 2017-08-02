define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/distributorservice'], function (dialog, app, ko, distributorService) {
    var DistributorForm = function(preTitle, distributor) {
        this.preTitle = preTitle;
        this.distributor = distributor;
        
        this.distributorFormModel = {
        	id: ko.observable(),
        	name: ko.observable(),
        	address: ko.observable(),
        	agent: ko.observable(),
        	phoneNumber: ko.observable()
        };
    };
    
    DistributorForm.prototype.activate = function() {
    	var self = this;
    	
    	self.distributorFormModel.id(self.distributor.id);
    	self.distributorFormModel.name(self.distributor.name);
    	self.distributorFormModel.address(self.distributor.address);
    	self.distributorFormModel.agent(self.distributor.agent);
    	self.distributorFormModel.phoneNumber(self.distributor.phoneNumber);
    };
 
    DistributorForm.show = function(preTitle, distributor) {
        return dialog.show(new DistributorForm(preTitle, distributor));
    };
 
    DistributorForm.prototype.save = function() {
    	var self = this;
    	
    	distributorService.saveDistributor(ko.toJSON(self.distributorFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    DistributorForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return DistributorForm;
});