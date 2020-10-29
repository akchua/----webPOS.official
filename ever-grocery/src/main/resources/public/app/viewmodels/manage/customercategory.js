define(['durandal/app', 'knockout', 'modules/customercategoryservice', 'viewmodels/manage/customercategoryform', 'viewmodels/manage/customercategoryview'], 
			function (app, ko, customerCategoryService, CustomerCategoryForm, CustomerCategoryView) {
	var CustomerCategory = function() {
		this.customerCategoryList = ko.observable();
		
		this.searchKey = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
	};
	
	CustomerCategory.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCustomerCategoryList();
		});
		
		self.searchKey.subscribe(function(searchKey) {
			if(searchKey.length >= 3) {
				self.search();
			}
		});
		
		self.refreshCustomerCategoryList();
	};
	
	CustomerCategory.prototype.refreshCustomerCategoryList = function() {
		var self = this;
		
		customerCategoryService.getCustomerCategoryList(self.currentPage(), self.searchKey()).done(function(data) {
			self.customerCategoryList(data.list);
			self.totalItems(data.total);
		});
	};
	
	CustomerCategory.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshCustomerCategoryList();
	};
	
	CustomerCategory.prototype.create = function() {
		var self = this;
		
		CustomerCategoryForm.show('Create', new Object()).then(function() {
			self.refreshCustomerCategoryList();
		});
	};
	
	CustomerCategory.prototype.view = function(customerCategoryId) {
    	var self = this;
    	
    	customerCategoryService.getCustomerCategory(customerCategoryId).done(function(customerCategory) {
    		CustomerCategoryView.show(customerCategory)
    	});
    };
	
	CustomerCategory.prototype.edit = function(customerCategoryId) {
		var self = this;
		
		customerCategoryService.getCustomerCategory(customerCategoryId).done(function(data) {
			CustomerCategoryForm.show('Update', data).then(function() {
				self.refreshCustomerCategoryList();
			});
		});
	};
	
	CustomerCategory.prototype.remove = function(customerCategoryId, customerCategoryName) {
		var self = this;
		
		app.showMessage('Are you sure you want to remove Customer Category "' + customerCategoryName + '"?',
				'Confirm Remove',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerCategoryService.removeCustomerCategory(customerCategoryId).done(function(result) {
					self.refreshCustomerCategoryList();
					app.showMessage(result.message);
				});
			}
		})
	};
	
    return CustomerCategory;
});