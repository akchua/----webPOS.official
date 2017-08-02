define(['durandal/app', 'knockout', 'modules/categoryservice', 'viewmodels/manage/categoryform'], function (app, ko, categoryService, CategoryForm) {
	var Category = function() {
		this.categoryList = ko.observable();
		
		this.searchKey = ko.observable();
		
		this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
	};
	
	Category.prototype.activate = function() {
		var self = this;
		
		self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCategoryList();
		});
		
		self.searchKey.subscribe(function(searchKey) {
			if(searchKey.length >= 3) {
				self.search();
			}
		});
		
		self.refreshCategoryList();
	};
	
	Category.prototype.refreshCategoryList = function() {
		var self = this;
		
		categoryService.getCategoryList(self.currentPage(), self.searchKey()).done(function(data) {
			self.categoryList(data.list);
			self.totalItems(data.total);
		});
	};
	
	Category.prototype.search = function() {
		var self = this;
		
		self.currentPage(1);
		self.refreshCategoryList();
	};
	
	Category.prototype.create = function() {
		var self = this;
		
		CategoryForm.show('Create', new Object()).then(function() {
			self.refreshCategoryList();
		});
	};
	
	Category.prototype.edit = function(categoryId) {
		var self = this;
		
		categoryService.getCategory(categoryId).done(function(data) {
			CategoryForm.show('Update', data).then(function() {
				self.refreshCategoryList();
			});
		});
	};
	
	Category.prototype.remove = function(categoryId, categoryName) {
		var self = this;
		
		app.showMessage('Are you sure you want to remove Category "' + categoryName + '"?',
				'Confirm Remove',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				categoryService.removeCategory(categoryId).done(function(result) {
					self.refreshCategoryList();
					app.showMessage(result.message);
				});
			}
		})
	};
	
    return Category;
});