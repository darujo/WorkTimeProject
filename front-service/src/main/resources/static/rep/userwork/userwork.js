angular.module('workTimeService').controller('userWorkController', function ($scope, $http, $location) {

    const constPatchWork = window.location.origin + '/workTime-service/v1'.toLowerCase();
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
    $scope.workPeriodDTOs = null;
    $scope.work = {allVacation:null,
        shotVacation:null}
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
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        }
        else {
            $scope.load = true;
            $scope.UserPeriodList = null;
            let Filter = $scope.Filt;
            $http({
                url: constPatchWork + "/workTime/rep/fact/user/work".toLowerCase(),
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
                $scope.WeekWorkList = response.data[0].workPeriodDTOs;
                $scope.UserPeriodList = response.data;
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
    $scope.getStyleVacation = function (work) {
        // console.log("getStyleVacation")
        // console.log(work)
        // console.log(work.allVacation)
        // console.log(work.shotVacation)

        if (typeof work.allVacation !== "undefined" && work.allVacation ) {
            return {
                'background-color': 'LightCoral'
                // ,
                // 'color': 'white'
            };
        } else {
            if (typeof work.shotVacation !== "undefined" && work.shotVacation ) {
                return {
                    'background-color': 'LemonChiffon'
                };
            } else {
                return {};
            }
        }
    };
    $scope.clearFilter(false);
    $location.getUsers().then(function (result) {$scope.UserList = result;
        $scope.UserList = structuredClone(result);
        $scope.UserList.push({firstName: "Все", nikName: "All"});

        console.log("result UserList"); console.log(result);
    });
    $location.getRoles().then(function (result) {$scope.RoleList = result;
        console.log("result RoleList"); console.log(result);
    });


    $scope.loadWorkTime();
})