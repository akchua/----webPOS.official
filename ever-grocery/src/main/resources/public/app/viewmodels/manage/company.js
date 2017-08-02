define(['durandal/app', 'knockout', 'modules/companyservice', 'viewmodels/manage/companyform'], function (app, ko, companyService, CompanyForm) {
	var Company = function() {
		this.companyList = ko.observable();
		
		this.searchKey = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
	};
	
	
	Company.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCompanyList();
		});
		
		self.searchKey.subscribe(function(searchKey) {
			if(searchKey.length >= 3) {
				self.search();
			}
		});
		
		self.refreshCompanyList();
	};
	
	Company.prototype.refreshCompanyList = function() {
		var self = this;
		
		companyService.getCompanyList(self.currentPage(), self.searchKey()).done(function(data) {
			self.companyList(data.list);
			self.totalItems(data.total);
		});
	};
	
	Company.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshCompanyList();
	};
	
	Company.prototype.create = function() {
		var self = this;
		
		CompanyForm.show('Create', new Object()).then(function() {
			self.refreshCompanyList();
		});
	};
	
	Company.prototype.edit = function(companyId) {
		var self = this;
		
		companyService.getCompany(companyId).done(function(data) {
			CompanyForm.show('Update', data).then(function() {
				self.refreshCompanyList();
			});
		});
	};
	
	Company.prototype.remove = function(companyId, companyName) {
		var self = this;
		
		app.showMessage('Are you sure you want to remove Company "' + companyName + '"?',
				'Confirm Remove',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				companyService.removeCompany(companyId).done(function(result) {
					self.refreshCompanyList();
					app.showMessage(result.message);
				});
			}
		})
	};
	
    return Company;
});