angular.module('workTimeService').controller('weekworkController', function ($scope, $http, $location, $localStorage) {

    const constPatchWork = window.location.origin + '/worktime-service/v1';

    let Filt= {    dateStart: new Date(),
                   dateEnd: new Date(),
                   weekSplit: true
    };
    $scope.Filt = Filt;
    $scope.loadWorkTime = function () {
        console.log("loadWorkTime");
        console.log($scope.Filt);

        $scope.findPage();
    };

    $scope.findPage = function () {
        console.log("findPage");
        console.log(Filt)
        $http({
            url: constPatchWork + "/worktime/rep/fact/week",
            method: "get",
            params: {
                nikName: Filt ? Filt.nikName : null,
                dateStart: Filt ? Filt.dateStart : new Date(),
                dateEnd: Filt ? Filt.dateEnd : new Date(),
                weekSplit: Filt ? Filt.weekSplit : null
            }
        }).then(function (response) {
            console.log(response.data);
            $scope.WeekWorkList = response.data;
        }, function errorCallback(response) {
            console.log(response)
            if($location.checkAuthorized(response)){
                //     alert(response.data.message);
            }
        });
    };

    $scope.filterWorkTime = function () {
        console.log("filterWorkTime");
        Filt = $scope.Filt;
        $scope.findPage();
    };

    $scope.loadWorkTime();
})