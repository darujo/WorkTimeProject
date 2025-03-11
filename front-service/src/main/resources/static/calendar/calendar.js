angular.module('workTimeService').controller('calendarController', function ($scope, $http, $location, $localStorage) {

    const constPatchWork = window.location.origin + '/calendar-service/v1';

    var Filt;
    $scope.getStyle = function (workDay,shotDay) {
        if(workDay){
            return {
                'background-color': 'red',
                'color': 'white'
            };
        }
        else {
            if(shotDay){
                return {
                    'background-color': 'yellow'
                };
            }
            else {
                return {};

            }

        }

    };

    $scope.loadMonth = function (month) {
        $http({
            url: constPatchWork + "/calendar",
            method: "get",
            params: {
                year: Filt ? Filt.year : 2025,
                month: month

            }
        }).then(function (response) {
            console.log(response.data);
            $scope["Month" + month] = response.data;
        }, function errorCallback(response) {
            console.log(response)
            if($location.checkAuthorized(response)){
                //     alert(response.data.message);
            }
        });
    };

    $scope.selectYear = function () {
        Filt = $scope.Filt;
        $scope.loadMonth(1);
        $scope.loadMonth(2);
        $scope.loadMonth(3);
        $scope.loadMonth(4);
        $scope.loadMonth(5);
        $scope.loadMonth(6);
        $scope.loadMonth(7);
        $scope.loadMonth(8);
        $scope.loadMonth(9);
        $scope.loadMonth(10);
        $scope.loadMonth(11);
        $scope.loadMonth(12);
    };
    $scope.Filt = {year : "2025"};
    $scope.selectYear();
})