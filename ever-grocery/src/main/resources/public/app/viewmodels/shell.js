define(['plugins/router', 'durandal/app', 'modules/securityservice', 'modules/userservice', 'viewmodels/usersettings'], function (router, app, securityService, userService, UserSettings) {
	var homeroute = [
	    { route: ['', 'home'], moduleId: 'viewmodels/home', title: 'Home', nav: false }
	];
	
	var userroute = [
	    { route: 'user', moduleId: 'viewmodels/user/user', title: 'Users', nav: true, hash: '#user' }
	];
	
	var reportroute = [
		{ route: 'report', moduleRootId: 'viewmodels/report', title: 'Reports', nav: true, hash: '#report',
			childRoutes: [
				{ route: 'sale', moduleId: 'sale', title: 'Sales', nav: true, hash: 'sale'},
	      		{ route: 'purchase', moduleId: 'purchase', title: 'Purchases', nav: true, hash: 'purchase'}
			]
		}
	];
	
	var manageroute = [
	  	{ route: 'manage', moduleRootId: 'viewmodels/manage', title: '', nav: true, hash: '#manage',
	  		childRoutes: [
	  		    { route: 'brand', moduleId: 'brand', title: 'Brands', nav: true, hash: 'brand' },
	  		    { route: 'category', moduleId: 'category', title: 'Categories', nav: true, hash: 'category' },
	  		    { route: 'company', moduleId: 'company', title: 'Companies', nav: true, hash: 'company' },
	  		    { route: 'distributor', moduleId: 'distributor', title: 'Distributors', nav: true, hash: 'distributor' },
	      		{ route: 'product', moduleId: 'product', title: 'Products', nav: true, hash: 'product' },
	      		
	      		{ route: 'customer', moduleId: 'customer', title: 'Customers', nav: true, hash: 'customer' }
	  		]
	  	}
	];
	
	var customerorderroute = [
	    { route: 'customerorder', moduleId: 'viewmodels/customer-order/customerorder', title: 'Customer Order', nav: true, hash: '#customerorder' },
	    { route: 'customerorderpage/:id', moduleId: 'viewmodels/customer-order/customerorderpage', title: 'Customer Order Page', nav: false, hash: '#customerorderpage' }
	];
	
	var cashierroute = [
	    { route: 'cashier', moduleId: 'viewmodels/cashier/cashier', title: '', nav: true, hash: '#cashier'}
	];
	
	var purchaseorderroute = [
	    { route: 'purchaseorder', moduleId: 'viewmodels/purchase-order/purchaseorder', title: 'Purchase Order', nav: true, hash: '#purchaseorder'},
	    { route: 'purchaseorderpage/:id', moduleId: 'viewmodels/purchase-order/purchaseorderpage', title: 'Purchase Order Page', nav: false, hash: '#purchaseorderpage' }
	];
	
	var searchroute = [
   	    { route: 'search', moduleId: 'viewmodels/search/search', title: '', nav: true, hash: '#search' }           
	];
	
    return {
        router: router,
        
        user: app.user,
        
        activate: function () {
        	var self = this;
        	var routes = homeroute;
        	
        	switch(self.user.userType) {
	        	case 'ADMINISTRATOR':
	        		routes = routes.concat(userroute);
	        	case 'MANAGER':
	        		routes = routes.concat(reportroute);
	        	case 'ASSISTANT_MANAGER':
	        		routes = routes.concat(manageroute);
	        		routes = routes.concat(cashierroute);
	        	case 'SENIOR_STAFF':
	        		routes = routes.concat(purchaseorderroute);
	        	case 'STAFF':
	        		routes = routes.concat(customerorderroute);
	        		break;
	        	case 'CASHIER':
	        		routes = routes.concat(cashierroute);
	        		break;
	        	case 'STORAGE_MANAGER':
	        		break;
        	}
        	
        	routes = routes.concat(searchroute);
        	
        	$.each(routes, function(index, route) {
                if (route.childRoutes === undefined)
                    return
                $.each(route.childRoutes, function(index, childRoute) {
                    childRoute.route = route.route + '/' + childRoute.route;
                    childRoute.moduleId = route.moduleRootId + '/' + childRoute.moduleId;
                    childRoute.title = childRoute.title;
                    childRoute.hash = route.hash + '/' + childRoute.hash;
                    childRoute.parent = route.moduleRootId;
                });
                routes = routes.concat(route.childRoutes);
            });
        	
            router.map(routes).buildNavigationModel();
            
            return router.activate();
        },
        
        logout: function() {
        	securityService.logout().done(function() {
        		location.href = '/';
        	});
        },
        
        userSettings: function() {
        	var self = this;
        	
        	UserSettings.show(self.user).done(function() {
        		window.location.reload();
        	});
        }
    };
});