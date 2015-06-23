'use strict';

angular.module('claire.services').factory('DrugInfo', ['$http', function($http) {
    var getDrug = function(drugName) {
        return $http.get('api/drug/detail', {name: drugName});
    };

    var getTwitterStats = function(drugName) {
        return $http.get('api/drug/tweets', {name: drugName});
    };

    var getFDAStats = function(drugName) {
        return $http.get('api/drug/fda', {name: drugName});
    };

    var getRankings = function(drugName) {
        return $http.get('api/drug/ranking', {name: drugName});
    };

    var getChart = function(drugName) {
        return $http.get('api/drug/chart', {name: drugName});
    };

    return {
        getDrug: getDrug,
        getTwitterStats: getTwitterStats,
        getFDAStats: getFDAStats,
        getRankings: getRankings,
        getChart: getChart
    };
}]);