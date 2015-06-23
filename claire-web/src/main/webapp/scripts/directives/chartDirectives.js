'use strict';

angular.module('claire.directives').
directive('pieChart', ["$window", function($window) {
    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            radialProgress(element[0])
                .id('cumulativeBlue')
                .diameter('200')
                .showLegend(false)

                .value(35, 0)
                .arcDesc('Positive', 0)

                .value(15, 1)
                .arcDesc('Negative', 1)

                .value(50, 2)
                .arcDesc('Neutral', 2)

                .theme('blue')
                .style('cumulative')
                .render();
        }
    };
}]).
directive('lineChart', ["DrugInfo", function(DrugInfo) {
    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            var data = scope[attrs.chartData];
            var options = {
                series: {
                    lines: { show: true },
                    bars: { show: false },
                    points: { show: true },
                    clickable: true,
                    hoverable: true
                },
                grid: {
                    show: true,
                    borderWidth: { top: 0, right: 0, bottom: 0, left: 0 },
                    borderColor: { top: "#DDD", left: "#FFF" },
                    clickable: true,
                    hoverable: true,
                    autoHighlight: true
                },
                colors: [ "#2166ac",
                          "#4393c3",
                          "#92c5de",
                          "#d1e5f0",
                          "#f7f7f7",
                          "#fddbc7",
                          "#f4a582",
                          "#d6604d",
                          "#b2182b" ],
                legend: {
                    show: true,
                    noColumns: 1,
                    position: "ne",
                    backgroundOpacity: 0
                },
                xaxis: {
                    mode: "time",
                    tickLength: 0
                },
                yaxis: {
                    min: 0,
                    font: { color: '#999' }
                }
            }

            options = {
                xaxis: {
                    mode: "time",
                    tickLength: 0
                }
            };

            // Convert the string dates into javascript Dates
            var transformDates = function(data) {
                for (var i = 0; i < data.length; i++) {
                    data[i][0] = new Date(data[i][0]);
                }
            };

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
                    points: { show: true }
                });
                transformDates(response.data.recalls);
                chartData.push({
                    data: response.data.recalls,
                    bars: { show: true }
                });
                $.plot(element[0], chartData, options);
            });
        }
    };
}]);
