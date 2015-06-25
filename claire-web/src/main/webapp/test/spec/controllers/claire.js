'use strict';

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

    var $controller = null;
    beforeEach(inject(function(_$controller_) {
        $controller = _$controller_;
    }));

    describe('ClaireController', function() {
        var $scope, ctrl;
        beforeEach(inject(function($rootScope) {
            $scope = $rootScope.$new();
            ctrl = $controller("ClaireCtrl", { $scope: $scope, DrugInfo, DrugInfo });
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

            it('should populate the drug list', inject(function($httpBackend) {
                $httpBackend.whenGET(/api\/trends\/positive/).respond(undefined);
                $httpBackend.whenGET(/api\/trends\/negative/).respond(undefined);
                $httpBackend.whenGET(/api\/trends\/adverse/).respond(undefined);
                $scope.$digest();

                expect(DrugInfo.getDrugList).toHaveBeenCalled();
                expect($scope.drugList).toBe(mockDrugListData);
            }));
        });
    });
});
