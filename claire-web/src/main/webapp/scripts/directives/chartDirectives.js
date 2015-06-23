'use strict';

angular.module('claire.directives').
directive('pieChart', ["$window", function($window) {
    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            scope.$watch("twitterStats", function(newValue) {
                if (newValue) {
                    radialProgress(element[0])
                        .id('cumulativeBlue')
                        .diameter('200')
                        .showLegend(false)

                        .value(newValue.percentPositive, 0)
                        .arcDesc('Positive', 0)

                        .value(newValue.percentNegative, 1)
                        .arcDesc('Negative', 1)

                        .value(newValue.percentUnknown, 2)
                        .arcDesc('Neutral', 2)

                        .theme('blue')
                        .style('cumulative')
                        .render();
                }
            });
        }
    };
}]).
directive('lineChart', [function() {
    return {
        restrict: "A",
        link: function(scope, element, attrs) {
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
                    font: { color: '#999' },
                    mode: "time",
                    tickLength: 0
                },
                yaxis: {
                    font: { color: '#999' },
                    min: 0,
                    max: 100
                }
            };

            scope.$watch('mainChartData', function(newValue) {
                if (newValue) {
                    $.plot(element[0], newValue, options);                    
                }
            });
        }
    };
}]);
