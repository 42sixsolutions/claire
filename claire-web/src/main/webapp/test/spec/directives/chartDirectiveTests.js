'use strict';

describe('directives', function() {
    beforeEach(module('claire'));

    var scope;
    var element;

    var mockChartData = [];
    mockChartData.push({
        data: [[0, 12], [1, 52], [2, 99], [3, 7]]
    });

    var mockTwitterStats = { percentNegative: 88.4, percentPositive: 2.8, percentUnknown: 8.8, totalTweets: 1990 };

    describe('line-chart', function() {
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

    describe('pie-chart', function() {
        beforeEach(inject(function($rootScope, $compile) {
            scope = $rootScope.$new();

            element = '<div pie-chart style="width: 100px; height: 100px"></div>';
            element = $compile(element)(scope);

            scope.$digest();
        }));

        it('should draw a chart when the twitter stats are set', function() {
            scope.twitterStats = mockTwitterStats;
            scope.$digest();

            expect(element.find('svg')).not.toBe(undefined);
            expect(element.find('svg').children()).not.toBe(undefined);
            expect(element.find('svg').children().length > 0).toBe(true);
        });
    });

    describe('pie-chart-sml', function() {
        beforeEach(inject(function($rootScope, $compile) {
            scope = $rootScope.$new();

            element = '<div pie-chart-sml style="width: 100px; height: 100px"></div>';
            element = $compile(element)(scope);

            scope.$digest();
        }));

        it('should draw a small pie chart when the twitter stats are set', function() {
            scope.twitterStats = mockTwitterStats;
            scope.$digest();

            expect(element.find('svg')).not.toBe(undefined);
            expect(element.find('svg').children()).not.toBe(undefined);
            expect(element.find('svg').children().length > 0).toBe(true);
        });
    });

    describe('trianglify', function() {
        beforeEach(inject(function($rootScope, $compile) {
            scope = $rootScope.$new();

            element = '<div trianglify style="width: 100px; height: 100px"></div>';
            element = $compile(element)(scope);

            scope.$digest();
        }));

        it('should draw a trianglify pattern', function() {
            expect(element.find('canvas')).not.toBe(undefined);
        });
    });    
});
