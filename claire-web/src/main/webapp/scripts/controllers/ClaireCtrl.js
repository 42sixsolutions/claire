'use strict';

angular.module('claire.controllers').controller('ClaireCtrl', ["$scope", "$location", "$timeout", "DrugInfo", "Trends",
        function($scope, $location, $timeout, DrugInfo, Trends) {

    $scope.drug = {};
    $scope.trends = {
        topPositive: [],
        topNegative: [],
        mostAdverseEvents: []
    };

    var pathParts = $location.path().split("/detail/");
    $scope.isDetailsPage = pathParts.length > 1;

    $scope.onSearch = function(drug) {
        if (drug) {
            $location.path("/detail/" + drug);            
        }
    };

    $scope.$watch("drug.selected", function(newValue, oldValue) {
        if (newValue && newValue !== oldValue) {
            $scope.onSearch(newValue);
        }
    });

    DrugInfo.getDrugList().then(function(response) {
        $scope.drugList = response.data;
        $timeout(function() {
            $('.chosen-select').chosen();
        });
    });
    
    if (!$scope.isDetailsPage) {
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

    if ($scope.isDetailsPage) {
        $scope.drug.selected = pathParts[1];
    }

    if ($scope.isDetailsPage) {
        DrugInfo.getTwitterStats($scope.drug.selected).then(function(response) {
            $scope.twitterStats = response.data;
        });

        var convertChartData = function(chartData) {
            var newData = [];
            for (var i = 0; i < chartData.length; i++) {
                var item = [];
                // Convert the string dates into javascript Dates
                item.push(new Date(chartData[i].date));
                item.push(chartData[i].percentMax);
                newData[i] = item;
            }
            return newData;
        };

        var adjustData = function(chartData, y) {
            var newData = [];
            for (var i = 0; i < chartData.length; i++) {
                for (var j = 0; j < chartData[i][1]; j++) {
                    var item = [];
                    item.push(chartData[i][0]);
                    item.push(y);
                    item.push({ data: chartData[i][1] });
                    newData.push(item);
                }
            }
            return newData;
        }

        var adjustRecallData = function(recalls, max) {
            var newData = [];
            for (var i = 0; i < recalls.length; i++) {
                var item = [];
                item.push(recalls[i][0]);
                item.push(max);
                item.push({ data: recalls[i][1] });
                newData[i] = item;
            }
            return newData;
        }

        var getMaxTweets = function(positive, negative, unknown) {
            var getMax = function(list) {
                var max = 0;
                for (var i = 0; i < list.length; i++) {
                    if (max < list[i][1]) {
                        max = list[i][1];
                    }
                }
                return max;
            }

            var positiveMax = getMax(positive);
            var negativeMax = getMax(negative);
            var unknownMax = getMax(unknown);

            return Math.max(positiveMax, negativeMax, unknownMax);
        }

        $scope.drugChartOptions = {};

        DrugInfo.getChart($scope.drug.selected).then(function(response) {
            var chartData = [];
            var positive = convertChartData(response.data.positiveTweets);
            var negative = convertChartData(response.data.negativeTweets);
            var unknown = convertChartData(response.data.unknownTweets);
            var max = getMaxTweets(positive, negative, unknown);
            console.log(max);
            $scope.drugChartOptions.min = -Math.floor(max / 14);
            $scope.drugChartOptions.max = max + Math.floor(max / 14);
            console.log($scope.drugChartOptions.min);

            chartData.push({
                data: positive,
                lines: { show: true },
                curvedLines: { apply: true }
            });
            chartData.push({
                data: negative,
                lines: { show: true },
                curvedLines: { apply: true }
            });
            chartData.push({
                data: unknown,
                lines: { show: true },
                curvedLines: { apply: true }
            });
            chartData.push({
                data: adjustRecallData(convertChartData(response.data.recalls), $scope.drugChartOptions.max),
                bars: { show: true, barWidth: 1, fill: true, fillColor: "#d54dde" },
                lines: { show: false }
            });
            chartData.push({
                data: adjustData(convertChartData(response.data.adverseEvents), $scope.drugChartOptions.min),
                points: { show: true, radius: 6, lineWidth: 0, fill: true, fillColor: "rgba(255,0,205,0.01)" },
                lines: { show: false }
            });
            $scope.mainChartData = chartData;
        });

        DrugInfo.getFDAStats($scope.drug.selected).then(function(response) {
            $scope.fdaStats = response.data;
        });

        DrugInfo.getDrug($scope.drug.selected).then(function(response) {
            $scope.drug = response.data;
        });

        var transformRankingsColumn = function(data, columnId) {
            var column = [];
            for (var i = 0; i < data.length; i++) {
                var dataPoint = [columnId, 101 - data[i].ranking];
                column.push(dataPoint);
            }
            return column;
        };

        var getSelectedDataPoint = function(data, columnId) {
            var selected = [];
            for (var i = 0; i < data.length; i++) {
                var dataPoint = [columnId, 101 - data[i].ranking];
                if (data[i].currentDrug) {
                    selected.push(dataPoint);
                }
            }
            return selected;
        }

        DrugInfo.getRankings($scope.drug.selected).then(function(response) {
            $scope.drugRankings = response.data;
        });
    }
    
}]);
