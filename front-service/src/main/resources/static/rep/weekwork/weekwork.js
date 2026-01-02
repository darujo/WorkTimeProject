angular.module('workTimeService').controller('weekWorkController', function ($scope, $http, $location) {

    const constPatchWork = window.location.origin + '/work-service/v1/works';
    $scope.userWorkFormDTOs = null;
    $scope.work = {
        userCol: null,
        codeInt: null,
        workTaskColAttr: null,
        workTimeAttr: null,
        workPlan: null,
        workAllFact: null
    }
    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        if ($scope.Filt === null || load) {
            $scope.Filt = {
                dateStart: new Date(),
                dateEnd: new Date(),
                weekSplit: true,
                workTask: true,
                workTime: true,
                workPercent: true,
                stageZi: 15,
                page: 1,
                size: 10
            };
        } else {
            $scope.Filt ["dateStart"] = $scope.Filt.dateStart ? $scope.Filt.dateStart : new Date();
            $scope.Filt ["dateEnd"] = $scope.Filt.dateEnd ? $scope.Filt.dateEnd : new Date();
            $scope.Filt ["weekSplit"] = $scope.Filt.weekSplit ? $scope.Filt.weekSplit : true;
            $scope.Filt ["workTask"] = $scope.Filt.workTask ? $scope.Filt.workTask : true;
            $scope.Filt ["workTime"] = $scope.Filt.workTime ? $scope.Filt.workTime : true;
            $scope.Filt ["workPercent"] = $scope.Filt.workPercent ? $scope.Filt.workPercent : true;
            $scope.Filt ["stageZi"] = $scope.Filt.stageZi ? $scope.Filt.stageZi : 15;
            $scope.Filt ["page"] = $scope.Filt.page ? $scope.Filt.page : 1;
            $scope.Filt ["size"] = $scope.Filt.size ? $scope.Filt.size : 10;
        }
        console.log($scope.Filt);
        if (load) {
            $scope.filterWorkTime();
            $scope.Filt.page = 1;
        }
    }

    $scope.loadWorkTime = function () {
        console.log("loadWorkTime");
        $location.parserFilter($scope.Filt);
        console.log($scope.Filt);

        $scope.findPage(0);
    };
    $scope.sendFilter = function () {
        $scope.Filt["weekSplit"] = $scope.Filt.weekSplit;
        $scope.Filt["workTask"] = $scope.Filt.workTask;
        $scope.Filt["workTime"] = $scope.Filt.workTime;
        $scope.Filt["workPercent"] = $scope.Filt.workPercent;
        $scope.Filt["addTotal"] = $scope.Filt.addTotal;
        $scope.Filt["ziSplit"] = $scope.Filt.ziSplit;


        $location.sendFilter(location.hash, $scope.Filt);
    }
    let maxPage;
    $scope.findPage = function (diff) {
        console.log("findPage");
        console.log($scope.Filt.page);
        let page = parseInt($scope.Filt.page) + diff;
        if (page > maxPage) {
            page = maxPage;

        }
        if (page < 1) {
            page = 1;
        }
        $scope.Filt.page = page;
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load = true;
            $scope.WeekWorkList = null;
            let Filter = $scope.Filt;

            $http({
                url: constPatchWork + "/rep/fact/week",
                method: "get",
                params: {
                    nikName: Filter ? Filter.nikName : null,
                    dateStart: Filter ? Filter.dateStart : new Date(),
                    dateEnd: Filter ? Filter.dateEnd : new Date(),
                    weekSplit: Filter ? Filter.weekSplit : null,
                    ziSplit: Filter ? Filter.ziSplit : null,
                    addTotal: Filter ? Filter.addTotal : null,
                    //ЗИ
                    page: Filter ? Filter.page : 1,
                    size: Filter ? Filter.size : 10,
                    name: Filter ? Filter.name : null,
                    task: Filter ? Filter.task : null,
                    codeSap: Filter ? Filter.codeSap : null,
                    codeZi: Filter ? Filter.codeZi : null,
                    stageZi: Filter ? Filter.stageZi : null,
                    releaseId: Filter ? Filter.releaseId : null
                }
            }).then(function (response) {
                $scope.load = false;
                console.log(response.data);
                $scope.WeekWorkList = response.data;
                $scope.ziSplit = Filter.ziSplit;
            }, function errorCallback(response) {
                $scope.load = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
        }
    };

    $scope.filterWorkTime = function () {
        console.log("filterWorkTime");
        $scope.Filt.page = 1;
        $location.saveFilter("week_work", $scope.Filt);
        $scope.findPage(0);
    };
    let callBackType = function (response) {
        console.log("TaskListType");
        console.log(response);
        $scope.TaskListType = response.data;
    }
    $scope.searchJson = function (list, searchField, searchVal, resultField) {
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
    $scope.openTask = function (task, type) {
        console.log(typeof task[type])
        if (typeof task[type] !== "undefined") {
            $location.path("/task").search({listId: task[type]});
        }
    }
    $scope.openWorkTime = function (nikName, task, type, dateGe, dateLe) {
        if (typeof task[type] !== "undefined") {
            $location.path("/workTime".toLowerCase()).search({
                listId: task[type],
                nikName: nikName,
                currentUser: false,
                dateGe: dateGe,
                dateLe: dateLe
            });
        }
    }

    $location.getCode("task/code/type", callBackType);
    $scope.Filt = $location.getFilter("week_work");
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
    $location.getReleases().then(function (result) {
        $scope.ReleaseList = result;
    });


    $scope.loadWorkTime();
})