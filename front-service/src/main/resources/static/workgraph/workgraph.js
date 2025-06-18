angular.module('workTimeService').controller('workGraphController', function ($scope, $http, $location) {

    const constPatchWorkGraph = window.location.origin + '/work-service/v1/works/rep/graph';


    $scope.loadWorkGraph = function () {
        console.log("loadWorkGraph");
        $location.parserFilter($scope.Filt);
        $scope.findPage(0);
    };

    $scope.sendFilter = function () {
        $location.sendFilter(location.hash, $scope.Filt);
    }

    let maxPage = 1;
    $scope.load = false;
    $scope.findPage = function (diffPage) {
        console.log("findPage");
        console.log($scope.WorkSort);
        let page = parseInt(document.getElementById("Page").value) + diffPage;
        if (page > maxPage) {
            page = maxPage;

        }
        if (page < 1) {
            page = 1;
        }
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        }
        else {
            document.getElementById("Page").value = page;
            let Filter;
            Filter = $scope.Filt;
            $scope.load = true;
            $scope.WorkGraphDTOs = null;
            $http({
                url: constPatchWorkGraph,
                method: "get",
                params: {
                    page: page,
                    size: Filter ? Filter.size : null,
                    nameZi: Filter ? Filter.name : null,
                    codeSap: Filter ? Filter.codeSap : null,
                    codeZi: Filter ? Filter.codeZi : null,
                    task: Filter ? Filter.task : null,
                    releaseId: Filter ? Filter.releaseId : null,
                    sort: Filter ? Filter.sort : null,
                    stageZi: Filter ? Filter.stageZi : null,

                    period: Filter ? Filter.period : null,
                    dateStart: Filter ? Filter.dateStart : null,
                    dateEnd: Filter ? Filter.dateEnd : null
                }
            }).then(function (response) {

                console.log(response.data);
                $scope.WeekWorkDTOs = response.data.content.weekWorkDTOs;
                $scope.WorkGraphDTOs = response.data.content.workGraphDTOs;
                maxPage = response.data["totalPages"];
                $scope.load = false;

            }, function errorCallback(response) {
                $scope.load = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
        }
    };

    $scope.getStyleColorBack= function (color){
        return {
            'background-color': color
        };
    }
    $scope.filterWork = function () {
        $location.saveFilter("workGraph", $scope.Filt);
        $scope.findPage(0);
    };
    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        document.getElementById("Page").value = "1";
        $scope.Filt = {
            stageZi: 15,
            size: 10
        }
        console.log($scope.Filt);
        if (load) {
            $scope.filterWork();
        }
    }
    let init = function () {
        document.getElementById("Page").value = "1";
        $scope.Filt = $location.getFilter("workGraph");
        if ($scope.Filt === null) {
            $scope.clearFilter(false);
        }
        $location.getReleases().then(function (result) {
            $scope.ReleaseList = result;
            console.log("result releaseList");
            console.log(result);
        });
        $scope.Filt.dateStart = new Date();
        $scope.Filt.dateEnd = new Date();
        $scope.Filt.period = 8;
        $scope.loadWorkGraph();
    }

    // let arrSort = [];
    // $scope.addSort = function (elemSort) {
    //     console.log("addSort");
    //
    //     let i = arrSort.indexOf(elemSort);
    //     if (-1 === i) {
    //         arrSort.push(elemSort);
    //         $scope.WorkSort = arrSort;
    //         console.log($scope.WorkSort);
    //     }
    //     $scope.filterWork();
    // }
    // $scope.delSort = function (elemSort) {
    //     console.log("delSort");
    //     let i = arrSort.indexOf(elemSort);
    //     if (-1 !== i) {
    //         arrSort.splice(i, 1);
    //         $scope.WorkSort = arrSort;
    //         console.log($scope.WorkSort);
    //     }
    //     $scope.filterWork();
    // }
    // $scope.nameSort = function (element) {
    //     // console.log("nameSort");
    //     if (element === "startTaskPlan") {
    //         return "Начало доработки план";
    //     } else if (element === "startTaskFact") {
    //         return "Начало доработки факт";
    //     } else if (element === "releaseId") {
    //         return "Релиз";
    //     } else if (element === "codeZI") {
    //         return "Код ЗИ";
    //     } else if (element === "name") {
    //         return "Наименование";
    //     }
    //     return element;
    // }
    init();
})