'use strict';

angular.module('claire.services').factory('Search', ['$resource', function($resource) {
        return $resource('/api/drugs/:name');
}]);