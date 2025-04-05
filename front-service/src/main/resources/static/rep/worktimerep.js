angular.module('workTimeService').controller('workTimeRepController', function ($scope, $http, $location, $localStorage) {

    const constPatchWork = window.location.origin + '/work-service/v1';


    $scope.loadWorkTime = function () {
        console.log("loadWorkTime");
        $scope.findPage();
    };

    $scope.findPage = function () {
        console.log("findPage");
        console.log($scope.WorkSort);
        let Filt;
        Filt = $scope.Filt;
        $http({
            url: constPatchWork + "/works/rep",
            method: "get",
            params: {
                userName: Filt ? Filt.userName : null,
                stageZi: Filt ? Filt.stageZi : null,
                availWork: Filt ? Filt.availWork : null,
                release: Filt ? Filt.release : null,
                sort: $scope.WorkSort ? $scope.WorkSort : null,
                ziName: Filt ? Filt.ziName : null


            }
        }).then(function (response) {

            console.log(response.data);
            $scope.WorkTimeList = response.data;

        }, function errorCallback(response) {
            console.log(response)
            if ($location.checkAuthorized(response)) {
                //     alert(response.data.message);
            }
        });
    };
    $scope.filterWork = function () {
        $location.saveFilter("workTimeRepFilter",$scope.Filt);
        $scope.findPage();
    };
    var init = function () {
        $scope.Filt = $location.getFilter("workTimeRepFilter");
        if ($scope.Filt === null ) {
            $scope.Filt = {
                stageZi: 15,
                availWork: true
            };
        }
        $scope.addSort("release");
        $scope.loadWorkTime();
    }

    let arrSort = [];
    $scope.addSort = function (elemSort) {
        console.log("addSort");

        let i = arrSort.indexOf(elemSort);
        if (-1 === i) {
            arrSort.push(elemSort);
            $scope.WorkSort = arrSort;
            console.log($scope.WorkSort);
        }
        $scope.filterWork();
    }
    $scope.delSort = function (elemSort) {
        console.log("delSort");
        let i = arrSort.indexOf(elemSort);
        if (-1 !== i) {
            arrSort.splice(i, 1);
            $scope.WorkSort = arrSort;
            console.log($scope.WorkSort);
        }
        $scope.filterWork();
    }
    $scope.nameSort = function (element) {
        // console.log("nameSort");
        if (element === "startTaskPlan") {
            return "Начало доработки план";
        } else if (element === "startTaskFact") {
            return "Начало доработки факт";
        } else if (element === "release") {
            return "Релиз";
        } else if (element === "codeZI") {
            return "Код ЗИ";
        } else if (element === "name") {
            return "Наименование";
        }
        return element;
    }
    init();
})