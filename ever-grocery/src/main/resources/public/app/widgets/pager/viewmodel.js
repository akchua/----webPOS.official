define(['knockout'], function (ko) {
    var Pager = function() {
    	this.numberOfPagesToShow = ko.observable();
    	
    	this.currentPage = ko.observable();
    	this.totalItems = ko.observable();
    	this.itemsPerPage = ko.observable();
    };
    
    Pager.prototype.activate = function(settings) {
    	this.numberOfPagesToShow(settings.config.numberOfPagesToShow);
    	
    	// itemsPerPage, totalItems and currentPage should be passed by the implementing page and must be observable to be able to notify changes
    	this.itemsPerPage = settings.config.itemsPerPage;
    	this.totalItems = settings.config.totalItems;
    	this.currentPage = settings.config.currentPage;
    };
    
    Pager.prototype.numberOfPages = function() {
    	var noOfPages = this.totalItems() / this.itemsPerPage();
    	noOfPages = parseInt(noOfPages + (this.totalItems() % this.itemsPerPage() > 0 ? 1 : 0));
    	return noOfPages;
    };
    
    Pager.prototype.computePageNumbers = function() {
    	var pageNumbers = new Array();
    	
    	var c;
    	var numberOfAvailablePagesToShow = this.numberOfPagesToShow() < this.numberOfPages() ? this.numberOfPagesToShow() : this.numberOfPages();
    	var median = numberOfAvailablePagesToShow < this.numberOfPagesToShow() ? numberOfAvailablePagesToShow : Math.ceil(numberOfAvailablePagesToShow / 2);
    	
    	if(this.currentPage() + (median - 1) > this.numberOfPages()) {
    		for(c = this.numberOfPages() - numberOfAvailablePagesToShow + 1; c <= this.numberOfPages(); c++) {
    			pageNumbers.push(c);
    		}
    	} else if(this.currentPage() > median) {
    		for(c = median - 1; c > 0; c--) {
    			pageNumbers.push(this.currentPage() - c);
    		}
    		
    		pageNumbers.push(this.currentPage());
        	
        	for(c = 1; c < median; c++) {
        		pageNumbers.push(this.currentPage() + c);
        	}
    	} else {
    		for(c = 1; c <= numberOfAvailablePagesToShow; c++) {
    			pageNumbers.push(c);
    		}
    	}
    	
    	return pageNumbers;
    };
    
    Pager.prototype.next = function() {
    	if(this.currentPage() < this.numberOfPages()) {
    		this.currentPage(this.currentPage() + 1);
    	}
    };
    
    Pager.prototype.back = function() {
    	if(this.currentPage() > 1) {
    		this.currentPage(this.currentPage() - 1);
    	}
    };
    
    Pager.prototype.first = function() {
    	if(this.currentPage() > 1) {
    		this.currentPage(1);
    	}
    };
    
    Pager.prototype.last = function() {
    	if(this.currentPage() < this.numberOfPages()) {
    		this.currentPage(this.numberOfPages());
    	}
    };
    
    Pager.prototype.page = function(pageNumber) {
    	this.currentPage(pageNumber);
    }
    
    return Pager;
});