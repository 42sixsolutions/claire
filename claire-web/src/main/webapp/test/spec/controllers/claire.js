'use strict';

describe('routes', function() {
    beforeEach(module('claire'));

    it('should map routes to controllers', function() {
        inject(function($route) {

            expect($route.routes['/'].controller).toBe('ClaireCtrl');
            expect($route.routes['/'].templateUrl).toBe('partials/home.html');

            expect($route.routes['/detail/:drug'].controller).toBe('ClaireCtrl');
            expect($route.routes['/detail/:drug'].templateUrl).toBe('partials/detail.html');

            // otherwise redirect to
            expect($route.routes[null].redirectTo).toBe('/')
        });
    });    
});

describe('controllers', function() {
    beforeEach(module('claire'));

    var DrugInfo;
    var mockDrugListData = ['aspirin', 'claritin', 'benadryl'];
    beforeEach(inject(function(_DrugInfo_, $q) {
        DrugInfo = _DrugInfo_;
        var deferred = $q.defer();
        deferred.resolve({ data: mockDrugListData });
        spyOn(DrugInfo, "getDrugList").and.returnValue(deferred.promise);
    }));

    var Trends;
    var mockTrendsPositive = [{
        brandName: "Plavix",
        percentPositive: 99,
        percentNegative: 0
    }];
    var mockTrendsNegative = [{
        brandName: "Lipitor",
        percentPositive: 4,
        percentNegative: 99
    }];
    var mockTrendsAdverse = [{
        brandName: "Amoxil",
        count: 5676
    }];
    beforeEach(inject(function(_Trends_, $q) {
        Trends = _Trends_;
        var positive = $q.defer();
        positive.resolve({ data: mockTrendsPositive });
        spyOn(Trends, "getTopPositive").and.returnValue(positive.promise);
        var negative = $q.defer();
        negative.resolve({ data: mockTrendsNegative });
        spyOn(Trends, "getTopNegative").and.returnValue(negative.promise);
        var adverse = $q.defer();
        adverse.resolve({ data: mockTrendsAdverse });
        spyOn(Trends, "getMostAdverseEvents").and.returnValue(adverse.promise);
    }));

    var $controller = null;
    beforeEach(inject(function(_$controller_) {
        $controller = _$controller_;
    }));

    describe('ClaireController', function() {
        var $scope, ctrl;
        beforeEach(inject(function($rootScope) {
            $scope = $rootScope.$new();
            ctrl = $controller("ClaireCtrl", { $scope: $scope, DrugInfo: DrugInfo, Trends: Trends });
        }));

        it('should exist', function() {
            expect(!!ctrl).toBe(true);
        });

        describe('when created', function() {
            it('should define a drug property', function() {
                expect($scope.drug instanceof Object).toBe(true);
            });

            it('should define a trends property', function() {
                expect($scope.trends instanceof Object).toBe(true);
                expect($scope.trends.topPositive instanceof Array).toBe(true);
                expect($scope.trends.topNegative instanceof Array).toBe(true);
                expect($scope.trends.mostAdverseEvents instanceof Array).toBe(true);
            });

            it('should define a search method', function() {
                expect(typeof $scope.onSearch).toBe('function');
            });

            it('should populate the drug list', function() {
                $scope.$digest();

                expect(DrugInfo.getDrugList).toHaveBeenCalled();
                expect($scope.drugList).toBe(mockDrugListData);
            });

            it('should get the positive trend', function() {
                $scope.$digest();

                expect(Trends.getTopPositive).toHaveBeenCalled();
                expect($scope.trends.topPositive).toBe(mockTrendsPositive);
            });

            it('should get the negative trend', function() {
                $scope.$digest();

                expect(Trends.getTopNegative).toHaveBeenCalled();
                expect($scope.trends.topNegative).toBe(mockTrendsNegative);
            });

            it('should get the most adverse trend', function() {
                $scope.$digest();

                expect(Trends.getMostAdverseEvents).toHaveBeenCalled();
                expect($scope.trends.mostAdverseEvents).toBe(mockTrendsAdverse);
            });
        });
    });
});
