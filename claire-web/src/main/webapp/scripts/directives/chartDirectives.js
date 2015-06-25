'use strict';

angular.module('claire.directives').
directive('trianglify', ["$window", function($window) {
    var getTrianglifyPattern = function() {
        var pattern = Trianglify({
            height: $(window).height(),
            width: $(window).width(),
            x_colors: ["#f7fcf0", "#e0f3db", "#ccebc5", "#a8ddb5", "#7bccc4", "#4eb3d3", "#2b8cbe", "#0868ac", "#084081"],
            cell_size: 40});

        return pattern;
    };

    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            var pattern;

            if (!scope.isDetailsPage) {
                pattern = getTrianglifyPattern();
                $(element[0]).append(pattern.canvas());

                $(window).on("resize.doResize", function() {
                    element.find("canvas").remove();
                    pattern = getTrianglifyPattern();
                    $(element[0]).append(pattern.canvas());
                });

                scope.$on("$destroy", function() {
                    $(window).off("resize.doResize");
                });
            }
        }
    }
}]).
directive('pieChart', ["$window", function($window) {
    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            scope.$watch("twitterStats", function(newValue, oldValue) {
                if (newValue && newValue !== oldValue) {
                    radialProgress(element[0])
                        .id('cumulativeBlue')
                        .diameter('200')
                        .margin({top:0, right:0, bottom:0, left:0})
                        .showLegend(false)
                        
                        .value(newValue.percentNegative, 0)
                        .arcDesc('Negative', 0)

                        .value(newValue.percentUnknown, 1)
                        .arcDesc('Neutral', 1)

                        .value(newValue.percentPositive, 2)
                        .arcDesc('Positive', 2)

                        .theme('blue')
                        .style('cumulative')
                        .render();
                }
            });
        }
    };
}]).
directive('pieChartSml', ["$window", function($window) {
    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            scope.$watch("twitterStats", function(newValue, oldValue) {
                if (newValue && newValue !== oldValue) {
                  
                  var tweets = newValue.percentNegative * 100;
                  
                    radialProgress(element[0])
                        .id('negativeTweets')
                        .diameter('60')
                        .margin({top:0, right:0, bottom:0, left:0})
                        .showLegend(false)
                        
                        .value(newValue.tweets, 0)

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
            var options = {
                yaxis: {
                    min: 0,
                    max: 100
                }
            };
            scope.$watch("rankings", function(newValue, oldValue) {
                if (newValue && newValue !== oldValue) {
                    $.plot(element[0], newValue, options);
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
                    lines: { 
                        show: true,
                        lineWidth: 2,
                        steps: false,
                    },
                    curvedLines: {
                        active: true
                    },
                    clickable: true,
                    hoverable: true,
                    shadowSize: 0
                },
                grid: {
                    show: true,
                    color: "#CCC",
                    borderWidth: { top: 0, right: 0, bottom: 0, left: 0 },
                    clickable: true,
                    hoverable: true,
                    autoHighlight: true
                },
                tooltip: {
                    show: true,
                    content: "<span>%y%</span>",
                    defaultTheme: false,
                    shifts: {
                        x: -30,
                        y: -38
                    }
                },
                colors: [ 
                    "#e79090",
                    "#bee76f",
                    "#e0e0e0",
                    "rgba(0,0,0,0.2)",
                    "rgba(255,0,205,0.3)"
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
                    tickLength: 10,
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
                    min: 0
                },
                crosshair: {
                    mode: "x"
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
