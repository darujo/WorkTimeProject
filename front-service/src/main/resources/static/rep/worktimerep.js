angular.module('workTimeService').controller('workTimeRepController', function ($scope, $http, $location) {

    const constPatchWork = window.location.origin + '/work-service/v1';

    $scope.work = {
        laborAnalise: null,
        timePlan: null,
        timeFact: null
    }
    $scope.loadWorkTime = function () {
        console.log("loadWorkTime");
        $location.parserFilter($scope.Filt);
        $scope.findPage();
    };
    $scope.sendFilter = function () {
        $location.sendFilter(location.hash, $scope.Filt);
    }

    $scope.findPage = function () {
        console.log("findPage");
        console.log($scope.WorkSort);
        let Filter;
        Filter = $scope.Filt;
        $http({
            url: constPatchWork + "/works/rep",
            method: "get",
            params: {
                userName: Filter ? Filter.userName : null,
                stageZi: Filter ? Filter.stageZi : null,
                availWork: Filter ? Filter.availWork : null,
                releaseId: Filter ? Filter.releaseId : null,
                sort: $scope.WorkSort ? $scope.WorkSort : null,
                ziName: Filter ? Filter.ziName : null


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
        $location.saveFilter("workTimeRepFilter", $scope.Filt);
        $scope.findPage();
    };
    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        $scope.Filt = {
            stageZi: 15,
            availWork: true
        };
        console.log($scope.Filt);
        if (load) {
            $scope.filterWork();
        }
    }
    let init = function () {
        $scope.Filt = $location.getFilter("workTimeRepFilter");
        if ($scope.Filt === null) {
            $scope.clearFilter(false);
        }
        $location.getReleases().then(function (result) {
            $scope.ReleaseList = result;
            console.log("result releaseList");
            console.log(result);
        });

        $scope.addSort("releaseId");
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
        } else if (element === "releaseId") {
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