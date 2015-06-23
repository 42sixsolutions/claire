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
  'claire.mockREST'
]).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', { templateUrl: 'partials/home.html', controller: 'ClaireCtrl' });
    $routeProvider.when('/detail/:drug', { templateUrl: 'partials/detail.html', controller: 'ClaireCtrl' });
    $routeProvider.otherwise({redirectTo: '/'});
}]);
