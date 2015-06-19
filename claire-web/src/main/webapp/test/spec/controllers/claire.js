'use strict';

describe('controllers', function() {
    beforeEach(module('ngRoute'));
    beforeEach(module('claire.controllers'));
    beforeEach(module('claire.services'));
    beforeEach(module('claire.directives'));

    var scope, ctrl;

    describe('ClaireController', function() {
        beforeEach(inject(function($rootScope, $controller) {
            scope = $rootScope.$new();
            ctrl = $controller("ClaireCtrl", { $scope: scope });
        }));

        it('gets results', inject(function() {
            expect(scope.data).not.toBe(undefined);
        }));
    });
});
