'use strict';

angular.module('claire.controllers', []);
angular.module('claire.services', []);
angular.module('claire.directives', []);

// Declare app level module which depends on views, and components
angular.module('claire', [
  'ngRoute',
  'claire.controllers',
  'claire.directives',
  'claire.services'
]).
config(['$routeProvider', function($routeProvider) {
}]);
