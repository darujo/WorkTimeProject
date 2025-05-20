angular.module('workTimeService').controller('workTimeController', function ($scope, $http, $location, $localStorage) {

    const constPatchWorkTime = window.location.origin + '/worktime-service/v1';
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

    let checkRight = function (right, message, callBack) {
        document.getElementById("ButtonSaveDown").style.display = "none";
        // document.getElementById("ButtonSaveUp").style.display = "none";
        $scope.Resp = {message: null}
        $http({
            url: constPatchWorkTime + "/worktime/right/" + right,
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
        console.log("$localStorage.openEdit worktime ");
        $scope.loadWorkTime();
    };
    $scope.loadWorkTime = function () {
        showWorkTime();
        console.log("$location.TaskId " + $location.TaskId);
        console.log("$location.WorkTime " + $localStorage.WorkTime);
        console.log($localStorage.WorkTime);
        console.log(typeof $localStorage.WorkTime !== "undefined");
        if (location.href.indexOf("?") !== -1) {
            $location.parserFilter($scope.Filt);
            $scope.findPage(0);
            console.log($scope.Filt);
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
        $scope.Filt["currentUser"] = $scope.Filt.currentUser ? true : false;
        $location.sendFilter(location.hash, $scope.Filt);
    }

    $scope.setFormWorkTime = function () {
        if (typeof $scope.Filt !== "undefined") {
            // if (Filt.taskId != null) {
            //     $scope.Filt = {
            //         size: $scope.Filt ? $scope.Filt.size : null,
            //         dateLe: $scope.Filt ? $scope.Filt.dateLe : null,
            //         dateGe: $scope.Filt ? $scope.Filt.dateGe : null,
            //         taskId: Filt ? Filt.taskId : null,
            //         taskDevbo: $scope.Filt ? $scope.Filt.taskDevbo : null,
            //         taskBts: $scope.Filt ? $scope.Filt.taskBts : null,
            //         nikName: $scope.Filt ? $scope.Filt.nikName : null,
            //         currentUser: $scope.Filt ? $scope.Filt.currentUser : null
            //     }
            //     // document.getElementById("TaskId").value = Filt.taskId;
            // }
            if ($scope.Filt.dateLe != null) {
                console.log("Filt.dateLe");
                console.log($scope.Filt.dateLe);
                let vdate = new Date($scope.Filt.dateLe);
                vdate.setHours(6);
                document.getElementById("DateLe").valueAsDate = vdate;
            }
            if ($scope.Filt.dateGe != null) {
                let vdate = new Date($scope.Filt.dateGe);
                vdate.setHours(6);

                document.getElementById("DateGe").valueAsDate = vdate;
            }
        }
    }

    $scope.findPage = function (diffPage) {
        console.log("findPage");
        let page = parseInt(document.getElementById("Page").value) + diffPage;
        document.getElementById("Page").value = page;
        console.log("запрос данных");

        // if (typeof $scope.Filt !== "undefined") {
        //     // Filt = {size: 10, currentUser: true};
        //     Filt = $scope.Filt;
        // } else
        if (typeof $scope.Filt === "undefined") {
            $scope.Filt = {size: 10, currentUser: true};
            // Filt = $scope.Filt;
        }
        let Filt;
        Filt = $scope.Filt;
        console.log(Filt);
        $http({
            url: constPatchWorkTime + "/worktime",
            method: "get",
            params: {
                page: page,
                size: Filt ? Filt.size : null,
                dateLe: Filt ? Filt.dateLe : null,
                dateGe: Filt ? Filt.dateGe : null,
                taskId: Filt ? Filt.listId ? Filt.listId :Filt.taskId : null,
                taskDEVBO: Filt ? Filt.taskDevbo : null,
                taskBTS: Filt ? Filt.taskBts : null,
                nikName: Filt ? Filt.nikName : null,
                currentUser: Filt ? Filt.currentUser : null,
                type: Filt ? Filt.type : null,
                comment: Filt ? Filt.comment : null

            }


        }).then(function (response) {
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
            console.log(response)
            if ($location.checkAuthorized(response)) {
                //     alert(response.data.message);
            }

            // showFindTask();
        });

    };
    $scope.filterWorkTime = function () {
        console.log("filterWorkTime")
        // Filt = $scope.Filt;
        // console.log(Filt)
        $location.saveFilter("wortTimeFilter", $scope.Filt);
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
        // $scope.FiltWork = {size: 10};
        showTaskNum();
        console.log("edit");
        $http.get(constPatchWorkTime + "/worktime/" + workTimeId)
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
        // $scope.FiltWork = {size: 10};
        showTaskNum();
        console.log("edit");
        $http.get(constPatchWorkTime + "/worktime/" + workTimeId)
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
        $http.delete(constPatchWorkTime + "/worktime/" + workTimeId)
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
            $http.post(constPatchWorkTime + "/worktime", $scope.WorkTime)
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
    // var FiltTask;
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
        let FiltTask;
        FiltTask = $scope.FiltTask;
        console.log(page);
        console.log(FiltTask);
        document.getElementById("PageTask").value = page;
        $http({
            url: constPatchTask + "/task",
            method: "get",
            params: {
                page: page,
                size: FiltTask ? FiltTask.size : null,
                workId: FiltTask ? FiltTask.workId : null,
                codeBTS: FiltTask ? FiltTask.bts : null,
                codeDEVBO: FiltTask ? FiltTask.devbo : null,
                description: FiltTask ? FiltTask.desc : null,
                ziName: FiltTask ? FiltTask.ziName : null,
                type: FiltTask ? FiltTask.type : null,
                nikName: FiltTask ? FiltTask.nikName : null
            }
        }).then(function (response) {
            console.log("/task.response");
            console.log(response);
            console.log("response,data :");
            console.log(response.data);
            $scope.TaskList = response.data.content;
            maxPage = response.data.totalPages;
            // showTask();

        }, function errorCallback(response) {
            console.log(response)
            if ($location.checkAuthorized(response)) {
                //     alert(response.data.message);
            }

            // showFindTask();
        });

    }
    $scope.filterTask = function () {
        console.log("filterTask");
        // FiltTask = $scope.FiltTask;
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
        $scope.loadTask(0);

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
        $scope.Filt = {
            taskId: null,
            currentUser: true,
            size: 10
        };
        console.log($scope.Filt);
        if (load) {
            $scope.filterWorkTime();
        }
    }
    $scope.clearFilterTask = function (load) {
        console.log("clearFilter");
        if ($scope.FiltTask === null) {
            $scope.FiltTask = {size: 10}
        }
        console.log($scope.Filt);
        if (load) {
            $scope.filterTask();
        }
    }
    $scope.Filt = $location.getFilter("wortTimeFilter");
    $scope.FiltTask = $location.getFilter("wortTimeEditFilter");
    if ($scope.Filt === null) {
        $scope.clearFilter(false);
    }
    $scope.clearFilterTask(false);
    checkRight("changeuser", false, callBackUserChange);
    console.log("Start");
    showWorkTime();
    console.log("Show ok");

    $location.getUsers().then(function (result) {$scope.UserList = result;
        console.log("result UserList"); console.log(result);
    });
    $location.getRoles().then(function (result) {$scope.RoleList = result;
        console.log("result RoleList"); console.log(result);
    });
    $scope.loadWorkTime();
    console.log("----------------------------------------")
    console.log($location.UserList);

})