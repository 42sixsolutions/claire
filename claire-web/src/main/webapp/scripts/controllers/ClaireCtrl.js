'use strict';

angular.module('claire.controllers').controller('ClaireCtrl', ["$scope", "Data",
        function($scope, Data) {

    $scope.data = Data.getLineData();
}]);
