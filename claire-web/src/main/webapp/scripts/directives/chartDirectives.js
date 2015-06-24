'use strict';

angular.module('claire.directives').
directive('pieChart', ["$window", function($window) {
    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            scope.$watch("twitterStats", function(newValue, oldValue) {
                if (newValue && newValue !== oldValue) {
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
directive('rankingsChart', [function() {
    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            $.plot(element[0], [
                { data: [[1, 100], [1, 75], [1, 50], [1, 25], [1, 0]],  points: { show: true },  lines: { show: true }   },
                { label: "- Tweets",     data: [[1, 75]],     points: { show: true, symbol: "cross", radius: 10 }   },
                { data: [[2, 100], [2, 75], [2, 50], [2, 25], [2, 0]],     points: { show: true },     lines: { show: true }   },
                { label: "+ Tweets",     data: [[2, 50]],     points: { show: true, symbol: "cross", radius: 10 }   },
                { data: [[3, 100], [3, 75], [3, 50], [3, 25], [3, 0]],     points: { show: true },     lines: { show: true }   },
                { label: "~ Tweets",     data: [[3, 100]],     points: { show: true, symbol: "cross", radius: 10 }   },
                {data: [[4, 100], [4, 75], [4, 50], [4, 25], [4, 0]],     points: { show: true },     lines: { show: true }   },
                {label: "Adverse Events",     data: [[4, 0]],     points: { show: true, symbol: "cross", radius: 10 }   },
                {data: [[5, 100], [5, 75], [5, 50], [5, 25], [5, 0]],     points: { show: true },     lines: { show: true }   },
                {label: "Recalls",     data: [[5, 0]],     points: { show: true, symbol: "cross", radius: 10 }   }]); 
        }
    }
}]).
directive('lineChart', [function() {
    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            var options = {
                series: {
                    lines: { 
                      show: true,
                      lineWidth: 2,
                      steps: false
                    },
                    points: { 
                      show: true,
                      radius: 2,
                      symbol: "circle",
                      lineWidth: 2,
                      fill: true,
                      fillColor: null
                    },
                    clickable: true,
                    hoverable: true,
                    shadowSize: 0,
                    curvedLines: {  
                      active: false, 
                      apply: true,
                    }
                },
                grid: {
                    show: true,
                    color: "#CCC",
                    borderWidth: { top: 0, right: 0, bottom: 0, left: 0 },
                    clickable: true,
                    hoverable: true,
                    autoHighlight: true
                },
                colors: [ 
                    "#e79090",
                    "#bee76f",
                    "#e0e0e0",
                    "rgba(0,0,0,0.2)",
                    "rgba(255,0,205,0.3)",
                ],
                xaxis: {
                    font: { 
                      size: 11,
                      lineHeight: 16,
                      weight: "300",
                      family: "Raleway",
                      color: "#444"
                    },
                    mode: "time",
                    tickLength: 0,
                    reserveSpace: true
                },
                yaxis: {
                    font: { 
                      size: 11,
                      lineHeight: 16,
                      weight: "300",
                      family: "Raleway",
                      color: "#444"
                    },
                    min: 0,
                    max: 100
                }
            };

            scope.$watch('mainChartData', function(newValue, oldValue) {
                if (newValue && newValue !== oldValue) {
                    $.plot(element[0], newValue, options);                    
                }
            });
        }
    };
}]);
