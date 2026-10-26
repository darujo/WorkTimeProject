angular.module('workTimeService').controller('workTaskTypeController', function ($scope, $http, $location) {

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
    let maxPage = 1;
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
            $scope.WorkList = null;
            let Filter = $scope.Filt;

            $http({
                url: constPatchWork + "/rep/fact/task/type",
                method: "get",
                params: {
                    nikName: Filter ? Filter.nikName : null,
                    // dateStart: Filter ? Filter.dateStart : new Date(),
                    // dateEnd: Filter ? Filter.dateEnd : new Date(),
                    // weekSplit: Filter ? Filter.weekSplit : null,
                    // ziSplit: Filter ? Filter.ziSplit : null,
                    addTotal: Filter ? Filter.addTotal : null,
                    //ЗИ
                    page: Filter ? Filter.page - 1 : 0,
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
                $scope.WorkList = getElementWorkList(response.data._embedded.workContentList);
                maxPage = response.data.page.totalPages;
                // $scope.ziSplit = Filter.ziSplit;
            }, function errorCallback(response) {
                maxPage = 1
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

    $scope.searchJson = function (list, searchField) {
        // console.log("searchJson");
        // console.log(list);
        // console.log(searchField);
        // console.log(searchVal);
        // console.log(resultField);

        return list[searchField]

    }


    let getElementWorkList = function (workList) {
        let arrElement = [];
        let id = 0;
        console.log("--------------------------------- getElementWorkList-------------")
        for (let k = 0; k < workList.length; k++) {

            let work = workList[k];
            if (work.content.length === 0) {
                let task = {
                    countWork: work.count === 0 ? 1 : work.count,
                    countTask: 1,
                    // codeDEVBO: taskElement.codeDEVBO,
                    // description: taskElement.description,
                    workName: work.name,
                    workTime: {}
                }
                let idEl = id
                let indexO = {indexWork: k, indexTask: 0, indexUser: 0, idEl: idEl}
                Object.assign(task, indexO);
                arrElement.push(task)
                id = id + 1;
            } else {
                for (let i = 0; i < work.content.length; i++) {
                    // console.log("i=", i)
                    let taskElement = work.content[i];

                    if (taskElement.content.length === 0) {
                        let task = {
                            countWork: work.count === 0 ? 1 : work.count,
                            countTask: taskElement.count === 0 ? 1 : taskElement.count,
                            codeDEVBO: taskElement.codeDEVBO,
                            description: taskElement.description,
                            workName: work.name,
                            workTime: {}

                        }
                        let idEl = id
                        let indexO = {indexWork: k, indexTask: i, indexUser: 0, idEl: idEl}
                        Object.assign(task, indexO);
                        arrElement.push(task)
                        id = id + 1;
                    } else {
                        for (let j = 0; j < taskElement.content.length; j++) {
                            let task = {
                                countWork: work.count,
                                countTask: taskElement.count,
                                codeDEVBO: taskElement.codeDEVBO,
                                description: taskElement.description,
                                workName: work.name
                            }

                            let idEl = id
                            // console.log("idEl", idEl)
                            let indexO = {indexWork: k, indexTask: i, indexUser: j, idEl: idEl}
                            Object.assign(task, taskElement.content[j], indexO);

                            // console.log(indexO)
                            // console.log(task)
                            // arrElement.push({task: task, user: taskElement.content[j], indexTask: i, indexUser: j} )
                            arrElement.push(task)

                            // arrElement.push(indexO)
                            // arrElement.push({ idEl: idEl})
                            // arrElement[idEl] = {idEl: nnn + "." + idEl}
                            id = id + 1;
                            // console.log(arrElement)
                        }
                    }

                }
            }
        }
        console.log(arrElement)
        return arrElement;
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
    $location.getWorkTimeTypes().then(function (result) {
        console.log("result TaskListType");
        console.log(result);
        $scope.TaskListType = result;
    });

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