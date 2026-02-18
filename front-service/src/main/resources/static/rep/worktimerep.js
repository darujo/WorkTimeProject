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
        console.log("findPage 1");
        $scope.findPage();
    };
    $scope.sendFilter = function () {
        $location.sendFilter(location.hash, $scope.Filt);
    }
    $scope.load = false;
    $scope.findPage = function () {
        console.log("findPage");
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load = true;
            $scope.WorkTimeList = null;
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
                $scope.load = false;
                console.log(response.data);
                $scope.WorkTimeList = response.data;

            }, function errorCallback(response) {
                $scope.load = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
        }
    };
    $scope.filterWork = function () {
        $location.saveFilter("workTimeRepFilter", $scope.Filt);
        console.log("findPage 2");
        $scope.findPage();
    };
    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        console.log(load);


        if (load) {
            $scope.Filt = {
                stageZi: 15,
                availWork: true
            };
            console.log($scope.Filt);
            console.log("load ?");
            console.log(load);
            $scope.filterWork();
        } else {
            $scope.Filt ["stageZi"] = $scope.Filt.stageZi ? $scope.Filt.stageZi : 15;
            $scope.Filt ["availWork"] = $scope.Filt.availWork ? $scope.Filt.availWork : true;
        }
    }
    let init = function () {
        $scope.Filt = $location.getFilter("workTimeRepFilter");
        $scope.clearFilter(false);

        $location.getReleases().then(function (result) {
            $scope.ReleaseList = result;
            console.log("result releaseList");
            console.log(result);
        });

        $scope.addSortNotLoad("release.sort");
        $scope.loadWorkTime();
    }

    let arrSort = [];
    $scope.addSort = function (elemSort) {
        console.log("addSort");

        $scope.addSortNotLoad(elemSort)
        $scope.filterWork();
    }
    $scope.addSortNotLoad = function (elemSort) {
        console.log("addSort");

        let i = arrSort.indexOf(elemSort);
        if (-1 === i) {
            arrSort.push(elemSort);
            $scope.WorkSort = arrSort;
            console.log($scope.WorkSort);
        }
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
        } else if (element === "release.sort") {
            return "Релиз";
        } else if (element === "codeZI") {
            return "Код ЗИ";
        } else if (element === "name") {
            return "Наименование";
        }
        return element;
    }
    $scope.getStyle = function (flag) {
        // if (new Date(date1) < new Date(date2)) {
        if (flag) {
            return {
                'background-color': 'red',
                'color': 'white'
            };
        } else {
            return {};
        }
    };
    init();
})