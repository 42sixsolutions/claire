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
    var isDetailsPage = pathParts.length > 1;

    $scope.onSearch = function(drug) {
        if (drug) {
            $location.path("/detail/" + drug);            
        }
    };

    DrugInfo.getDrugList().then(function(response) {
        $scope.drugList = response.data;
        $timeout(function() {
            $('.chosen-select').chosen();
        });
    });
    
    if (!isDetailsPage) {
        $scope.body = { className: "home" };

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
        DrugInfo.getTwitterStats($scope.drug.selected).then(function(response) {
            $scope.twitterStats = response.data;
        });

        var convertChartData = function(chartData) {
            var newData = [];
            for (var i = 0; i < chartData.length; i++) {
                var item = [];
                // Convert the string dates into javascript Dates
                item[0] = new Date(chartData[i].date);
                item[1] = chartData[i].percentMax;
                newData[i] = item;
            }
            return newData;
        };

        DrugInfo.getChart($scope.drug.selected).then(function(response) {
            var chartData = [];
            chartData.push({
                data: convertChartData(response.data.positiveTweets),
                lines: { show: true }
            });
            chartData.push({
                data: convertChartData(response.data.negativeTweets),
                lines: { show: true }
            });
            chartData.push({
                data: convertChartData(response.data.unknownTweets),
                lines: { show: true }
            });
            chartData.push({
                data: convertChartData(response.data.recalls),
                bars: { show: true, barWidth: 1, fill: true, fillColor: "#eee" }
            });
            chartData.push({
                data: convertChartData(response.data.adverseEvents),
                points: { show: true, radius: 6, lineWidth: 0, fill: true, fillColor: "rgba(255,0,205,0.3)" }
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
            var rankingsData = [];
            var columnId = 1;
            rankingsData.push({
                data: transformRankingsColumn(response.data.negativeTweets, columnId),
                points: { show: true }, 
                lines: { show: true } 
            });
            rankingsData.push({ 
                label: "- Tweets", 
                data: getSelectedDataPoint(response.data.negativeTweets, columnId), 
                points: { show: true, symbol: "cross", radius: 10 }
            } );
            columnId++;
            rankingsData.push({
                data: transformRankingsColumn(response.data.positiveTweets, columnId),
                points: { show: true }, 
                lines: { show: true } 
            });
            rankingsData.push({ 
                label: "+ Tweets", 
                data: getSelectedDataPoint(response.data.positiveTweets, columnId), 
                points: { show: true, symbol: "cross", radius: 10 }
            } );
            columnId++;
            rankingsData.push({
                data: transformRankingsColumn(response.data.neutralTweets, columnId),
                points: { show: true }, 
                lines: { show: true } 
            });
            rankingsData.push({ 
                label: "~ Tweets", 
                data: getSelectedDataPoint(response.data.neutralTweets, columnId), 
                points: { show: true, symbol: "cross", radius: 10 }
            } );
            columnId++;
            rankingsData.push({
                data: transformRankingsColumn(response.data.adverseEvents, columnId),
                points: { show: true }, 
                lines: { show: true } 
            });
            rankingsData.push({ 
                label: "Adverse Events", 
                data: getSelectedDataPoint(response.data.adverseEvents, columnId), 
                points: { show: true, symbol: "cross", radius: 10 }
            } );
            columnId++;
            rankingsData.push({
                data: transformRankingsColumn(response.data.recalls, columnId),
                points: { show: true }, 
                lines: { show: true } 
            });
            rankingsData.push({ 
                label: "Recalls", 
                data: getSelectedDataPoint(response.data.recalls, columnId), 
                points: { show: true, symbol: "cross", radius: 10 }
            } );

            $scope.rankings = rankingsData;
        });
    }
    
}]);
