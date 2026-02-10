angular.module('workTimeService').controller('workTimeController', function ($scope, $http, $location, $localStorage) {

    const constPatchWorkTime = window.location.origin + '/workTime-service/v1'.toLowerCase();
    const constPatchTask = window.location.origin + '/task-service/v1';

    let showWorkTime = function () {
        document.getElementById("WorkTimeList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    let showFormEdit = function () {
        checkRight("edit", false, callBackUpdate);
        document.getElementById("WorkTimeList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
    };
    let showTaskNum = function () {
        document.getElementById("TaskNum").style.display = "block";
        document.getElementById("FindTask").style.display = "none";
    };
    let showFindTask = function () {
        document.getElementById("TaskNum").style.display = "none";
        document.getElementById("FindTask").style.display = "block";
        $scope.filterTask();
    };
    let callBackCreate = function () {
        document.getElementById("ButtonSaveDown").style.display = "inline-block";
        $scope.createWorkTimeRun();

    };
    let callBackUpdate = function () {
        document.getElementById("ButtonSaveDown").style.display = "inline-block";
        // $scope.createWorkTimeRun();

    };
    $scope.Filter = {
        size: null,
        dateLe: null,
        dateGe: null,
        taskId: null,
        taskDevbo: null,
        taskBts: null,
        nikName: null,
        currentUser: true
    }
    $scope.FiltTask = {
        size: null,
        workId: null,
        bts: null,
        devbo: null,
        desc: null,
        ziName: null,
        type: null,
        nikName: null
    }
    $scope.workTime = {
        authorFirstName: null,
        authorLastName: null,
        authorPatronymic: null,
        workDateStr: null,
        taskCodeDEVBO: null,
        taskCodeBTS: null,
        taskDescription: null,
        typeStr: null,
        nameZi: null
    }
    let checkRight = function (right, message, callBack) {
        document.getElementById("ButtonSaveDown").style.display = "none";
        // document.getElementById("ButtonSaveUp").style.display = "none";
        $scope.Resp = {message: null}
        $http({
            url: constPatchWorkTime + "/workTime/right/".toLowerCase() + right,
            method: "get"
        }).then(function (response) {
                console.log(response)
                callBack();

            }, function errorCallback(response) {
                console.log("Check");
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    if (message) {
                        alert(response.data.message);
                    } else {
                        $scope.Resp = {message: response.data.message};
                    }
                }
            }
        )
    }
    $location.openEdit = function () {
        console.log("$localStorage.openEdit workTime ");
        $scope.loadWorkTime();
    };
    $scope.loadWorkTime = function () {
        showWorkTime();
        console.log("$location.WorkTime " + $localStorage.WorkTime);
        console.log($localStorage.WorkTime);
        console.log(typeof $localStorage.WorkTime !== "undefined");
        if (location.href.indexOf("?") !== -1) {

            $location.parserFilter($scope.Filter);
            $scope.findPage(0);
            console.log($scope.Filter);
        } else if (typeof $localStorage.WorkTime !== "undefined") {
            console.log($localStorage.WorkTime);
            if (typeof $localStorage.WorkTime.edit !== "undefined") {
                if ($localStorage.WorkTime.edit) {
                    $localStorage.WorkTime.edit = false;
                    $scope.createWorkTime();
                } else {
                    $scope.findPage(0);
                }
            }
        } else {
            $scope.findPage(0);
        }


    };
    $scope.sendFilter = function () {
        $scope.Filter["currentUser"] = !!$scope.Filter.currentUser;
        $location.sendFilter(location.hash, $scope.Filter);
    }

    $scope.setFormWorkTime = function () {
        if (typeof $scope.Filter !== "undefined") {
            // if (Filter.taskId != null) {
            //     $scope.Filter = {
            //         size: $scope.Filter ? $scope.Filter.size : null,
            //         dateLe: $scope.Filter ? $scope.Filter.dateLe : null,
            //         dateGe: $scope.Filter ? $scope.Filter.dateGe : null,
            //         taskId: Filter ? Filter.taskId : null,
            //         taskDevbo: $scope.Filter ? $scope.Filter.taskDevbo : null,
            //         taskBts: $scope.Filter ? $scope.Filter.taskBts : null,
            //         nikName: $scope.Filter ? $scope.Filter.nikName : null,
            //         currentUser: $scope.Filter ? $scope.Filter.currentUser : null
            //     }
            //     // document.getElementById("TaskId").value = Filter.taskId;
            // }
            if ($scope.Filter.dateLe != null) {
                console.log("Filter.dateLe");
                console.log($scope.Filter.dateLe);
                let vDate = new Date($scope.Filter.dateLe);
                vDate.setHours(6);
                document.getElementById("DateLe").valueAsDate = vDate;
            }
            if ($scope.Filter.dateGe != null) {
                let vDate = new Date($scope.Filter.dateGe);
                vDate.setHours(6);

                document.getElementById("DateGe").valueAsDate = vDate;
            }
        }
    }

    $scope.findPage = function (diffPage) {
        console.log("findPage");
        let page = parseInt(document.getElementById("Page").value) + diffPage;
        document.getElementById("Page").value = page;
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load = true;
            $scope.WorkTimeList = null;
            console.log("запрос данных");

            // if (typeof $scope.Filter !== "undefined") {
            //     // Filter = {size: 10, currentUser: true};
            //     Filter = $scope.Filter;
            // } else
            if (typeof $scope.Filter === "undefined") {
                $scope.Filter = {size: 10, currentUser: true};
                // Filter = $scope.Filter;
            }
            let Filter;
            Filter = $scope.Filter;
            console.log(Filter);
            $http({
                url: constPatchWorkTime + "/workTime".toLowerCase(),
                method: "get",
                params: {
                    page: page,
                    size: Filter ? Filter.size : null,
                    dateLe: Filter ? Filter.dateLe : null,
                    dateGe: Filter ? Filter.dateGe : null,

                    taskDEVBO: Filter ? Filter.taskDevbo : null,
                    taskBTS: Filter ? Filter.taskBts : null,
                    nikName: Filter ? Filter.nikName : null,
                    currentUser: Filter ? Filter.currentUser : null,
                    type: Filter ? Filter.type : null,
                    comment: Filter ? Filter.comment : null,
                    taskId: Filter ? Filter.listId ? Filter.listId : Filter.taskId : null

                }


            }).then(function (response) {
                loadPage = false;
                $scope.load = false;
                $scope.setFormWorkTime();
                console.log("response,data :");
                console.log(response.data);
                if (typeof response.data.content === "undefined") {
                    $scope.WorkTimeList = response.data;
                } else {
                    $scope.WorkTimeList = response.data.content;
                }

                showWorkTime();

            }, function errorCallback(response) {
                loadPage = false;
                $scope.load = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

                // showFindTask();
            });
        }
    };
    let loadPage = true;
    $scope.filterWorkTime = function () {

        if (loadPage) {
            return;
        }
        console.log("filterWorkTime")
        // Filter = $scope.Filter;
        // console.log(Filter)
        $location.saveFilter("wortTimeFilter", $scope.Filter);
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };

    let WorkTimeIdEdit = null;

    $scope.createWorkTime = function () {
        checkRight("create", true, callBackCreate);
    }
    $scope.createWorkTimeRun = function () {
        console.log("createWorkTime");
        $scope.FiltWork = {size: 10};
        showFindTask();
        WorkTimeIdEdit = null;
        console.log("создаем");

        $scope.WorkTime = {
            id: null,
            taskId: parseInt(document.getElementById("TaskId").value),
            workDate: new Date(),
            nikName: null,
            workTime: null,
            comment: "",
            type: 1
        };
        // $scope

        console.log($scope.WorkTime);
        console.log("создаем 6");
        console.log("создаем 3");
        if (!isNaN($scope.WorkTime.taskId)) {
            findNameTask($scope.WorkTime.taskId);
        }
        showFormEdit();

    };

    $scope.editWorkTime = function (workTimeId) {
        showTaskNum();
        console.log("edit");
        $http.get(constPatchWorkTime + "/workTime/".toLowerCase() + workTimeId)
            .then(function (response) {
                WorkTimeIdEdit = response.data.id;
                $scope.WorkTime = response.data;
                console.log($scope.WorkTime);

                $scope.WorkTime.workDate = new Date(response.data.workDate);
                console.log("eeee 3")

                showFormEdit();
                findNameTask($scope.WorkTime.taskId);

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

                // showFindTask();
            });
    };

    $scope.copyWorkTime = function (workTimeId) {
        showTaskNum();
        console.log("edit");
        $http.get(constPatchWorkTime + "/workTime/".toLowerCase() + workTimeId)
            .then(function (response) {
                WorkTimeIdEdit = response.data.id;
                $scope.WorkTime = response.data;
                $scope.WorkTime.id = null;
                $scope.WorkTime.nikName = null;
                $scope.WorkTime.workTime = null;
                $scope.WorkTime.workDate = new Date();
                console.log($scope.WorkTime);

                $scope.WorkTime.workDate = new Date(response.data.workDate);
                console.log("eeee 3")

                showFormEdit();
                findNameTask($scope.WorkTime.taskId);

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

                // showFindTask();
            });
    };
    $scope.deleteWorkTime = function (workTimeId) {
        $http.delete(constPatchWorkTime + "/workTime/".toLowerCase() + workTimeId)
            .then(function (response) {
                console.log("Delete response")
                console.log(response);
                $scope.loadWorkTime();
            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

                // showFindTask();
            });
    };
    let sendSave = false;
    $scope.saveWorkTime = function () {
        console.log("saveWorkTime");
        console.log($scope.WorkTime);
        console.log(WorkTimeIdEdit);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchWorkTime + "/workTime".toLowerCase(), $scope.WorkTime)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendSave = false;
                    showWorkTime();
                    $scope.loadWorkTime();
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log(response.data);
                    if ($location.checkAuthorized(response)) {

                        alert(response.data.message);
                    }
                });
        }
    }
    let TaskType;
    let findNameTask = function (taskId) {
        console.log("findNameTask")

        $http({
            url: constPatchTask + "/task/" + taskId,
            method: "get"

        }).then(function (response) {
            console.log(response.data);
            document.getElementById("TaskName").value = response.data.description;
            TaskType = response.data.type;
            // showTask();
            showTaskNum();

        }, function errorCallback(response) {
            console.log(response)
            if ($location.checkAuthorized(response)) {
                alert(response.data.message);
                showFindTask();
            }
        });

    }
    let maxPage = 1;
    $scope.loadTask = function (diffPage) {
        console.log("loadTask");
        console.log("diffPage");
        console.log(diffPage);
        let page = parseInt(document.getElementById("PageTask").value) + diffPage;
        console.log(page);


        if (maxPage < page) {
            page = maxPage;
        }
        // должно быть после если maxPage = 0
        if (page < 1) {
            page = 1;
        }
        document.getElementById("PageTask").value = page;
        if ($scope.loadTaskWait) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.loadTaskWait = true;
            $scope.TaskList = null;
            let FilterTask;
            FilterTask = $scope.FiltTask;
            console.log(page);
            console.log(FilterTask);

            $http({
                url: constPatchTask + "/task",
                method: "get",
                params: {
                    page: page,
                    size: FilterTask ? FilterTask.size : null,
                    workId: FilterTask ? FilterTask.workId : null,
                    codeBTS: FilterTask ? FilterTask.bts : null,
                    codeDEVBO: FilterTask ? FilterTask.devbo : null,
                    description: FilterTask ? FilterTask.desc : null,
                    ziName: FilterTask ? FilterTask.ziName : null,
                    type: FilterTask ? FilterTask.type : null,
                    nikName: FilterTask ? FilterTask.nikName : null
                }
            }).then(function (response) {
                $scope.loadTaskWait = false;
                console.log("/task.response");
                console.log(response);
                console.log("response,data :");
                console.log(response.data);
                $scope.TaskList = response.data.content;
                maxPage = response.data["totalPages"];
                // showTask();

            }, function errorCallback(response) {
                $scope.loadTaskWait = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

                // showFindTask();
            });
        }
    }

    $scope.filterTask = function () {
        console.log("filterTask");
        document.getElementById("PageTask").value = "1";
        $location.saveFilter("wortTimeEditFilter", $scope.FiltTask);
        $scope.loadTask(0);
    };
    $scope.setTask = function (taskId) {
        document.getElementById("TaskIdEdit").value = taskId;
        $scope.WorkTime.taskId = taskId;
        findNameTask(taskId)

    }
    $scope.findTask = function () {
        // $scope.loadTask(0);

        showFindTask();
    }
    $scope.showType = function () {
        // console.log("TaskType");
        // console.log(TaskType);
        if (TaskType !== 3) {
            // console.log(true);
            return true;
        }
        console.log(false);
        return false;
    }
    let userChange;
    $scope.showUser = function () {
        // console.log("userChange");
        // console.log(userChange);
        if (userChange) {
            // console.log(true);
            return true;
        }
        // console.log(false);
        return false;
    }
    // let callBackEmpty = function () {
    //
    // }
    let callBackUserChange = function () {
        userChange = true;
    }
    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        console.log($scope.Filter)
        if (load) {
            $scope.Filter = {
                taskId: null,
                currentUser: true,
                size: 10
            };
            console.log($scope.Filter);
            $scope.filterWorkTime();
        } else {
            console.log($scope.Filter);
            $scope.Filter["size"] = $scope.Filter.size ? $scope.Filter.size : 10;
            $scope.Filter["currentUser"] = $scope.Filter.currentUser ? $scope.Filter.currentUser : true;
            console.log($scope.Filter);
        }
    }
    $scope.clearFilterTask = function (load) {
        console.log("clearFilter");
        if ($scope.FiltTask === null) {
            $scope.FiltTask = {size: 10}
        }
        console.log($scope.FiltTask)
        if (load) {
            $scope.filterTask();
        }
    }
    console.log(" get filter 1 ", $scope.Filter)
    $scope.Filter = $location.getFilter("wortTimeFilter");
    console.log(" get filter 2 ", $scope.Filter)
    $scope.FiltTask = $location.getFilter("wortTimeEditFilter");

    $scope.clearFilter(false);

    $scope.clearFilterTask(false);
    checkRight("changeUser".toLowerCase(), false, callBackUserChange);
    console.log("Start");
    showWorkTime();
    console.log("Show ok");

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
    $location.getWorkTimeTypes().then(function (result) {
        $scope.WorkTimeTypeList = result;
        console.log("result WorkTimeTypeList");
        console.log(result);
    });

    $scope.availProject = function (list) {
        return $scope.availProjectInList(list, $location.UserLogin.projectId)
    }
    $scope.availProjectInList = function (list, searchVal) {
        console.log("availProjectInList search ", searchVal)

        for (let i = 0; i < list.length; i++) {
            if (list[i]["id"] === searchVal) {
                return true;
            }
        }
        return false;
    }
    $scope.loadWorkTime();
    console.log("----------------------------------------")
    console.log($location.UserList);

})