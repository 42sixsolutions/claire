'use strict';

describe('controllers', function() {
    beforeEach(module('claire'));

    beforeEach(inject(function(DrugInfo, $q) {
        spyOn(DrugInfo, "getDrugList").and.callFake(function() {
            var deferred = $q.defer();
            deferred.resolve({ data: ['aspirin', 'claritin', 'benadryl'] });
            return deferred.promise;
        });
    }));

    var $controller = null;
    beforeEach(inject(function(_$controller_) {
        $controller = _$controller_;
    }));

    describe('ClaireController', function() {
        var $scope, ctrl;
        beforeEach(inject(function() {
            $scope = {};
            ctrl = $controller("ClaireCtrl", { $scope: $scope });
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
        });
    });
});
