angular.module('workTimeService').controller('calendarController', function ($scope, $http, $location) {

    const constPatchWork = window.location.origin + '/calendar-service/v1';
    $scope.day = {
        weekend: null,
        shotDay: null
    }

    $scope.Month1 = null;
    $scope.Month2 = null;
    $scope.Month3 = null;
    $scope.Month4 = null;
    $scope.Month5 = null;
    $scope.Month6 = null;
    $scope.Month7 = null;
    $scope.Month8 = null;
    $scope.Month9 = null;
    $scope.Month10 = null;
    $scope.Month11 = null;
    $scope.Month12 = null;
    $scope.Month = {
        monday: null,
        tuesday: null,
        wednesday: null,
        thursday: null,
        friday: null,
        saturday: null,
        sunday: null
    }

    let Filter;
    $scope.getStyle = function (workDay, shotDay) {
        if (workDay) {
            return {
                'background-color': 'red',
                'color': 'white'
            };
        } else {
            if (shotDay) {
                return {
                    'background-color': 'yellow'
                };
            } else {
                return {};

            }

        }

    };

    $scope.loadMonth = function (month) {
        $http({
            url: constPatchWork + "/calendar",
            method: "get",
            params: {
                year: Filter ? Filter.year : 2025,
                month: month

            }
        }).then(function (response) {
            console.log(response.data);
            $scope["Month" + month] = response.data;
        }, function errorCallback(response) {
            console.log(response)
            if ($location.checkAuthorized(response)) {
                //     alert(response.data.message);
            }
        });
    };

    $scope.selectYear = function () {
        Filter = $scope.Filt;
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
    $scope.Filt = {year: new Date().getFullYear().toString()};
    $scope.selectYear();
})