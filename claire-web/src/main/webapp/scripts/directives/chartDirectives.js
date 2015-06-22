'use strict';

angular.module('claire.directives').
directive('timeseries', ["$window", function($window) {
    return {
        restrict: "A",
        template: "<svg></svg>",
        link: function(scope, element, attrs) {
            var dataToPlot = scope[attrs.chartData];

            var d3 = $window.d3;
            var rawSvg = element.find("svg")[0];
            var svg = d3.select(rawSvg);

            function getDate(d) {
                var date = moment(d);
                date.hour(1);
                date.minute(0);
                date.second(0);
                return date.valueOf();
            }

            function render() {
                var dates = _.pluck(dataToPlot, 'value');

                var margin = {
                    top: 10,
                    right: 25,
                    bottom: 15,
                    left: 35
                }
                var width = 900 - margin.left - margin.right;
                var height = 50 - margin.top - margin.bottom;

                var x = d3.time.scale().range([0 + margin.right, width - margin.left]);

                var ticks = width > 800 ? 8 : 4;

                x.domain(d3.extent([_.min(dates), _.max(dates)]));

                var xFormat, yFormat;
                xFormat = "%m/%d/%y";

                var xAxis = d3.svg.axis().scale(x).orient("bottom")
                    .ticks(ticks)
                    .tickSize(-height, 0)
                    .tickFormat(d3.time.format(xFormat));

                svg.attr("width", width + margin.left + margin.right)
                    .attr("height", height + margin.top + margin.bottom);

                var context = svg.append("g")
                    .attr("class", "context")
                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

                context.append("g")
                    .attr("class", "x axis")
                    .attr("transform", "translate(" + margin.left + "," + (margin.top + (height - margin.bottom)) + ")")
                    .call(xAxis);

                var circles = context.append("g")
                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")")

                circles.selectAll(".circ")
                    .data(dataToPlot)
                    .enter().append("circle")
                    .attr("class", "circ")
                    .attr("cx", function(d) {
                        return x(getDate(d.value));
                    })
                    .attr("r", 9)
                    .on("click", function(d) {
                        console.log(new Date(d.value));
                    })
            }

            render();
        }
    };
}]).
directive('lineChart', ["$window", function($window) {
    return {
        restrict: "A",
        template: "<svg></svg>",
        link: function(scope, element, attrs) {
            var dataToPlot = scope[attrs.chartData];

            var d3 = $window.d3;
            var rawSvg = element.find("svg")[0];
            var svg = d3.select(rawSvg);

            //set the margins
            var margin = {
                top: 10, 
                right: 50, 
                bottom: 15, 
                left: 35
            };
            var width = 900 - margin.left - margin.right;
            var height = 500 - margin.top - margin.bottom;

            // set the type of number here, n is a number with a comma, .2% will get you a percent, .2f will get you 2 decimal points
            var NumbType = d3.format(".2f");

            // color array
            var bluescale4 = ["#8BA9D0", "#6A90C1", "#066CA9", "#004B8C"];

            //color function pulls from array of colors stored in color.js
            var color = d3.scale.ordinal().range(bluescale4);

            //define the approx. number of x scale ticks
            var xscaleticks = 5;

            //defines a function to be used to append the title to the tooltip.  you can set how you want it to display here.
            var maketip = function (d) {                           
                var tip = '<p class="tip3">' + d.name + '<p class="tip1">' + NumbType(d.value) + '</p> <p class="tip3">'+  formatDate(d.date)+'</p>';
                return tip;
            }
               
            //define your year format here, first for the x scale, then if the date is displayed in tooltips
            var parseDate = d3.time.format("%m/%d/%y").parse;
            var formatDate = d3.time.format("%b %d, '%y");

            //adjust the created SVG
            svg.attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom)
                .append("g")
                .attr("transform", "translate(" + margin.left + "," + margin.top + ")");  
        
            //make a rectangle so there is something to click on
            svg.append("svg:rect")
                .attr("width", width)
                .attr("height", height)
                .attr("class", "plot");

             //make a clip path for the graph  
             var clip = svg.append("svg:clipPath")
                .attr("id", "clip")
                .append("svg:rect")
                .attr("x", 0)
                .attr("y", 0)
                .attr("width", width)
                .attr("height", height);   
    
            // force data to update when menu is changed    
            var menu = d3.select("#menu select")
                .on("change", change);    
                
            //suck in the data, store it in a value called formatted, run the redraw function
            var formatted = dataToPlot;
            redraw();

            d3.select(window)
                .on("keydown", function() { altKey = d3.event.altKey; })
                .on("keyup", function() { altKey = false; });
            var altKey;

            // set terms of transition that will take place
            // when a new economic indicator is chosen   
            function change() {
                d3.transition()
                    .duration(altKey ? 7500 : 1500)
                    .each(redraw);
            }

            // all the meat goes in the redraw function
            function redraw() { 

                // create data nests based on economic indicator (series)
                var nested = d3.nest()
                    .key(function(d) { return d.type; })
                    .map(formatted)
                
                // get value from menu selection
                // the option values are set in HTML and correspond
                //to the [type] value we used to nest the data  
                var series = "val1";
                
                // only retrieve data from the selected series, using the nest we just created
                var data = nested[series];
                
                // for object constancy we will need to set "keys", one for each type of data (column name) exclude all others.
                color.domain(d3.keys(data[0]).filter(function(key) { return (key !== "date" && key !== "type"); }));

                var linedata = color.domain().map(function(name) {
                    return { name: name,
                        values: data.map(function(d) {
                            return {name:name, date: parseDate(d.date), value: parseFloat(d[name],10)};
                        })
                    };
                });

            
                //make an empty variable to stash the last values into so i can sort the legend
                var lastvalues=[];

                //setup the x and y scales
                var x = d3.time.scale()
                    .domain([
                        d3.min(linedata, function(c) { return d3.min(c.values, function(v) { return v.date; }); }),
                        d3.max(linedata, function(c) { return d3.max(c.values, function(v) { return v.date; }); })
                    ])
                    .range([0, width]);

                var y = d3.scale.linear()
                    .domain([
                        d3.min(linedata, function(c) { return d3.min(c.values, function(v) { return v.value; }); }),
                        d3.max(linedata, function(c) { return d3.max(c.values, function(v) { return v.value; }); })
                    ])
                    .range([height, 0]);

                //will draw the line        
                var line = d3.svg.line()
                    .x(function(d) { return x(d.date); })
                    .y(function(d) { return y(d.value); });

                //define the zoom
                var zoom = d3.behavior.zoom()
                    .x(x)
                    .y(y)
                    .scaleExtent([1,8])
                    .on("zoom", zoomed);

                //call the zoom on the SVG
                svg.call(zoom);

                //create and draw the x axis
                var xAxis = d3.svg.axis()
                    .scale(x)
                    .orient("bottom")
                    .tickPadding(8)
                    .ticks(xscaleticks);
                
                svg.append("svg:g")
                    .attr("class", "x axis");

                //create and draw the y axis                  
                var yAxis = d3.svg.axis()
                    .scale(y)
                    .orient("left")
                    .tickSize(0-width)
                    .tickPadding(8);
                
                svg.append("svg:g")
                    .attr("class", "y axis");

                //bind the data
                var thegraph = svg.selectAll(".thegraph")
                    .data(linedata)
             
                //append a g tag for each line and set of tooltip circles and give it a unique ID based on the column name of the data     
                var thegraphEnter=thegraph.enter().append("g")
                    .attr("clip-path", "url(#clip)")
                    .attr("class", "thegraph")
                    .attr('id',function(d){ return d.name+"-line"; })
                    .style("stroke-width",2.5)
                    .on("mouseover", function (d) {                                  
                        d3.select(this)                          //on mouseover of each line, give it a nice thick stroke
                        .style("stroke-width",'6px');
                        
                        var selectthegraphs = $('.thegraph').not(this);     //select all the rest of the lines, except the one you are hovering on and drop their opacity
                        d3.selectAll(selectthegraphs)
                            .style("opacity",0.2);
                        
                        var getname = document.getElementById(d.name);    //use get element cause the ID names have spaces in them
                        var selectlegend = $('.legend').not(getname);    //grab all the legend items that match the line you are on, except the one you are hovering on

                        d3.selectAll(selectlegend)    // drop opacity on other legend names
                            .style("opacity",.2);

                        d3.select(getname)
                            .attr("class", "legend-select");  //change the class on the legend name that corresponds to hovered line to be bolder           
                    })
                    .on("mouseout", function(d) {        //undo everything on the mouseout
                        d3.select(this)
                            .style("stroke-width",'2.5px');
                        
                        var selectthegraphs = $('.thegraph').not(this);
                        d3.selectAll(selectthegraphs)
                            .style("opacity",1);
                        
                        var getname = document.getElementById(d.name);
                        var getname2= $('.legend[fakeclass="fakelegend"]')
                        var selectlegend = $('.legend').not(getname2).not(getname);

                        d3.selectAll(selectlegend)
                            .style("opacity",1);
                        
                        d3.select(getname)
                            .attr("class", "legend");           
                    });

                //actually append the line to the graph
                thegraphEnter.append("path")
                    .attr("class", "line")
                    .style("stroke", function(d) { return color(d.name); })
                    .attr("d", function(d) { return line(d.values[0]); })
                    .transition()
                    .duration(2000)
                    .attrTween('d',function (d){
                        var interpolate = d3.scale.quantile()
                            .domain([0,1])
                            .range(d3.range(1, d.values.length+1));
                        return function(t){
                            return line(d.values.slice(0, interpolate(t)));
                        };
                    });
      
                //then append some 'nearly' invisible circles at each data point  
                thegraph.selectAll("circle")
                    .data( function(d) {return(d.values);} )
                    .enter()
                    .append("circle")
                        .attr("class","tipcircle")
                        .attr("cx", function(d,i){return x(d.date)})
                        .attr("cy",function(d,i){return y(d.value)})
                        .attr("r",12)
                        .style('opacity', 1e-6)
                        .attr ("title", maketip);

                //append the legend
                var legend = svg.selectAll('.legend')
                    .data(linedata);
        
                var legendEnter=legend
                    .enter()
                    .append('g')
                    .attr('class', 'legend')
                    .attr('id',function(d){ return d.name; })
                    .on('click', function (d) {                           //onclick function to toggle off the lines            
                        if($(this).css("opacity") == 1){                  //uses the opacity of the item clicked on to determine whether to turn the line on or off         

                            var elemented = document.getElementById(this.id +"-line");   //grab the line that has the same ID as this point along w/ "-line"  use get element cause ID has spaces
                            d3.select(elemented)
                                .transition()
                                .duration(1000)
                                .style("opacity",0)
                                .style("display",'none');
                        
                            d3.select(this)
                                .attr('fakeclass', 'fakelegend')
                                .transition()
                                .duration(1000)
                                .style ("opacity", .2);
                        } else {
                        
                            var elemented = document.getElementById(this.id +"-line");
                            d3.select(elemented)
                                .style("display", "block")
                                .transition()
                                .duration(1000)
                                .style("opacity",1);
                        
                            d3.select(this)
                                .attr('fakeclass','legend')
                                .transition()
                                .duration(1000)
                                .style ("opacity", 1);}
                    });

                //create a scale to pass the legend items through
                var legendscale= d3.scale.ordinal()
                            .domain(lastvalues)
                            .range([0,30,60,90,120,150,180,210]);

                //actually add the circles to the created legend container
                legendEnter.append('circle')
                    .attr('cx', width +20)
                    .attr('cy', function(d){return legendscale(d.values[d.values.length-1].value);})
                    .attr('r', 7)
                    .style('fill', function(d) { 
                        return color(d.name);
                    });
                                    
                //add the legend text
                legendEnter.append('text')
                    .attr('x', width+35)
                    .attr('y', function(d){return legendscale(d.values[d.values.length-1].value);})
                    .text(function(d){ return d.name; });

                // set variable for updating visualization
                var thegraphUpdate = d3.transition(thegraph);
                
                // change values of path and then the circles to those of the new series
                thegraphUpdate.select("path")
                    .attr("d", function(d, i) {       
                  
                        //must be a better place to put this, but this works for now
                        lastvalues[i]=d.values[d.values.length-1].value;         
                        lastvalues.sort(function (a,b){return b-a});
                        legendscale.domain(lastvalues);
                    
                        return line(d.values); });
                  
                thegraphUpdate.selectAll("circle")
                    .attr ("title", maketip)
                    .attr("cy",function(d,i){return y(d.value)})
                    .attr("cx", function(d,i){return x(d.date)});


                  // and now for legend items
                  var legendUpdate=d3.transition(legend);
                  
                legendUpdate.select("circle")
                    .attr('cy', function(d, i){  
                        return legendscale(d.values[d.values.length-1].value);});

                legendUpdate.select("text")
                    .attr('y',  function (d) {return legendscale(d.values[d.values.length-1].value);});


                // update the axes,   
                d3.transition(svg).select(".y.axis")
                    .call(yAxis);   
                      
                d3.transition(svg).select(".x.axis")
                    .attr("transform", "translate(0," + height + ")")
                    .call(xAxis);

                //define the zoom function
                function zoomed() {
             
                    svg.select(".x.axis").call(xAxis);
                    svg.select(".y.axis").call(yAxis);

                    svg.selectAll(".tipcircle")
                        .attr("cx", function(d,i){return x(d.date)})
                        .attr("cy",function(d,i){return y(d.value)});
                        
                    svg.selectAll(".line")
                        .attr("class","line")
                        .attr("d", function (d) { return line(d.values)});
                }

            //end of the redraw function
            }
        }
    };
}]);
