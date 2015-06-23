'use strict';

angular.module('claire.services').factory('Trends', ['$http', function($http){
    var getTopPositive = function(count) {
        return $http.get('api/trends/positive', {count: count});
    };

    var getTopNegative = function(count) {
        return $http.get('api/trends/negative', {count: count});
    };
    var getMostAdverseEvents = function(count) {
        return $http.get('api/trends/adverse', {count: count});
    }
    return {
        getTopPositive: getTopPositive,
        getTopNegative: getTopNegative,
        getMostAdverseEvents: getMostAdverseEvents
    };
}]);