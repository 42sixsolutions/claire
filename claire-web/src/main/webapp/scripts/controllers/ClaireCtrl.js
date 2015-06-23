'use strict';

angular.module('claire.controllers').controller('ClaireCtrl', ["$scope", "DrugInfo",
        function($scope, DrugInfo) {


    // Convert the string dates into javascript Dates
    var transformDates = function(data) {
        for (var i = 0; i < data.length; i++) {
            data[i][0] = new Date(data[i][0]);
        }
    };

    DrugInfo.getTwitterStats().then(function(response) {
        $scope.twitterStats = response.data;
    });

    DrugInfo.getChart().then(function(response) {
        var chartData = [];
        transformDates(response.data.positiveTweets);
        chartData.push({
            data: response.data.positiveTweets,
            lines: { show: true }
        });
        transformDates(response.data.negativeTweets);
        chartData.push({
            data: response.data.negativeTweets,
            lines: { show: true }
        });
        transformDates(response.data.unknownTweets);
        chartData.push({
            data: response.data.unknownTweets,
            lines: { show: true }
        });
        transformDates(response.data.adverseEvents);
        chartData.push({
            data: response.data.adverseEvents,
            points: { show: true, radius: 7 }
        });
        transformDates(response.data.recalls);
        chartData.push({
            data: response.data.recalls,
            bars: { show: true, barWidth: 3 * 12 * 24 * 60 * 60 }
        });
        $scope.mainChartData = chartData;
    });

    DrugInfo.getFDAStats().then(function(response) {
        $scope.fdaStats = response.data;
    });

    DrugInfo.getDrug().then(function(response) {
        $scope.drug = response.data;
    });
}]);
