angular.module('workTimeService').controller('worktimeController', function ($scope, $http, $location, $localStorage) {

    const constPatchWorkTime = window.location.origin + '/worktime-service/v1';
    const constPatchTask = window.location.origin + '/task-service/v1';

    var showWorkTime = function () {
        document.getElementById("WorkTimeList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    var showFormEdit = function () {
        checkRight("edit", false);
        document.getElementById("WorkTimeList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
    };
    var showTaskNum = function () {
        document.getElementById("TaskNum").style.display = "block";
        document.getElementById("FindTask").style.display = "none";
    };
    var showFindTask = function () {
        document.getElementById("TaskNum").style.display = "none";
        document.getElementById("FindTask").style.display = "block";
        $scope.filterTask();
    };

    var checkRight = function (right,message) {
        document.getElementById("ButtonSaveDown").style.display = "none";
        // document.getElementById("ButtonSaveUp").style.display = "none";
        $scope.Resp = {message: null}
        $http({
            url: constPatchWorkTime + "/worktime/right/" + right,
            method: "get"
        }).then(function (response) {
                console.log(response)
                document.getElementById("ButtonSaveDown").style.display = "inline-block";
                // document.getElementById("ButtonSaveUp").style.display = "inline-block";
                if (right === "create") {
                    $scope.createWorkTimeRun();
                }
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
    $localStorage.openEdit = function () {
        console.log("$localStorage.openEdit worktime ");
        $scope.loadWorkTime();
    };
    var Filt;
    $scope.loadWorkTime = function () {
        console.log("$location.TaskId " + $location.TaskId);
        console.log("$location.WorkTime " + $localStorage.WorkTime);
        console.log($localStorage.WorkTime);
        console.log(typeof $localStorage.WorkTime !== "undefined");
        if (typeof $location.TaskId !== "undefined") {
            console.log("load5")
            if (typeof Filt === "undefined") {
                Filt = {taskId: null};
            }
            Filt.taskId = $location.TaskId;
            $scope.setFormWorkTime();
            console.log(Filt);
            $location.TaskId = null;
            $scope.findPage(0);
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

    $scope.setFormWorkTime = function () {
        if (typeof Filt != "undefined") {
            if (Filt.taskId != null) {
                $scope.Filt = {
                    size: $scope.Filt ? $scope.Filt.size : null,
                    dateLe: $scope.Filt ? $scope.Filt.dateLe : null,
                    dateGe: $scope.Filt ? $scope.Filt.dateGe : null,
                    taskId: Filt ? Filt.taskId : null,
                    taskDevbo: $scope.Filt ? $scope.Filt.taskDevbo : null,
                    taskBts: $scope.Filt ? $scope.Filt.taskBts : null,
                    nikName: $scope.Filt ? $scope.Filt.nikName : null,
                    currentUser: $scope.Filt ? $scope.Filt.currentUser : null
                }
                // document.getElementById("TaskId").value = Filt.taskId;
            }
            if (Filt.dateLe != null) {
                console.log("Filt.dateLe");
                console.log(Filt.dateLe);
                var vdate = new Date(Filt.dateLe);
                vdate.setHours(6);
                document.getElementById("DateLe").valueAsDate = vdate;
            }
            if (Filt.dateGe != null) {
                var vdate = new Date(Filt.dateGe);
                vdate.setHours(6);

                document.getElementById("DateGe").valueAsDate = vdate;
            }
        }
    }

    $scope.findPage = function (diffPage) {
        var page = parseInt(document.getElementById("Page").value) + diffPage;
        document.getElementById("Page").value = page;
        console.log("запрос данных7");
        if (typeof $scope.Filt === "undefined") {
            $scope.Filt = {size: 10, currentUser: true};
            Filt = $scope.Filt;
        }
        console.log(Filt);
        $http({
            url: constPatchWorkTime + "/worktime",
            method: "get",
            params: {
                page: page,
                size: Filt ? Filt.size : null,
                dateLe: Filt ? Filt.dateLe : null,
                dateGe: Filt ? Filt.dateGe : null,
                taskId: Filt ? Filt.taskId : null,
                taskDevbo: Filt ? Filt.taskDevbo : null,
                taskBts: Filt ? Filt.taskBts : null,
                nikName: Filt ? Filt.nikName : null,
                currentUser: Filt ? Filt.currentUser : null

            }


        }).then(function (response) {
            console.log("sssssss");
            $scope.setFormWorkTime();
            console.log("response :");
            console.log(response);
            console.log("response,data :");
            console.log(response.data);
            if (typeof response.data.content == "undefined") {
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
        Filt = $scope.Filt;
        console.log(Filt)
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };

    var WorkTimeIdEdit = null;

    $scope.createWorkTime = function () {
        checkRight("create", true);
    }
    $scope.createWorkTimeRun = function () {
        console.log("createWorkTime");
        $scope.FiltWork = {size: 10};
        showFindTask();
        WorkTimeIdEdit = null;
        console.log("создаем");

        $scope.WorkTime = {
            id: null,
            taskId: document.getElementById("TaskId").value,
            workDate: new Date(),
            userName: null,
            workTime: null,
            comment: ""
        };
        // $scope
        console.log($scope.WorkTime);
        console.log("создаем 6");
        console.log("создаем 3");

        showFormEdit();

    };

    $scope.editWorkTime = function (workTimeId) {
        $scope.FiltWork = {size: 10};
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
    var sendSave = false;
    $scope.saveWorkTime = function () {
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
    var FiltTask;
    var findNameTask = function (taskId) {
        console.log("findNameTask")

        $http({
            url: constPatchTask + "/task/" + taskId,
            method: "get"

        }).then(function (response) {
            console.log(response.data);
            document.getElementById("TaskName").value = response.data.description;
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
    var maxPage = 1;
    $scope.loadTask = function (diffPage) {
        console.log("loadTask");
        console.log("diffPage");
        console.log(diffPage);
        var page = parseInt(document.getElementById("PageTask").value) + diffPage;
        console.log(page);


        if (maxPage < page) {
            page = maxPage;
        }
        // должно быть после если maxPage = 0
        if (page < 1) {
            page = 1;
        }
        console.log(page);

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
                type: FiltTask ? FiltTask.type : null
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
        FiltTask = $scope.FiltTask;
        document.getElementById("PageTask").value = "1";
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
    $scope.FiltTask = {size: 10}
    console.log("Start");
    showWorkTime();
    console.log("Show ok");

    $scope.loadWorkTime();
})