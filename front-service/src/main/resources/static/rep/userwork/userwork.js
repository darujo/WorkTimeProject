angular.module('workTimeService').controller('userWorkController', function ($scope, $http, $location, $localStorage) {

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
    $scope.clearFilter(false);
    $scope.UserList = $location.getUsers();
    $scope.RoleList = $location.getRoles();
    $scope.UserList.push({firstName: "Все", nikName: "All"});
    $scope.loadWorkTime();
})