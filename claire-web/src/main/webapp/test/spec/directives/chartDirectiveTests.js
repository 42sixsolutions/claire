'use strict';

describe('directives', function() {
    beforeEach(module('claire'));

    var scope;
    var element;

    var mockChartData = [];
    mockChartData.push({
        data: [[0, 12], [1, 52], [2, 99], [3, 7]]
    });

    describe('chartDirectives', function() {
        beforeEach(inject(function($rootScope, $compile) {
            scope = $rootScope.$new();

            element = '<div line-chart style="width: 100px; height: 100px"></div>';
            element = $compile(element)(scope);

            scope.$digest();
        }));

        it('should draw a chart when the chart data is set', function() {
            scope.mainChartData = mockChartData;
            scope.drugChartOptions = {};
            scope.drugChartOptions.min = 0;
            scope.drugChartOptions.max = 100;
            scope.$digest();

            expect(element.find('canvas')).not.toBe(undefined);
        });
    });
});
