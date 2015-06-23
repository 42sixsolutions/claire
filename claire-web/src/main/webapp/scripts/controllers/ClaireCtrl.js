'use strict';

angular.module('claire.controllers').controller('ClaireCtrl', ["$scope", "$location", "DrugInfo", "Trends",
        function($scope, $location, DrugInfo, Trends) {

    $scope.drug = {};
    $scope.trends = {
        topPositive: [],
        topNegative: [],
        mostAdverseEvents: []
    };

    var pathParts = $location.path().split("/detail/");
    var isDetailsPage = pathParts.length > 1;

    $scope.onSearch = function(drug) {
        $location.path("/detail/" + drug);
    };

    if (!isDetailsPage) {
        Trends.getTopPositive(5).then(function(response) {
            $scope.trends.topPositive = response.data;
        });

        Trends.getTopNegative(5).then(function(response) {
            $scope.trends.topNegative = response.data;
        });

        Trends.getMostAdverseEvents(5).then(function(response) {
            $scope.trends.mostAdverseEvents = response.data;
        });
    }

    if (isDetailsPage) {
        $scope.drug.selected = pathParts[1];
    }

    if (isDetailsPage) {
        $scope.$watch("drug.selected", function(newValue, oldValue) {
            if (newValue && newValue !== oldValue) {
                $location.path("/detail/" + newValue);
            }
        });
    }

    if (isDetailsPage) {
        // Convert the string dates into javascript Dates
        var transformDates = function(data) {
            for (var i = 0; i < data.length; i++) {
                data[i][0] = new Date(data[i][0]);
            }
        };

        DrugInfo.getTwitterStats($scope.drug.selected).then(function(response) {
            $scope.twitterStats = response.data;
        });

        DrugInfo.getChart($scope.drug.selected).then(function(response) {
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

        DrugInfo.getFDAStats($scope.drug.selected).then(function(response) {
            $scope.fdaStats = response.data;
        });

        DrugInfo.getDrug($scope.drug.selected).then(function(response) {
            $scope.drug = response.data;
        });
    }
}]);
