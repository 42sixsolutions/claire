/**
 Copyright (c) 2014 BrightPoint Consulting, Inc.

 Permission is hereby granted, free of charge, to any person
 obtaining a copy of this software and associated documentation
 files (the "Software"), to deal in the Software without
 restriction, including without limitation the rights to use,
 copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the
 Software is furnished to do so, subject to the following
 conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 OTHER DEALINGS IN THE SOFTWARE.
 */
radial_chart_arcs = {};
radial_chart_paths = {};
radial_chart_outerRadiuses = {};

function radialProgressSml(parent) {
    var STYLES = {concentric: 'concentric', cumulative: 'cumulative', pie: 'pie'}
    var THEMES = {blue: 'blue', green: 'green'}

    var _data=null,
        _duration= 1000,
        _selection,
        _margin = {top:0, right:0, bottom:0, left:0},
        __width = 150,
        __height = 200,
        _diameter,
        _label='',
        _theme = THEMES.blue,
        _fontSize=10,
        _id = '',
        _showLegend = null,
        _centralLabel = null,
        _style = STYLES.concentric,
        radial_chart_arcDesc = {},
        _description = '';

    var _mouseClick;

    var _value= [],
        _minValue = 0,
        _maxValue = 100;

    var _currentArcs = [], _currentValue = 0
    _selection=d3.select(parent);

    //startAngle, endAngle should both be in radians
    //radians = (Math.PI/180)*degrees
    function createArc(startAngle, endAngle) {
        var arc = d3.svg.arc()
            .startAngle(startAngle) //just radians
            .endAngle(endAngle); //just radians
        radial_chart_arcs[_id].push(arc);
        _currentArcs.push(0);
        return endAngle
    }

    function createPath(svg, data, index, arc, id) {
        var path = svg.select(".arcs").selectAll(".arc" + index + "-" + _theme).data(data);
        var appendedPath = path.enter().append("path")
            .attr("class", "arc" + index % 5 + "-" + _theme)
            .attr("transform", "translate(" + _width/2 + "," + _width/2 + ")")
            .attr("d", arc);

        if (_showLegend || (_value.length > 2 && _showLegend == null) || _style == STYLES.cumulative || _style == STYLES.pie) {
            with({id: _id}) {
                appendedPath
                    .on("mouseenter", function() { setLabel(index, id) })
                    .on("mouseout", function() { mouseOut(id); })
            }
        }

        radial_chart_paths[id].push(appendedPath)
    }

    function mouseOut(id) {
        obj = $('#' + id + ' .labels .label')
        obj.hide()
    }

    function setLabel(arcIndex, id) {
        theme = (_style == STYLES.pie) ? 'white' : 'arc' + arcIndex + '-' + _theme
        obj = $('#' + id + ' .labels .label.central')
        obj.attr("class", "label central " + theme)
        obj.html(_value[arcIndex] + '%')
        obj.fadeIn()

        if (_style == STYLES.cumulative || _style == STYLES.pie) {
            descObj = $('#' + id + ' .labels .label.description')
            descObj.attr("class", "label description " + theme)
            descObj.html(radial_chart_arcDesc[arcIndex])
            descObj.fadeIn()
        }
    }

    function createLabels(label, svg) {
        if (_showLegend || (_value.length > 2 && _showLegend == null)) {
            svg = $(svg[0]);
            svg.css("display","inline-block");
            wrappedDiv = $(svg[0].parentNode).wrap("<div class='chart'></div>");
            wrappedDiv.append("<div class='radial_chart_legend'></div>");
            legend = wrappedDiv.find('.radial_chart_legend');

            for (i=0; i < _value.length; i++) {
                legend.append("<div class='radial_legend_bullet'><svg width='10' height='10'><rect width='10' height='10' class='arc" + i + "-" + _theme + "'/></svg> " + radial_chart_arcDesc[i] + "</div>")
            }
            y = (_style == STYLES.concentric) ? _width/2+_fontSize/3 : _width/2.2+_fontSize/3
            appendLabel(label, 0, _width/2, y, _fontSize, radial_chart_arcDesc[0], "none")
        }
        else if (_value.length == 1) {
            appendLabel(label, 0, _width/2, _width/2+_fontSize/3, _fontSize, radial_chart_arcDesc[0]);
        }
        else if (_value.length == 2) {
            appendLabel(label, 0, _width/3, _width/2+_fontSize/3, _fontSize * .75, radial_chart_arcDesc[0])
            appendLabel(label, 1, _width/3*2, _width/2+_fontSize/3, _fontSize *.75, radial_chart_arcDesc[1]);
        }
        else if (_style == STYLES.cumulative || _style == STYLES.pie) {
            y = _width/2.2+_fontSize/3
            appendLabel(label, 0, _width/2, y, _fontSize, radial_chart_arcDesc[0], "none")
        }
    }

    function appendLabel(label, index, x, y, fontSize, desc, display) {
        _centralLabel = label.enter().append("text")
            .attr("class","label central arc"+index + "-" + _theme)
            .attr("y",y)
            .attr("x",x)
            .attr("width",_width)
            .text(function (d) { return Math.round((_value[index]-_minValue)/(_maxValue-_minValue)*100) + "%" })
            .style("font-size",fontSize+"px")
            .style("display", display);
        if (desc) {
            label.enter().append("text")
                .attr("class","label description arc"+index + "-" + _theme)
                .attr("y",y + fontSize * 2 / 3)
                .attr("x",x)
                .attr("width",_width)
                .text(function (d) { return desc; })
                .style("font-size",fontSize *.6+"px")
                .style("display", display);
        }
    }

    function component() {

        _selection.each(function (data) {
            // Select the svg element, if it exists.
            var svg = d3.select(this).selectAll("svg.radial-svg").data([data]);

            var enter = svg.enter().append("svg").attr("class","radial-svg").append("g");

            measure();

            svg.attr("width", _width + 20)
                .attr("height", _height + _diameter *.2);


            var background = enter.append("g").attr("class","component")
                .on("click",onMouseClick);


            radial_chart_arcs[_id][0].endAngle(360 * (Math.PI/180))

            background.append("rect")
                .attr("class","background")
                .attr("width", _width)
                .attr("height", _height);

            background.append("path")
                .attr("transform", "translate(" + _width/2 + "," + _width/2 + ")")
                .attr("d", radial_chart_arcs[_id][0]);

            background.append("text")
                .attr("class", "label")
                .attr("transform", "translate(" + _width/2 + "," + (_width + _fontSize) + ")")
                .text(_label);

           var g = svg.select("g")
                .attr("transform", "translate(" + _margin.left + "," + _margin.top + ")");


            radial_chart_arcs[_id][0].endAngle(_currentArcs[0]);
            enter.append("g").attr("class", "arcs");

            radial_chart_paths[_id] = []
            for (var i=0; i < _value.length; i++) {
                createPath(svg, data, i, radial_chart_arcs[_id][i], _id);
            }


            enter.append("g").attr("class", "labels");

            var label = svg.select(".labels").selectAll(".label").data(data);
            createLabels(label, svg);

            layout(svg);

            function renderPath(index, id) {
                if (_style == STYLES.concentric) {
                    var ratio=(_value[index]-_minValue)/(_maxValue-_minValue);
                    var endAngle=Math.min(360*ratio,360);
                    endAngle=endAngle * Math.PI/180;
                }
                else if (_style == STYLES.cumulative || _style == STYLES.pie) {
                    endAngle = radial_chart_outerRadiuses[id][index+1];
                }


                callback = function(a) { return arcTween(a, index, id); }

                radial_chart_paths[id][index].datum(endAngle);
                radial_chart_paths[id][index].transition().duration(_duration)
                    .attrTween("d", callback);
                $(radial_chart_paths[id][index]).tooltip({
                    html: "test"
                });

                return ratio;
            }

            function layout(svg) {
                ratios = []
                for (index=0; index < _value.length; index++) {
                    ratio = renderPath(index, _id)
                    ratios.push(ratio);
                    label.datum(ratio * 100);
                }

            }

        });

        function onMouseClick(d) {
            if (typeof _mouseClick == "function") {
                _mouseClick.call();
            }
        }
    }

    function arcTween(a, index, id) {
        var i = d3.interpolate(_currentArcs[index], a);

        return function(t) {
            _currentArcs[index]=i(t);
            return radial_chart_arcs[id][index].endAngle(i(t))();
        };
    }

    function measure() {
        _width=_diameter - _margin.right - _margin.left - _margin.top - _margin.bottom;
        _height=_width;
        _fontSize=_width*.2;
        radial_chart_arcs[_id] = [];

        _widthOfArc = _width/2 * .12;

        if (_style == STYLES.concentric) {
            while (radial_chart_arcs[_id].length < _value.length)
                createArc(0,0);
            delta = _widthOfArc;
        }
        else if (_style == STYLES.cumulative || _style == STYLES.pie) {
            delta = 0;
            offset = 0;
            i = 0;
            radial_chart_outerRadiuses[_id] = [0]
            while (radial_chart_arcs[_id].length < _value.length) {
                ratio=(_value[i]-_minValue)/(_maxValue-_minValue);
                endAngle=convertDegreesToRadians(360*ratio) + offset;
                offset = createArc(offset,endAngle);
                startAngle = endAngle;
                endAngle = offset;
                radial_chart_outerRadiuses[_id].push(endAngle);
                i++;
            }

            if (_style == STYLES.pie)
                fixedInnerRadius = 0;
        }

        if (radial_chart_outerRadiuses[_id] == undefined)
            radial_chart_outerRadiuses[_id] = [];

        previousInnerRadius = _width/2;

        for (i = 0; i < radial_chart_arcs[_id].length; i++) {
            radial_chart_arcs[_id][i].outerRadius(previousInnerRadius);
            radial_chart_arcs[_id][i].innerRadius((typeof fixedInnerRadius === 'undefined') ? previousInnerRadius - _widthOfArc - 1 : 0);
            if (_style == STYLES.concentric) {
                radial_chart_outerRadiuses[_id].push(previousInnerRadius);
            }
            previousInnerRadius = previousInnerRadius - delta;
            //if(previousInnerRadius < 10)
            //    break;
        }

        ////GET RID OF DATA THAT WILL BE TOO SMALL TO DISPLAY
        //radial_chart_arcs = radial_chart_arcs.slice(0,i);
        //_currentArcs = _currentArcs.slice(0,i);
        //_value = _value.slice(0,i);
        //_arcDesc = _arcDesc.slice(0,i);
    }

    function convertDegreesToRadians(degrees) {
        return degrees * Math.PI / 180;
    }

    component.render = function() {
        component();
        return component;
    }

    component.value = function (_, index) {
        if (!arguments.length) return _value;
        if (index == 0) {
            _value = []
        }
        _value[index] = [_];
        _selection.datum([_value[index]]);
        return component;
    }

    component.margin = function(_) {
        if (!arguments.length) return _margin;
        _margin = _;
        return component;
    };

    component.diameter = function(_) {
        if (!arguments.length) return _diameter
        _diameter =  _;
        return component;
    };

    component.minValue = function(_) {
        if (!arguments.length) return _minValue;
        _minValue = _;
        return component;
    };

    component.maxValue = function(_) {
        if (!arguments.length) return _maxValue;
        _maxValue = _;
        return component;
    };

    component.label = function(_) {
        if (!arguments.length) return _label;
        _label = _;
        return component;
    };

    component.arcDesc = function(_, index) {
        if (!arguments.length) return radial_chart_arcDesc;
        radial_chart_arcDesc[index] = _;
        return component;
    };

    component._duration = function(_) {
        if (!arguments.length) return _duration;
        _duration = _;
        return component;
    };

    component.onClick = function (_) {
        if (!arguments.length) return _mouseClick;
        _mouseClick=_;
        return component;
    }

    component.showLegend = function(_) {
        if (!arguments.length) return _showLegend;
        _showLegend=_;
        return component;
    }

    component.id = function(_) {
        if (!arguments.length) return _id;
        _id=_;
        return component;
    }

    component.theme = function(_) {
        if (!arguments.length) return _theme;
        _theme=_;
        return component;
    }

    component.style = function(_) {
        if (!arguments.length) return _style;
        _style=_;
        return component;
    }

    component.description = function(_) {
        if (!arguments.length) return _description;
        _description=_;
        return component;
    }

    return component;

}

