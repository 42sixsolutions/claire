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

        var convertAdverseData = function(chartData) {
            var newData = [];
            for (var i = 0; i < chartData.length; i++) {
                for (var j = 0; j < chartData[i][1]; j++) {
                    var item = [];
                    item.push(chartData[i][0]);
                    item.push(0);
                    item.push({ data: chartData[i][1] });
                    newData.push(item);
                }
            }
            return newData;
        }

        DrugInfo.getChart($scope.drug.selected).then(function(response) {
            var chartData = [];
            chartData.push({
                data: convertChartData(response.data.positiveTweets),
                lines: { show: true },
                curvedLines: { apply: true }
            });
            chartData.push({
                data: convertChartData(response.data.negativeTweets),
                lines: { show: true },
                curvedLines: { apply: true }
            });
            chartData.push({
                data: convertChartData(response.data.unknownTweets),
                lines: { show: true },
                curvedLines: { apply: true }
            });
            chartData.push({
                data: convertChartData(response.data.recalls),
                bars: { show: true, barWidth: 1, fill: true, fillColor: "rgba(0,0,0,0.2)" }
            });
            chartData.push({
                data: convertAdverseData(convertChartData(response.data.adverseEvents)),
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
