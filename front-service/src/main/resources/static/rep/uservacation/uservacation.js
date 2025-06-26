angular.module('workTimeService').controller('userVacationController', function ($scope, $http, $location) {

    const constPatchWork = window.location.origin + '/calendar-service/v1/vacation/report';
    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        $scope.Filt = {
            dateStart: new Date(),
            dateEnd: new Date(),
            period: 2
        };
        console.log($scope.Filt);
        if (load) {
            $scope.filterWorkTime();
        }
    }
    $scope.weekWorkDtos = null;
    $scope.userVacations = null;

    $scope.loadWorkTime = function () {
        console.log("loadWorkTime");
        console.log($scope.Filt);
        $location.parserFilter($scope.Filt);
        console.log($scope.Filt);

        $scope.findPage();
    };

    $scope.sendFilter = function () {
        $location.sendFilter(location.hash, $scope.Filt);
    }
    $scope.findPage = function () {
        console.log("findPage");
        let Filter = $scope.Filt;
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load = true;
            $scope.UserPeriodList = null;
            $http({
                url: constPatchWork + "/user",
                method: "get",
                params: {
                    nikName: Filter ? Filter.nikName : null,
                    dateStart: Filter ? Filter.dateStart : new Date(),
                    dateEnd: Filter ? Filter.dateEnd : new Date(),
                    periodSplit: Filter ? Filter.period : null
                }
            }).then(function (response) {
                $scope.load = false;
                console.log(response.data);
                $scope.WeekWorkList = response.data.weekWorkDtos;
                $scope.UserPeriodList = response.data.userVacations;
            }, function errorCallback(response) {
                $scope.load = false;
                console.log(response)
                console.log(location);
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
        }
    };

    $scope.filterWorkTime = function () {
        console.log("filterWorkTime");

        $scope.findPage();
    };
    $scope.getStyle = function (time) {
        if (time === 0) {
            return {
                'background-color': 'red',
                'color': 'white'
            };
        } else {
            if (time < 8) {
                return {
                    'background-color': 'yellow'
                };
            } else {
                return {};
            }
        }
    };
    let searchJson = function (list, searchField, searchVal, resultField) {
        // console.log("searchJson");
        // console.log(list);
        // console.log(searchField);
        // console.log(searchVal);
        // console.log(resultField);
        for (let i = 0; i < list.length; i++) {
            if (list[i][searchField] === searchVal) {
                return list[i][resultField]
            }
        }
    }
    $scope.getStyleUserPeriod = function (color) {
        if(typeof color.color !== "undefined") {
            return {
                'background-color': color.color,
                'color': color.color
            };
        }
        return {};
    }
    // $scope.getStyleUserPeriod = function (work) {
    //     let timePeriod = searchJson($scope.WeekWorkList,"period",work.period,"time");
    //     return getStyleUserPeriod(work.time,timePeriod);
    // };
    // let getStyleUserPeriod = function (time,timePeriod) {
    //     // console.log("getStyleUserPeriod")
    //     // console.log(time)
    //     // console.log(timePeriod)
    //     if (time === 0 && !($scope.Filt.period ===1
    //                   || $scope.Filt.period ===2
    //         || $scope.Filt.period ===5
    //         || $scope.Filt.period ===8
    //         || $scope.Filt.period ===11)) {
    //         return {
    //             'background-color': 'red',
    //             'color': 'red'
    //         };
    //     }
    //     else if (time < 0) {
    //         return {
    //             'background-color': 'red',
    //             'color': 'red'
    //         };
    //     } else {
    //         if (time < timePeriod ) {
    //             return {
    //                 'background-color': 'yellow'
    //             };
    //         } else {
    //             return {'color': 'white'};
    //         }
    //     }
    // };
    $scope.clearFilter(false);
    $location.getUsers().then(function (result) {
        $scope.UserList = result;
        console.log("result UserList");
        console.log(result);
    });
    $location.getRoles().then(function (result) {
        $scope.RoleList = result;
        console.log("result RoleList");
        console.log(result);
    });
    $scope.loadWorkTime();
})