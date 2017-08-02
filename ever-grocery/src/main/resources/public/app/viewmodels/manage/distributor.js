define(['durandal/app', 'knockout', 'modules/distributorservice', 'viewmodels/manage/distributorform'], function (app, ko, distributorService, DistributorForm) {
	var Distributor = function() {
		this.distributorList = ko.observable();
		
		this.searchKey = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
	};
	
	Distributor.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshDistributorList();
		});
		
		self.searchKey.subscribe(function(searchKey) {
			if(searchKey.length >= 3) {
				self.search();
			}
		});
		
		self.refreshDistributorList();
	};
	
	Distributor.prototype.refreshDistributorList = function() {
		var self = this;
		
		distributorService.getDistributorList(self.currentPage(), self.searchKey()).done(function(data) {
			self.distributorList(data.list);
			self.totalItems(data.total);
		});
	};
	
	Distributor.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshDistributorList();
	};
	
	Distributor.prototype.create = function() {
		var self = this;
		
		DistributorForm.show('Create', new Object()).then(function() {
			self.refreshDistributorList();
		});
	};
	
	Distributor.prototype.edit = function(distributorId) {
		var self = this;
		
		distributorService.getDistributor(distributorId).done(function(data) {
			DistributorForm.show('Update', data).then(function() {
				self.refreshDistributorList();
			});
		});
	};
	
	Distributor.prototype.remove = function(distributorId, distributorName) {
		var self = this;
		
		app.showMessage('Are you sure you want to remove Distributor "' + distributorName + '"?',
				'Confirm Remove',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				distributorService.removeDistributor(distributorId).done(function(result) {
					self.refreshDistributorList();
					app.showMessage(result.message);
				});
			}
		})
	};
	
    return Distributor;
});