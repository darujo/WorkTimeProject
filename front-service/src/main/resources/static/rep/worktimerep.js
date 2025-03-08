angular.module('workTimeService').controller('workTimeRepController', function ($scope, $http, $location, $localStorage) {

    const constPatchWork = window.location.origin + '/work-service/v1';

    var Filt;
    $scope.loadWorkTime = function () {
        console.log("qqqqqqqq 2");
        $scope.findPage();
    };

    $scope.findPage = function () {
        console.log("qqqqqqqq 3");

        $http({
            url: constPatchWork + "/works/rep",
            method: "get",
            params: {
                userName: Filt ? Filt.userName : null

            }
        }).then(function (response) {

            console.log(response.data);
            $scope.WorkTimeList = response.data;

        }, function errorCallback(response) {
            console.log(response)
            if($location.checkAuthorized(response)){
                //     alert(response.data.message);
            }
        });
    };
    $scope.filterWorkTime = function () {
        Filt = $scope.Filt;
        $scope.findPage();
    };

    $scope.loadWorkTime();
})