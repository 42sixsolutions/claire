'use strict';

angular.module('claire.directives').
directive('pieChart', ["$window", function($window) {
    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            radialProgress(element[0])
                .id('cumulativeBlue')
                .label('Radial Label')
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
                .description('Radial Description?')
                .render();
        }
    };
}]).
directive('lineChart', ["$window", function($window) {
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
                    tickLength: 0,
                    min: 1388552400000,
                    max: 1417410000000
                },
                yaxis: {
                    min: 0,
                    font: { color: '#999' }
                }
            }
            console.log(data);
            var plot = $.plot("#line-chart", data, options);
            console.log(plot);
        }
    };
}]);
