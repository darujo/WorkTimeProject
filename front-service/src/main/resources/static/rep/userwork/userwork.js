angular.module('workTimeService').controller('userworkController', function ($scope, $http, $location, $localStorage) {

    const constPatchWork = window.location.origin + '/worktime-service/v1';
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
        if (location.href.indexOf("?") !== -1) {
            let paramsStr = new URLSearchParams(location.href.substring(location.href.indexOf("?")));
            for (let [key, value] of paramsStr.entries()) {
                if (key.toLowerCase().indexOf("stage") !== -1
                    || key.toLowerCase().indexOf("size") !== -1
                    || key.toLowerCase().indexOf("sap") !== -1
                    || key.indexOf("period") !== -1) {
                    $scope.Filt[key] = parseInt(value);
                } else if (key.toLowerCase().indexOf("date") !== -1) {
                    console.log(key);

                    console.log(paramsStr.get(key))
                    console.log(new Date(paramsStr.get(key)));

                    $scope.Filt[key] = new Date(paramsStr.get(key));
                } else if (key.indexOf("weekSplit") !== -1
                    || key.indexOf("workTask") !== -1
                    || key.indexOf("workTime") !== -1
                    || key.indexOf("workPercent") !== -1
                    || key.indexOf("addTotal") !== -1
                    || key.indexOf("ziSplit") !== -1) {
                    console.log(key);

                    console.log(paramsStr.get(key))
                    console.log(value)
                    $scope.Filt[key] = paramsStr.get(key) !== "false";
                } else {
                    $scope.Filt[key] = paramsStr.get(key);
                }

            }
            console.log($scope.Filt);
        }
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
            url: constPatchWork + "/worktime/rep/fact/user/work",
            method: "get",
            params: {
                nikName: Filt ? Filt.nikName : null,
                dateStart: Filt ? Filt.dateStart : new Date(),
                dateEnd: Filt ? Filt.dateEnd : new Date(),
                periodSplit: Filt ? Filt.period : null
            }
        }).then(function (response) {
            console.log(response.data);
            $scope.WeekWorkList = response.data[0].workPeriodDTOs;
            $scope.UserPeriodList = response.data;
        }, function errorCallback(response) {
            console.log(response)
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
    $scope.clearFilter(false);
    $scope.UserList = $location.UserList;
    $scope.UserList.push({firstName: "Разработчик", nikName: "ROLE_DEVELOPER"});
    $scope.UserList.push({firstName: "Все", nikName: "All"});
    $scope.loadWorkTime();
})