angular.module('workTimeService').controller('calendarDayController', function ($scope, $http, $location) {

    const constPatchCalendar = window.location.origin + '/calendar-service/v1/calendar';


    $scope.day = {
        dateStartStr: null,
        dateEndStr: null
    }
    $scope.Day = {
        date: new Date("2026-05-09")
    };
    $scope.editDay = function () {
        console.log("editDay");

        $scope.Day = {
            date: new Date()
        };

    };

    $scope.getDateInfo = function () {
        console.log("getDateInfo");
        $http({
            url: constPatchCalendar + "/day",
            method: "get",
            params: {
                date: $scope.Day.date
            }
        })
            .then(function (response) {
                $scope.Day = response.data;
                console.log("ffffff")
                console.log(response)
                console.log(response.data)
                $scope.Day.date = new Date($scope.Day.date)

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    alert(response.data.message);
                }
            });
    };

    let getDateType = function () {
        console.log("getDateType");
        $http.get(constPatchCalendar + "/day/type")
            .then(function (response) {
                $scope.DayTypeList = response.data;


            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    alert(response.data.message);
                }
            });
    };

    $scope.back = function () {
        $location.path('/calendar');

    }
    getDateType();
    $scope.getDateInfo();

    let sendSave = false;
    $scope.saveDay = function () {
        console.log("saveDay");
        console.log($scope.Day);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchCalendar + "/day", $scope.Day)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendSave = false;
                    $scope.back();
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log(response.data);
                    if ($location.checkAuthorized(response)) {

                        alert(response.data.message);
                    }
                });
        }
    }
})