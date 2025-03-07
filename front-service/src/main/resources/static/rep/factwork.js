angular.module('workTimeService').controller('workFactRepController', function ($scope, $http, $location, $localStorage) {

    const constPatchWork = 'http://178.20.40.174:5555/work-service/v1';

    var Filt;
    $scope.loadWorkTime = function () {
        console.log("qqqqqqqq 2");
        $scope.findPage();
    };

    $scope.findPage = function () {
        $http({
            url: constPatchWork + "/works/rep/factwork",
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