angular.module('workTimeService').controller('userVacationController', function ($scope, $http, $location) {

    const constPatchWork = window.location.origin + '/calendar-service/v1/vacation/report';
    $scope.clearFilter =function (load){
        console.log("clearFilter");
        $scope.Filt =  {
            dateStart: new Date(),
            dateEnd: new Date(),
            period: 2
        };
        console.log($scope.Filt);
        if(load){
            $scope.filterWorkTime();
        }
    }

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
        let Filt = $scope.Filt;
        $http({
            url: constPatchWork + "/user",
            method: "get",
            params: {
                nikName: Filt ? Filt.nikName : null,
                dateStart: Filt ? Filt.dateStart : new Date(),
                dateEnd: Filt ? Filt.dateEnd : new Date(),
                periodSplit: Filt ? Filt.period : null
            }
        }).then(function (response) {
            console.log(response.data);
            $scope.WeekWorkList = response.data.weekWorkDtos;
            $scope.UserPeriodList = response.data.userVacations;
        }, function errorCallback(response) {
            console.log(response)
            console.log("rrrrr");
            console.log(location);
            if ($location.checkAuthorized(response)) {
                //     alert(response.data.message);
            }
        });
    };

    $scope.filterWorkTime = function () {
        console.log("filterWorkTime");

        $scope.findPage();
    };
    $scope.getStyle = function (time) {
        if (time === 0 ) {
            return {
                'background-color': 'red',
                'color': 'white'
            };
        } else {
            if (time < 8 ) {
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
    $scope.getStyleUserPeriod = function (work) {
        let timePeriod = searchJson($scope.WeekWorkList,"period",work.period,"time");
        return getStyleUserPeriod(work.time,timePeriod);
    };
    let getStyleUserPeriod = function (time,timePeriod) {
        console.log("getStyleUserPeriod")
        console.log(time)
        console.log(timePeriod)
        if (time === 0 && !($scope.Filt.period ===1
                      || $scope.Filt.period ===2
            || $scope.Filt.period ===5
            || $scope.Filt.period ===8
            || $scope.Filt.period ===11)) {
            return {
                'background-color': 'red',
                'color': 'red'
            };
        }
        else if (time < 0) {
            return {
                'background-color': 'red',
                'color': 'red'
            };
        } else {
            if (time < timePeriod ) {
                return {
                    'background-color': 'yellow'
                };
            } else {
                return {'color': 'white'};
            }
        }
    };
    let callBackUser = function (response){
        console.log("callBackUser");
        console.log(response);
        $scope.UserList = response;
    }
    let callBackRole = function (response){
        $scope.RoleList = response;
    }
    $scope.clearFilter(false);
    $location.getUsers(callBackUser);
    $location.getRoles(callBackRole);
    $scope.UserList.push({firstName: "Все", nikName: "All"});
    $scope.loadWorkTime();
})