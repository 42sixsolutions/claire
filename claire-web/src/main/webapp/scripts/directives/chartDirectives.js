'use strict';

angular.module('claire.directives').
directive('scatterPlot', ["$window", function($window) {
    return {
        restrict: "A",
        template: "<svg></svg>",
        link: function(scope, element, attrs) {
            

            var d3 = $window.d3;
            var rawSvg = element.find("svg")[0];
            var svg = d3.select(rawSvg);

            var margin = {top: 20, right: 20, bottom: 30, left: 40};
            var width = 960 - margin.left - margin.right;
            var height = 500 - margin.top - margin.bottom;

        }
    };
}]).
directive('bubbleChart', ["$window", function($window) {
    return {
        restrict: "A",
        template: "<svg></svg>",
        link: function(scope, element, attrs) {
            var dataToPlot = scope[attrs.chartData];

            var d3 = $window.d3;
            var rawSvg = element.find("svg")[0];
            var svg = d3.select(rawSvg);

            var diameter = 960;
            var format = d3.format(",d");
            var color = d3.scale.category20c();

            var bubble = d3.layout.pack()
                .sort(null)
                .size([diameter, diameter])
                .padding(1.5);

            svg.attr("width", diameter)
                .attr("height", diameter)
                .attr("class", "bubble");

            var node = svg.selectAll(".node")
                .data(bubble.nodes(classes(dataToPlot))
                    .filter(function(d) { return !d.children; }))
                .enter().append("g")
                .attr("class", "node")
                .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

            node.append("title")
                .text(function(d) { return d.className + ": " + format(d.value); });

            node.append("circle")
                .attr("r", function(d) { return d.r; })
                .style("fill", function(d) { return color(d.packageName); });

            node.append("text")
                .attr("dy", ".3em")
                .style("text-anchor", "middle")
                .text(function(d) { return d.className.substring(0, d.r / 3); });

            // Returns a flattened hierarchy containing all leaf nodes under the root.
            function classes(root) {
                var classes = [];

                function recurse(name, node) {
                    if (node.children) node.children.forEach(function(child) { recurse(node.name, child); });
                    else classes.push({packageName: name, className: node.name, value: node.size});
                }

                recurse(null, root);
                return {children: classes};
            }
        }
    };
}]);
