define(['plugins/dialog', 'durandal/app', 'knockout', 'viewmodels/manage/productdetails', 'modules/utility', 'modules/productservice', 'objects/productdetailsenableconfig'], function (dialog, app, ko, ProductDetails, utility, productService, ProductDetailsEnableConfig) {
    var ProductDetailsForm = function(product, productDetailList) {
        this.product = product;
        
        this.productDetailList = productDetailList;
        
        this.hasProductDetail = productDetailList.length != 0;
        
        this.productDetailsFormModel = {
        	id: ko.observable(),
        	whole: null,
        	piece: null,
        	innerPiece: null,
        	secondInnerPiece: null
        };
        
        this.wholeEnableConfig = new ProductDetailsEnableConfig(true, false, true, true, true, false, true, false, false, true, true);
        
        this.pieceEnableConfig = new ProductDetailsEnableConfig(true, true, true, false, false, false, true, false, false, true, true);
        
        this.innerPieceEnableConfig = new ProductDetailsEnableConfig(true, true, true, false, false, false, true, false, false, true, true);
        
        this.secondInnerPieceEnableConfig = new ProductDetailsEnableConfig(true, true, true, false, false, false, true, false, false, true, true);
    };
    
    ProductDetailsForm.prototype.activate = function() {
    	var self = this;
    	
    	self.productDetailsFormModel.id(self.product.id);
    	if(!self.hasProductDetail) { 
    		self.productDetailsFormModel.whole = new ProductDetails({ title: 'Whole', quantity: 1, grossPrice: 0, discount: 0,
    			netPrice: 0, percentProfit: 3.75, sellingPrice: 0, netProfit: 0, storageStockCount: 0, storeStockCount: 0}, self.wholeEnableConfig);
    		self.productDetailsFormModel.piece = new ProductDetails({ title: 'Piece', quantity: 0, grossPrice: 0, discount: 0, 
    			netPrice: 0, percentProfit: 5, sellingPrice: 0, netProfit: 0, storageStockCount: 0, storeStockCount: 0}, self.pieceEnableConfig);
    		self.productDetailsFormModel.innerPiece = new ProductDetails({ title: 'Inner Piece', quantity: 0, grossPrice: 0, discount: 0, 
    			netPrice: 0, percentProfit: 7.5, sellingPrice: 0, netProfit: 0, storageStockCount: 0, storeStockCount: 0}, self.innerPieceEnableConfig);
    		self.productDetailsFormModel.secondInnerPiece = new ProductDetails({ title: '2nd Inner Piece', quantity: 0, grossPrice: 0, discount: 0, 
    			netPrice: 0, percentProfit: 11.25, sellingPrice: 0, netProfit: 0, storageStockCount: 0, storeStockCount: 0}, self.secondInnerPieceEnableConfig);
    	} else {
    		var productDetailMap = new Array();
    		for(var c = 0; c < self.productDetailList.length; c++) {
    			productDetailMap[self.productDetailList[c].title] = self.productDetailList[c];
    		}
    		
    		self.productDetailsFormModel.whole = new ProductDetails(productDetailMap['Whole'], self.wholeEnableConfig);
    		self.productDetailsFormModel.piece = new ProductDetails(productDetailMap['Piece'], self.pieceEnableConfig);
    		self.productDetailsFormModel.innerPiece = new ProductDetails(productDetailMap['Inner Piece'], self.innerPieceEnableConfig);
    		self.productDetailsFormModel.secondInnerPiece = new ProductDetails(productDetailMap['2nd Inner Piece'], self.secondInnerPieceEnableConfig);
    	}
    };
 
    ProductDetailsForm.prototype.compositionComplete = function() {
    	var self = this;
    	
    	self.productDetailsFormModel.whole.formModel.grossPrice.subscribe(function(newValue) {
    		self.productDetailsFormModel.whole.formModel.netPrice(utility.computeNetPrice(newValue, self.productDetailsFormModel.whole.formModel.discount()));
    	
    		if(self.productDetailsFormModel.piece.formModel.quantity() > 1) {
    			self.productDetailsFormModel.piece.formModel.grossPrice(newValue / self.productDetailsFormModel.piece.formModel.quantity());
    		} else {
    			self.productDetailsFormModel.piece.formModel.grossPrice(0);
    		}
    	});
    	
    	self.productDetailsFormModel.whole.formModel.discount.subscribe(function(newValue) {
    		self.productDetailsFormModel.whole.formModel.netPrice(utility.computeNetPrice(self.productDetailsFormModel.whole.formModel.grossPrice(), newValue));
    	
    		self.productDetailsFormModel.piece.formModel.discount(newValue);
    		self.productDetailsFormModel.innerPiece.formModel.discount(newValue);
    		self.productDetailsFormModel.secondInnerPiece.formModel.discount(newValue);
    	});
    	
    	self.productDetailsFormModel.whole.formModel.percentProfit.subscribe(function(newValue) {
    		self.productDetailsFormModel.whole.formModel.sellingPrice(utility.computeSellingPrice(self.productDetailsFormModel.whole.formModel.netPrice(), newValue));
    		self.productDetailsFormModel.whole.formModel.netProfit(utility.computeNetProfit(self.productDetailsFormModel.whole.formModel.netPrice(), self.productDetailsFormModel.whole.formModel.sellingPrice()));
    	});
    	
    	self.productDetailsFormModel.whole.formModel.netPrice.subscribe(function(newValue) {
    		self.productDetailsFormModel.whole.formModel.sellingPrice(utility.computeSellingPrice(newValue, self.productDetailsFormModel.whole.formModel.percentProfit()));
    		self.productDetailsFormModel.whole.formModel.netProfit(utility.computeNetProfit(newValue, self.productDetailsFormModel.whole.formModel.sellingPrice()));
    	});
    	
    	self.enableConfigByPieceQuantity(self.productDetailsFormModel.piece.formModel.quantity());
		self.productDetailsFormModel.piece.formModel.quantity.subscribe(function(newValue) {
    		if(newValue > 1) {
    			self.productDetailsFormModel.piece.formModel.grossPrice(self.productDetailsFormModel.whole.formModel.grossPrice() / newValue);
    		} else {
    			self.productDetailsFormModel.piece.formModel.grossPrice(0);
    		}
    		
    		self.enableConfigByPieceQuantity(newValue);
    	});
    	
    	self.productDetailsFormModel.piece.formModel.grossPrice.subscribe(function(newValue) {
    		self.productDetailsFormModel.piece.formModel.netPrice(utility.computeNetPrice(newValue, self.productDetailsFormModel.piece.formModel.discount()));
    		
    		if(self.productDetailsFormModel.innerPiece.formModel.quantity() > 1) {
    			self.productDetailsFormModel.innerPiece.formModel.grossPrice(newValue / self.productDetailsFormModel.innerPiece.formModel.quantity());
    		} else {
    			self.productDetailsFormModel.innerPiece.formModel.grossPrice(0);
    		}
    	});
    	
    	self.productDetailsFormModel.piece.formModel.discount.subscribe(function(newValue) {
    		self.productDetailsFormModel.piece.formModel.netPrice(utility.computeNetPrice(self.productDetailsFormModel.piece.formModel.grossPrice(), newValue));
    	});
    	
    	self.productDetailsFormModel.piece.formModel.percentProfit.subscribe(function(newValue) {
    		self.productDetailsFormModel.piece.formModel.sellingPrice(utility.computeSellingPrice(self.productDetailsFormModel.piece.formModel.netPrice(), newValue));
    		self.productDetailsFormModel.piece.formModel.netProfit(utility.computeNetProfit(self.productDetailsFormModel.piece.formModel.netPrice(), self.productDetailsFormModel.piece.formModel.sellingPrice()));
    	});
    	
    	self.productDetailsFormModel.piece.formModel.netPrice.subscribe(function(newValue) {
    		self.productDetailsFormModel.piece.formModel.sellingPrice(utility.computeSellingPrice(newValue, self.productDetailsFormModel.piece.formModel.percentProfit()));
    		self.productDetailsFormModel.piece.formModel.netProfit(utility.computeNetProfit(newValue, self.productDetailsFormModel.piece.formModel.sellingPrice()));
    	});
    	
    	self.enableConfigByInnerPieceQuantity(self.productDetailsFormModel.innerPiece.formModel.quantity());
    	self.productDetailsFormModel.innerPiece.formModel.quantity.subscribe(function(newValue) {
    		if(newValue > 1) {
    			self.productDetailsFormModel.innerPiece.formModel.grossPrice(self.productDetailsFormModel.piece.formModel.grossPrice() / newValue);
    		} else {
    			self.productDetailsFormModel.innerPiece.formModel.grossPrice(0);
    		}
    		
    		self.enableConfigByInnerPieceQuantity(newValue);
    	});
    	
    	self.productDetailsFormModel.innerPiece.formModel.grossPrice.subscribe(function(newValue) {
    		self.productDetailsFormModel.innerPiece.formModel.netPrice(utility.computeNetPrice(newValue, self.productDetailsFormModel.innerPiece.formModel.discount()));
    		
    		if(self.productDetailsFormModel.secondInnerPiece.formModel.quantity() > 1) {
    			self.productDetailsFormModel.secondInnerPiece.formModel.grossPrice(newValue / self.productDetailsFormModel.secondInnerPiece.formModel.quantity());
    		} else {
    			self.productDetailsFormModel.secondInnerPiece.formModel.grossPrice(0);
    		}
    	});
    	
    	self.productDetailsFormModel.innerPiece.formModel.discount.subscribe(function(newValue) {
    		self.productDetailsFormModel.innerPiece.formModel.netPrice(utility.computeNetPrice(self.productDetailsFormModel.innerPiece.formModel.grossPrice(), newValue));
    	});
    	
    	self.productDetailsFormModel.innerPiece.formModel.percentProfit.subscribe(function(newValue) {
    		self.productDetailsFormModel.innerPiece.formModel.sellingPrice(utility.computeSellingPrice(self.productDetailsFormModel.innerPiece.formModel.netPrice(), newValue));
    		self.productDetailsFormModel.innerPiece.formModel.netProfit(utility.computeNetProfit(self.productDetailsFormModel.innerPiece.formModel.netPrice(), self.productDetailsFormModel.innerPiece.formModel.sellingPrice()));
    	});
    	
    	self.productDetailsFormModel.innerPiece.formModel.netPrice.subscribe(function(newValue) {
    		self.productDetailsFormModel.innerPiece.formModel.sellingPrice(utility.computeSellingPrice(newValue, self.productDetailsFormModel.innerPiece.formModel.percentProfit()));
    		self.productDetailsFormModel.innerPiece.formModel.netProfit(utility.computeNetProfit(newValue, self.productDetailsFormModel.innerPiece.formModel.sellingPrice()));
    	});
    	
    	self.enableConfigBySecondInnerPieceQuantity(self.productDetailsFormModel.secondInnerPiece.formModel.quantity());
    	self.productDetailsFormModel.secondInnerPiece.formModel.quantity.subscribe(function(newValue) {
    		if(newValue > 1) {
    			self.productDetailsFormModel.secondInnerPiece.formModel.grossPrice(self.productDetailsFormModel.innerPiece.formModel.grossPrice() / newValue);
    		} else {
    			self.productDetailsFormModel.secondInnerPiece.formModel.grossPrice(0);
    		}
    		
    		self.enableConfigBySecondInnerPieceQuantity(newValue);
    	});
    	
    	self.productDetailsFormModel.secondInnerPiece.formModel.grossPrice.subscribe(function(newValue) {
    		self.productDetailsFormModel.secondInnerPiece.formModel.netPrice(utility.computeNetPrice(newValue, self.productDetailsFormModel.secondInnerPiece.formModel.discount()));
    	});
    	
    	self.productDetailsFormModel.secondInnerPiece.formModel.discount.subscribe(function(newValue) {
    		self.productDetailsFormModel.secondInnerPiece.formModel.netPrice(utility.computeNetPrice(self.productDetailsFormModel.secondInnerPiece.formModel.grossPrice(), newValue));
    	});
    	
    	self.productDetailsFormModel.secondInnerPiece.formModel.percentProfit.subscribe(function(newValue) {
    		self.productDetailsFormModel.secondInnerPiece.formModel.sellingPrice(utility.computeSellingPrice(self.productDetailsFormModel.secondInnerPiece.formModel.netPrice(), newValue));
    		self.productDetailsFormModel.secondInnerPiece.formModel.netProfit(utility.computeNetProfit(self.productDetailsFormModel.secondInnerPiece.formModel.netPrice(), self.productDetailsFormModel.secondInnerPiece.formModel.sellingPrice()));
    	});
    	
    	self.productDetailsFormModel.secondInnerPiece.formModel.netPrice.subscribe(function(newValue) {
    		self.productDetailsFormModel.secondInnerPiece.formModel.sellingPrice(utility.computeSellingPrice(newValue, self.productDetailsFormModel.secondInnerPiece.formModel.percentProfit()));
    		self.productDetailsFormModel.secondInnerPiece.formModel.netProfit(utility.computeNetProfit(newValue, self.productDetailsFormModel.secondInnerPiece.formModel.sellingPrice()));
    	});
    };
    
    ProductDetailsForm.show = function(product, productDetailList) {
        return dialog.show(new ProductDetailsForm(product, productDetailList));
    };
    
    ProductDetailsForm.prototype.save = function() {
    	var self = this;
    	
    	if(self.productDetailsFormModel.secondInnerPiece.formModel.storageStockCount() > 0) {
    		self.productDetailsFormModel.innerPiece.formModel.storageStockCount(Number(self.productDetailsFormModel.innerPiece.formModel.storageStockCount()) + Math.floor(self.productDetailsFormModel.secondInnerPiece.formModel.storageStockCount() / self.productDetailsFormModel.secondInnerPiece.formModel.quantity()));
        	self.productDetailsFormModel.secondInnerPiece.formModel.storageStockCount(utility.mod(self.productDetailsFormModel.secondInnerPiece.formModel.storageStockCount(), self.productDetailsFormModel.secondInnerPiece.formModel.quantity()));
    	}
    	
    	if(self.productDetailsFormModel.innerPiece.formModel.storageStockCount() > 0) {
	    	self.productDetailsFormModel.piece.formModel.storageStockCount(Number(self.productDetailsFormModel.piece.formModel.storageStockCount()) + Math.floor(self.productDetailsFormModel.innerPiece.formModel.storageStockCount() / self.productDetailsFormModel.innerPiece.formModel.quantity()));
	    	self.productDetailsFormModel.innerPiece.formModel.storageStockCount(utility.mod(self.productDetailsFormModel.innerPiece.formModel.storageStockCount(), self.productDetailsFormModel.innerPiece.formModel.quantity()));
    	}
    	
    	if(self.productDetailsFormModel.piece.formModel.storageStockCount() > 0) {
			self.productDetailsFormModel.whole.formModel.storageStockCount(Number(self.productDetailsFormModel.whole.formModel.storageStockCount()) + Math.floor(self.productDetailsFormModel.piece.formModel.storageStockCount() / self.productDetailsFormModel.piece.formModel.quantity()));
	    	self.productDetailsFormModel.piece.formModel.storageStockCount(utility.mod(self.productDetailsFormModel.piece.formModel.storageStockCount(), self.productDetailsFormModel.piece.formModel.quantity()));
    	}
    	
    	if(self.productDetailsFormModel.secondInnerPiece.formModel.storeStockCount() > 0) {
	    	self.productDetailsFormModel.innerPiece.formModel.storeStockCount(Number(self.productDetailsFormModel.innerPiece.formModel.storeStockCount()) + Math.floor(self.productDetailsFormModel.secondInnerPiece.formModel.storeStockCount() / self.productDetailsFormModel.secondInnerPiece.formModel.quantity()));
	    	self.productDetailsFormModel.secondInnerPiece.formModel.storeStockCount(utility.mod(self.productDetailsFormModel.secondInnerPiece.formModel.storeStockCount(), self.productDetailsFormModel.secondInnerPiece.formModel.quantity()));
    	}
    	
    	if(self.productDetailsFormModel.innerPiece.formModel.storeStockCount() > 0) {
	    	self.productDetailsFormModel.piece.formModel.storeStockCount(Number(self.productDetailsFormModel.piece.formModel.storeStockCount()) + Math.floor(self.productDetailsFormModel.innerPiece.formModel.storeStockCount() / self.productDetailsFormModel.innerPiece.formModel.quantity()));
	    	self.productDetailsFormModel.innerPiece.formModel.storeStockCount(utility.mod(self.productDetailsFormModel.innerPiece.formModel.storeStockCount(), self.productDetailsFormModel.innerPiece.formModel.quantity()));
    	}
    	
    	if(self.productDetailsFormModel.piece.formModel.storeStockCount() > 0) {
			self.productDetailsFormModel.whole.formModel.storeStockCount(Number(self.productDetailsFormModel.whole.formModel.storeStockCount()) + Math.floor(self.productDetailsFormModel.piece.formModel.storeStockCount() / self.productDetailsFormModel.piece.formModel.quantity()));
	    	self.productDetailsFormModel.piece.formModel.storeStockCount(utility.mod(self.productDetailsFormModel.piece.formModel.storeStockCount(), self.productDetailsFormModel.piece.formModel.quantity()));
    	}
    	
    	productService.saveProductDetails(self.productDetailsFormModel.id,
    			ko.toJSON(self.productDetailsFormModel.whole.formModel),
    			ko.toJSON(self.productDetailsFormModel.piece.formModel),
    			ko.toJSON(self.productDetailsFormModel.innerPiece.formModel),
    			ko.toJSON(self.productDetailsFormModel.secondInnerPiece.formModel)).done(function(result) {
    				
				if(result.success) {
	        		dialog.close(self);	
	        	} 
	        	app.showMessage(result.message);
        });
    };
    
    ProductDetailsForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    ProductDetailsForm.prototype.enableConfigByPieceQuantity = function(value) {
    	var self = this;
    	
    	self.pieceEnableConfig.enableBarcode(value > 1);
    	self.pieceEnableConfig.enableUnitType(value > 1);
		self.pieceEnableConfig.enablePercentProfit(value > 1);
		self.pieceEnableConfig.enableStorageStockCount(value > 1);
		self.pieceEnableConfig.enableStoreStockCount(value > 1);
    	self.innerPieceEnableConfig.enableQuantity(value > 1);
    };
    
    ProductDetailsForm.prototype.enableConfigByInnerPieceQuantity = function(value) {
    	var self = this;
    	
    	self.innerPieceEnableConfig.enableBarcode(value > 1);
    	self.innerPieceEnableConfig.enableUnitType(value > 1);
    	self.innerPieceEnableConfig.enablePercentProfit(value > 1);
    	self.innerPieceEnableConfig.enableStorageStockCount(value > 1);
		self.innerPieceEnableConfig.enableStoreStockCount(value > 1);
    	self.secondInnerPieceEnableConfig.enableQuantity(value > 1);
    };
    
    ProductDetailsForm.prototype.enableConfigBySecondInnerPieceQuantity = function(value) {
    	var self = this;
    	
    	self.secondInnerPieceEnableConfig.enableBarcode(value > 1);
    	self.secondInnerPieceEnableConfig.enableUnitType(value > 1);
    	self.secondInnerPieceEnableConfig.enablePercentProfit(value > 1);
    	self.secondInnerPieceEnableConfig.enableStorageStockCount(value > 1);
		self.secondInnerPieceEnableConfig.enableStoreStockCount(value > 1);
    };
    
    return ProductDetailsForm;
});