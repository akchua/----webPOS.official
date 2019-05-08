define(['plugins/router', 'durandal/app', 'knockout', 'modules/securityservice', 'modules/userservice', 'modules/constantsservice', 'viewmodels/profile', 'viewmodels/passwordform', 'viewmodels/forgotpassword', 'viewmodels/settings', 'viewmodels/recenttransactions'], 
		function (router, app, ko, securityService, userService, constantsService, Profile, PasswordForm, ForgotPasswordForm, Settings, RecentTransactions) {
	var homeroute = [
	    { route: ['', 'home'], moduleId: 'viewmodels/home', title: 'Home', nav: false }
	];
	
	var adminroute = [
	    { route: 'admin', moduleId: 'viewmodels/admin/adminhome', title: 'Admin Home', nav: false, hash: '#admin' }
	];
	
	var userroute = [
	    { route: 'user', moduleId: 'viewmodels/user/user', title: 'Users', nav: true, hash: '#user' }
	];
	
	var reportroute = [
		{ route: 'report', moduleRootId: 'viewmodels/report', title: 'Reports', nav: true, hash: '#report',
			childRoutes: [
				{ route: 'cashflow', moduleId: 'cashflow', title: 'Cash Flow', nav: true, hash: 'cashflow'},
				{ route: 'sale', moduleId: 'sale', title: 'Sales', nav: true, hash: 'sale'},
	      		{ route: 'purchase', moduleId: 'purchase', title: 'Purchases', nav: true, hash: 'purchase'}
			]
		}
	];
	
	var manageroute = [
	  	{ route: 'manage', moduleRootId: 'viewmodels/manage', title: '', nav: true, hash: '#manage',
	  		childRoutes: [
	  		    /*{ route: 'brand', moduleId: 'brand', title: 'Brands', nav: true, hash: 'brand' },*/
	  		    { route: 'category', moduleId: 'category', title: 'Categories', nav: true, hash: 'category' },
	  		    { route: 'company', moduleId: 'company', title: 'Companies', nav: true, hash: 'company' },
	  		    /*{ route: 'distributor', moduleId: 'distributor', title: 'Distributors', nav: true, hash: 'distributor' },*/
	      		{ route: 'product', moduleId: 'product', title: 'Products', nav: true, hash: 'product' },
	      		
	      		{ route: 'customer', moduleId: 'customer', title: 'Customers', nav: true, hash: 'customer' }
	  		]
	  	}
	];
	
	var customerorderroute = [
	    { route: 'customerorder', moduleId: 'viewmodels/customer-order/customerorder', title: 'Sales Order', nav: true, hash: '#customerorder' },
	    { route: 'customerorderpage/:id', moduleId: 'viewmodels/customer-order/customerorderpage', title: 'Customer Order Page', nav: false, hash: '#customerorderpage' }
	];
	
	var cashierroute = [
	    { route: 'cashier', moduleId: 'viewmodels/cashier/cashier', title: '', nav: true, hash: '#cashier'}
	];
	
	var purchaseorderroute = [
	    { route: 'purchaseorder', moduleId: 'viewmodels/purchase-order/purchaseorder', title: 'Purchase Order', nav: true, hash: '#purchaseorder'},
	    { route: 'purchaseorderpage/:id', moduleId: 'viewmodels/purchase-order/purchaseorderpage', title: 'Purchase Order Page', nav: false, hash: '#purchaseorderpage' }
	];
	
	var cashtransferroute = [
		{ route: 'cashtransfer', moduleId: 'viewmodels/cashtransfer/cashtransfer', title: 'Cash Transfer', nav: true, hash: '#cashtransfer'},
	];
	
	var searchroute = [
   	    { route: 'search', moduleId: 'viewmodels/search/search', title: '', nav: true, hash: '#search' }           
	];
	
	var Shell = function() {
		this.router = router;
		
		this.routes = homeroute;
		this.systemVersion = ko.observable();
		
		this.userDetails = {
			id: ko.observable(),
			fullName: ko.observable(),
			userType: ko.observable(),
			itemsPerPage: ko.observable(),
			imagePath: ko.observable()
		};
		
		this.loginForm = {
			username: ko.observable(),
			password: ko.observable()
		};
	};
	
	Shell.prototype.activate = function() {
		var self = this;
		
		constantsService.getVersion().done(function(version) {
			self.systemVersion(version);
		});
		
		if(app.user) {
			self.userDetails.id(app.user.id);
    		self.userDetails.fullName(app.user.fullName);
    		self.userDetails.userType(app.user.userType);
    		self.userDetails.itemsPerPage(app.user.itemsPerPage);
    		self.userDetails.imagePath(userService.getUserImageByFileName(app.user.image));
    		
    		switch(app.user.userType.name) {
	        	case 'ADMINISTRATOR':
	        		self.routes = self.routes.concat(adminroute);
	        		self.routes = self.routes.concat(userroute);
	        	case 'MANAGER':
	        		self.routes = self.routes.concat(reportroute);
	        	case 'ASSISTANT_MANAGER':
	        		self.routes = self.routes.concat(manageroute);
	        		self.routes = self.routes.concat(cashierroute);
	        		self.routes = self.routes.concat(cashtransferroute);
	        	case 'SENIOR_STAFF':
	        		self.routes = self.routes.concat(purchaseorderroute);
	        	case 'STAFF':
	        		self.routes = self.routes.concat(customerorderroute);
	        		break;
	        	case 'CASHIER':
	        		self.routes = self.routes.concat(cashierroute);
	        		self.routes = self.routes.concat(cashtransferroute);
	        		break;
	        	case 'STORAGE_MANAGER':
	        		break;
	    	}
	    	
    		self.routes = self.routes.concat(searchroute);
		}
    		
    	$.each(self.routes, function(index, route) {
            if (route.childRoutes === undefined)
                return
            $.each(route.childRoutes, function(index, childRoute) {
                childRoute.route = route.route + '/' + childRoute.route;
                childRoute.moduleId = route.moduleRootId + '/' + childRoute.moduleId;
                childRoute.title = childRoute.title;
                childRoute.hash = route.hash + '/' + childRoute.hash;
                childRoute.parent = route.moduleRootId;
            });
            self.routes = self.routes.concat(route.childRoutes);
        });
    	
        self.router.map(self.routes)
        	.buildNavigationModel()
        	.mapUnknownRoutes('viewmodels/home');
        
        return router.activate();
	};
	
	Shell.prototype.refreshUser = function() {
		var self = this;
		
		securityService.getUser().done(function(user) {
    		app.user = user;
    		self.userDetails.id(user.id);
    		self.userDetails.fullName(user.fullName);
    		self.userDetails.userType(user.userType);
    		self.userDetails.itemsPerPage(user.itemsPerPage);
    		self.userDetails.imagePath(userService.getUserImageByFileName(user.image));
        });
	};
	
	Shell.prototype.profile = function() {
		var self = this;
		
		Profile.show(app.user).done(function() {
			self.refreshUser();
		});
	};
	
	Shell.prototype.changePassword = function() {
		var self = this;
		
		PasswordForm.show();
	};
	
	Shell.prototype.changeSettings = function() {
		var self = this;
		
		Settings.show().done(function() {
			self.refreshUser();
		});
	};
	
	Shell.prototype.recentTransactions = function() {
		var self = this;
		
		RecentTransactions.show();
	};
	
	Shell.prototype.logout = function() {
		securityService.logout().done(function() {
			location.href = '/';
    	});
	};
	
	return Shell;
});