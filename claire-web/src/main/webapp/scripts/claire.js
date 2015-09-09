'use strict';

angular.module('claire.controllers', []);
angular.module('claire.services', []);
angular.module('claire.directives', []);

// Declare app level module which depends on views, and components
angular.module('claire', [
    'ngRoute',
    'claire.controllers',
    'claire.directives',
    'claire.services',
    'angulartics',
    'angulartics.google.analytics'
]).
    config(['$routeProvider', '$analyticsProvider', function ($routeProvider, $analyticsProvider) {
        $routeProvider.when('/', {templateUrl: 'partials/home.html', controller: 'ClaireCtrl'});
        $routeProvider.when('/detail/:drug', {templateUrl: 'partials/detail.html', controller: 'ClaireCtrl'});
        $routeProvider.otherwise({redirectTo: '/'});

        $analyticsProvider.firstPageview(true);
        $analyticsProvider.withAutoBase(true);
    }]);
