define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice'/*, 'modules/brandservice'*/, 'modules/categoryservice', 'modules/companyservice', 'modules/constantsservice', 'viewmodels/manage/productdetailsform'], 
		function (dialog, app, ko, productService/*, brandService*/, categoryService, companyService, constantsService, ProductDetailsForm) {
    var ProductForm = function(preTitle, product) {
        this.preTitle = preTitle;
        this.product = product;
        
        this.productFormModel = {
        	id: ko.observable(),
        	name: ko.observable(),
        	displayName: ko.observable(),
        	code: ko.observable(),
        	/*brandId: ko.observable(),*/
        	categoryId: ko.observable(),
        	companyId: ko.observable(),
        	
        	promo: ko.observable(false),
        	
        	taxType: ko.observable(),
        	allowSeniorDiscount: ko.observable(false),
        	allowPWDDiscount: ko.observable(false)
        };
        
        this.errors = {
    		name: ko.observable(),
        	displayName: ko.observable(),
        	code: ko.observable(),
        	category: ko.observable(),
        	company: ko.observable()
    	};
        
        this.brandList = ko.observable();
        this.categoryList = ko.observable();
        this.companyList = ko.observable();
        this.taxTypeList = ko.observable();
    };
 
    ProductForm.prototype.activate = function() {
    	var self = this;
    	
    	self.productFormModel.id(self.product.id);
    	self.productFormModel.name(self.product.name);
    	self.productFormModel.displayName(self.product.displayName);
    	self.productFormModel.code(self.product.code);
    	self.productFormModel.promo(self.product.promo);
    	self.productFormModel.allowSeniorDiscount(self.product.allowSeniorDiscount);
    	self.productFormModel.allowPWDDiscount(self.product.allowPWDDiscount);
    	
    	/*brandService.getBrandListByName().done(function(brandList) {
    		self.brandList(brandList);
    		self.productFormModel.brandId(self.product.brand.id);
    	});*/
    	
    	categoryService.getCategoryListByName().done(function(categoryList) {
    		self.categoryList(categoryList);
    		self.productFormModel.categoryId(self.product.category.id);
    	});
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    		self.productFormModel.companyId(self.product.company.id);
    	});
    	
    	constantsService.getTaxTypeList().done(function(taxTypeList) {
    		self.taxTypeList(taxTypeList);
    	});
    	
    	if(self.product && self.product.taxType) {
    		self.productFormModel.taxType(self.product.taxType.name);
    	} else {
    		constantsService.getDefaultTaxType().done(function(defaultTaxType) {
        		self.productFormModel.taxType(defaultTaxType.name);
        	});
    	}
    };
    
    ProductForm.show = function(preTitle, product) {
        return dialog.show(new ProductForm(preTitle, product));
    };
    
    ProductForm.prototype.save = function() {
    	var self = this;
    	
        productService.saveProduct(ko.toJSON(self.productFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);
        		
        		productService.getProduct(result.extras.productId).done(function(data) {
        			productService.getProductDetailList(result.extras.productId).done(function(data2) {
        				ProductDetailsForm.show(data, data2)
        			});
        		});
        	} else {
        		self.errors.name(result.extras.errors.name);
        		self.errors.displayName(result.extras.errors.displayName);
        		self.errors.code(result.extras.errors.code);
        		self.errors.category(result.extras.errors.category);
        		self.errors.company(result.extras.errors.company);
        		
        		if(result.message) app.showMessage(result.message);
        	}
        });
    };
    
    ProductForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ProductForm;
});