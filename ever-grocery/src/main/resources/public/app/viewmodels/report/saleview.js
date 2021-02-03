define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/customerorderservice'], function (dialog, app, ko, customerOrderService) {
    var SaleView = function(customerOrder) {
    	this.customerOrder = customerOrder;
    	this.customerOrderDetailList = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.enableButtons = ko.observable(true);
		this.allowPrint = ko.observable(false);
		
		this.customerOrderViewModel = {
			customerOrderId: ko.observable(),
			customerOrderNumber: ko.observable(),
			
			serialInvoiceNumber : ko.observable(),
			refundNumber : ko.observable(),
			referenceSerialInvoiceNumber : ko.observable(),
			
			formattedPointsEarned : ko.observable(''),
			totalItems : ko.observable(),
			formattedPaidOn : ko.observable(),
			formattedCashier: ko.observable(),
			
			formattedCash : ko.observable(),
			formattedCheckAmount : ko.observable(),
			formattedCardAmount : ko.observable(),
			formattedPointsAmount : ko.observable(),
			formattedGrossAmount : ko.observable(),
			formattedOutrightPromoDiscount : ko.observable(),
			
			discountShorthand : ko.observable(''),
			formattedTotalDiscount: ko.observable(),
			formattedTotalAmount: ko.observable(),
			
			cartonCount: ko.observable(),
			plasticCount: ko.observable(),
			bagCount: ko.observable()
		};
		
		this.customer = {
			formattedName : ko.observable(''),
			formattedCardId : ko.observable('')
		};
		
		this.discounted = ko.observable(false);
    };
    
    SaleView.prototype.activate = function() {
    	var self = this;
    	
    	self.customerOrderViewModel.customerOrderId(self.customerOrder.id);
    	self.customerOrderViewModel.customerOrderNumber(self.customerOrder.orderNumber);
    	
    	self.customerOrderViewModel.serialInvoiceNumber(self.customerOrder.serialInvoiceNumber);
    	self.customerOrderViewModel.refundNumber(self.customerOrder.refundNumber);
    	self.customerOrderViewModel.referenceSerialInvoiceNumber(self.customerOrder.referenceSerialInvoiceNumber);
    	
    	self.customerOrderViewModel.formattedPointsEarned(self.customerOrder.formattedPointsEarned);
    	self.customerOrderViewModel.totalItems(self.customerOrder.totalItems);
    	self.customerOrderViewModel.formattedPaidOn(self.customerOrder.formattedPaidOn);
    	if(self.customerOrder.cashier) {
    		self.customerOrderViewModel.formattedCashier(self.customerOrder.cashier.formattedName);
    		self.allowPrint(true);
    	}

    	self.customerOrderViewModel.formattedCash(self.customerOrder.formattedCash);
    	self.customerOrderViewModel.formattedCheckAmount(self.customerOrder.formattedCheckAmount);
    	self.customerOrderViewModel.formattedCardAmount(self.customerOrder.formattedCardAmount);
    	self.customerOrderViewModel.formattedPointsAmount(self.customerOrder.formattedPointsAmount);
    	self.customerOrderViewModel.formattedGrossAmount(self.customerOrder.formattedGrossAmount);
    	self.customerOrderViewModel.formattedOutrightPromoDiscount(self.customerOrder.formattedOutrightPromoDiscount);
    	
    	if(self.customerOrder.discountType) {
    		self.customerOrderViewModel.discountShorthand(self.customerOrder.discountType.shortHand + ' ' + self.customerOrder.discountType.percentDiscount + '%');
    	}
    	self.customerOrderViewModel.formattedTotalDiscount(self.customerOrder.formattedTotalDiscountAmount);
    	self.customerOrderViewModel.formattedTotalAmount(self.customerOrder.formattedTotalAmount);
    	self.discounted(self.customerOrder.totalDiscountAmount > 0);
    	
    	if(self.customerOrder.customer) {
    		self.customer.formattedName(self.customerOrder.customer.formattedName);
    		self.customer.formattedCardId(self.customerOrder.customer.formattedCardId);
    	} else {
    		self.customer.formattedName('n/a');
    		self.customer.formattedCardId('n/a');
    	}
    	
    	self.customerOrderViewModel.cartonCount(self.customerOrder.cartonCount);
    	self.customerOrderViewModel.plasticCount(self.customerOrder.plasticCount);
    	self.customerOrderViewModel.bagCount(self.customerOrder.bagCount);
    	
    	self.currentPage(1);
    	self.currentPageSubscription = self.currentPage.subscribe(function() {
    		self.refreshCustomerOrderDetailList();
		});
    	
    	self.refreshCustomerOrderDetailList();
    };
    
    SaleView.show = function(customerOrder) {
    	return dialog.show(new SaleView(customerOrder));
    };
    
    SaleView.prototype.refreshCustomerOrderDetailList = function() {
    	var self = this;
    	
    	customerOrderService.getCustomerOrderDetailList(self.currentPage(), self.customerOrderViewModel.customerOrderId(), false).done(function(data) { 
			self.customerOrderDetailList(data.list);
			self.totalItems(data.total);
		});
    };
    
    SaleView.prototype.print = function() {
    	var self = this;
		self.enableButtons(false);
		
		app.showMessage('Are you sure you want to print Customer Order #' + self.customerOrderViewModel.customerOrderNumber() + '?',
				'Confirm Print',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				customerOrderService.printReceiptCopy(self.customerOrderViewModel.customerOrderId()).done(function(result) {
					app.showMessage(result.message);
				});
			}
			self.enableButtons(true);
		});
    };
    
    SaleView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return SaleView;
});