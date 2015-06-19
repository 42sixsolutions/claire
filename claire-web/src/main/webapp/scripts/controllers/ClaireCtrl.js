'use strict';

angular.module('claire.controllers').controller('ClaireCtrl', ["$scope", "Data",
        function($scope, Data) {

    // TODO: make stuff happen
    $scope.data = Data.getBubbleData();
}]);
