requirejs.config({
	waitSeconds: 200,
	paths: {
        'text': '../lib/require/text',
        'durandal':'../lib/durandal/js',
        'plugins' : '../lib/durandal/js/plugins',
        'transitions' : '../lib/durandal/js/transitions',
        'knockout': '../lib/knockout/knockout-3.4.0',
        'bootstrap': '../lib/bootstrap/js/bootstrap',
        'jquery': '../lib/jquery/jquery-1.9.1',
        'fileDownload': '../lib/jquery/jquery.fileDownload',
        'bootstrap-datetimepicker': '../lib/bootstrap/js/bootstrap-datetimepicker',
        'moment': '../lib/moment/moment',
        'fullcalendar' : '../lib/fullcalendar/js/fullcalendar.min',
        'd3' : '../lib/d3/d3.min',
        'c3' : '../lib/c3/c3.min'
    },
    shim: {
        'bootstrap': {
            deps: ['jquery'],
            exports: 'jQuery'
       },
       	'fileDownload': {
       		deps: ['jquery'],
       		exports: 'jQuery'
       	},
       	'c3': {
       		deps: ['d3'],
       		exports: 'd3'
       }
    }
});

define(['durandal/system', 'durandal/app', 'durandal/viewLocator', 'modules/securityservice', 'bootstrap', 'modules/kocustombindings'],  function (system, app, viewLocator, securityService) {
    //>>excludeStart("build", true);
    system.debug(true);
    //>>excludeEnd("build");

    app.title = 'Ever Grocery';

    app.configurePlugins({
        router: true,
        dialog: true,
        widget: true
    });

    app.start().then(function() {
        //Replace 'viewmodels' in the moduleId with 'views' to locate the view.
        //Look for partial views in a 'views' folder in the root.
        viewLocator.useConvention();

        securityService.getUser().done(function(user) {
    		app.user = user;
    		//Show the app by setting the root view model for our application with a transition.
            app.setRoot('viewmodels/shell');
        }).error(function() {
        	app.setRoot('viewmodels/security/login');
        });
    });
});