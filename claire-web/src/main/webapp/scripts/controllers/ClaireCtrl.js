'use strict';

angular.module('claire.controllers').controller('ClaireCtrl', ["$scope", "$location", "DrugInfo",
        function($scope, $location, DrugInfo) {

    $scope.drug = {};

    var pathParts = $location.path().split("/detail/");
    var isDetailsPage = pathParts.length > 1;
    if (isDetailsPage) {
        $scope.drug.selected = pathParts[1];
    }

    $scope.onSearch = function(drug) {
        $location.path("/detail/" + drug);
    };

    $scope.$watch("drug.selected", function(newValue, oldValue) {
        if (newValue && newValue !== oldValue) {
            if (isDetailsPage) {
                $location.path("/detail/" + newValue);
            }
        }
    })

    // Convert the string dates into javascript Dates
    var transformDates = function(data) {
        for (var i = 0; i < data.length; i++) {
            data[i][0] = new Date(data[i][0]);
        }
    };

    if ($scope.drug.selected) {
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
