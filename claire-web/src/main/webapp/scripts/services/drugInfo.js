'use strict';

angular.module('claire.services').factory('DrugInfo', ['$http', function($http) {
    var getDrugList = function() {
        return $http.get('api/drug/list');
    };

    var getDrug = function(drugName) {
        return $http.get('api/drug/detail/' + drugName, { data:  {drugName: drugName} });
    };

    var getTwitterStats = function(drugName) {
        return $http.get('api/drug/tweets/' + drugName, { data:  {drugName: drugName} });
    };

    var getFDAStats = function(drugName) {
        return $http.get('api/drug/fda/' + drugName, { data:  {drugName: drugName} });
    };

    var getRankings = function(drugName) {
        return $http.get('api/drug/ranking/' + drugName, { data:  {drugName: drugName} });
    };

    var getChart = function(drugName) {
        return $http.get('api/drug/chart/' + drugName, { data:  {drugName: drugName} });
    };

    return {
        getDrugList: getDrugList,
        getDrug: getDrug,
        getTwitterStats: getTwitterStats,
        getFDAStats: getFDAStats,
        getRankings: getRankings,
        getChart: getChart
    };
}]);