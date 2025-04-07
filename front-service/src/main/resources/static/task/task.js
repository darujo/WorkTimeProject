angular.module('workTimeService').controller('taskController', function ($scope, $http, $location, $localStorage) {

    const constPatchTask = window.location.origin + '/task-service/v1';
    const constPatchWork = window.location.origin + '/work-service/v1';

    let showTask = function () {
        document.getElementById("TaskList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    let showFormEdit = function () {
        checkRight("Edit", false);
        document.getElementById("TaskList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
    };
    let showWorkNum = function () {
        document.getElementById("WorkNum").style.display = "block";
        document.getElementById("FindWork").style.display = "none";
    };
    let showFindWork = function () {
        document.getElementById("WorkNum").style.display = "none";
        document.getElementById("FindWork").style.display = "block";
        $scope.filterWork();
    };
    let checkRight = function (right, message) {
        document.getElementById("ButtonSaveDown").style.display = "none";
        document.getElementById("ButtonSaveUp").style.display = "none";
        $scope.Resp = {message: null}
        $http({
            url: constPatchTask + "/task/right/" + right,
            method: "get"
        }).then(function (response) {
                console.log(response)
                document.getElementById("ButtonSaveDown").style.display = "inline-block";
                document.getElementById("ButtonSaveUp").style.display = "inline-block";
                if (right === "create") {
                    $scope.createTaskRun();
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

    let TaskIdEdit = null;
    $scope.loadTask = function () {
        console.log("loadTask");
        console.log("$location.WorkId " + $location.WorkId);
        if (typeof $location.WorkId !== "undefined") {
            console.log("load5")
            // if (typeof $scope.Filt == "undefined") {
            //     $scope.Filt = {workId: null};
            // }
            $scope.Filt = {workId: $location.WorkId};
            $scope.setFormTask();
            console.log($scope.Filt);
            $location.WorkId = null;
        }

        $scope.findPage(0);
    };

    $scope.setFormTask = function () {
        console.log("setFormTask");
        // console.log(Filt);
        // if (typeof Filt != "undefined") {
        //     if (Filt.workId != null) {
        //         $scope.Filt =
        //             {
        //                 size: $scope.Filt ? $scope.Filt.size : null,
        //                 workId: Filt.workId,
        //                 codeBTS: $scope.Filt ? $scope.Filt.bts : null,
        //                 codeDEVBO: $scope.Filt ? $scope.Filt.devbo : null,
        //                 description: $scope.Filt ? $scope.Filt.desc : null,
        //                 ziName: $scope.Filt ? $scope.Filt.ziName : null,
        //                 type: $scope.Filt ? $scope.Filt.type : null
        //
        //             }
        //         // Filt.workId;
        //     }
        // }
        console.log(document.getElementById("WorkIdEdit").value);
    }

    $scope.findPage = function (diffPage) {
        console.log("findPage");
        let page = parseInt(document.getElementById("Page").value) + diffPage;
        document.getElementById("Page").value = page;
        console.log("запрос данных7");

        let Filt;
        Filt = $scope.Filt;
        console.log(Filt);
        $http({
            url: constPatchTask + "/task",
            method: "get",
            params: {
                page: page,
                size: Filt ? Filt.size : null,
                workId: Filt ? Filt.workId : null,
                codeBTS: Filt ? Filt.bts : null,
                codeDEVBO: Filt ? Filt.devbo : null,
                description: Filt ? Filt.desc : null,
                ziName: Filt ? Filt.ziName : null,
                type: Filt ? Filt.type : null

            }
        }).then(function (response) {
                $scope.setFormTask();
                console.log("response :");
                console.log(response);
                console.log("response,data :");
                console.log(response.data);
                console.log(response.data.content);
                if (typeof response.data.content === "undefined") {
                    $scope.TaskList = response.data;
                } else {
                    $scope.TaskList = response.data.content;
                }
                showTask();
            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            }
        );

    };
    $scope.filterTask = function () {
        console.log("filterTask");
        document.getElementById("Page").value = "1";
        $location.saveFilter("taskFilter", $scope.Filt);
        $scope.findPage(0);
    };

    $scope.createTask = function () {
        checkRight("create", true);
    }
    $scope.createTaskRun = function () {
        showFindWork();
        console.log("create");
        TaskIdEdit = null;
        console.log("создаем");
        // document.getElementById("TaskIdEdit").value = null;
        console.log("создаем 1");
        console.log(typeof $scope.Task);
        $scope.Task = {
            id: null,
            workId: null,
            codeBTS: null,
            userName: null,
            codeDEVBO: null,
            description: null,
            type: 1
        };
        // $scope
        console.log($scope.Task);
        console.log("создаем 5");
        $scope.Task.workId = parseInt(document.getElementById("WorkIdFilt").value);
        console.log($scope.Task.workId);
        if (!isNaN($scope.Task.workId)){
            findNameWork($scope.Task.workId);
        }
        console.log("создаем 3");

        showFormEdit();

    };

    $scope.editTask = function (taskId) {
        showWorkNum();
        console.log("edit");
        $http.get(constPatchTask + "/task/" + taskId)
            .then(function (response) {
                TaskIdEdit = response.data.id;
                $scope.Task = response.data;
                console.log($scope.Task);

                $scope.Task.workDate = new Date(response.data.workDate);
                showFormEdit();
                findNameWork($scope.Task.workId);
            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };
    $scope.deleteTask = function (taskId) {
        $http.delete(constPatchTask + "/task/" + taskId)
            .then(function (response) {
                console.log(response)
                $scope.loadTask();
            }, function errorCallback(response) {
                console.log(response);
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };
    let sendSave = false;
    $scope.saveTaskPost = function () {
        console.log("save");
        console.log($scope.Task);
        console.log(TaskIdEdit);
        if (!sendSave) {
            sendSave = true;
            console.log("sendSave");
            console.log(sendSave);

            $http.post(constPatchTask + "/task", $scope.Task)
                .then(function (response) {
                    console.log(response);
                    $scope.loadTask();
                    showTask();
                    sendSave = false;
                    console.log("send ok");
                    console.log(sendSave);
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log("send false");
                    console.log(sendSave)
                    console.log(response)
                    if ($location.checkAuthorized(response)) {
                        alert(response.data.message);
                    }
                });
        } else {
            alert("Подождите запрос на сохранение данных отправлен");
        }
    }
    $scope.saveTask = function () {
        console.log("save");
        console.log($scope.Task);
        console.log(TaskIdEdit);
        if (!sendSave) {
            sendSave = true;
            console.log("sendSave");
            console.log(sendSave);

            $http.post(constPatchTask + "/task/checkAvail", $scope.Task)
                .then(function (response) {
                    sendSave = false;
                    console.log(response);
                    if(parseInt(response.data.codeInt) === 0){
                        console.log("Сохраняем сразу");
                        $scope.saveTaskPost();
                    } else{
                        let userResponse = confirm(response.data.value + " Продолжить сохранение?");
                        if(userResponse){
                            console.log("Сохраняем с подтверждением");
                            $scope.saveTaskPost();
                        }
                    }
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log("send false");
                    console.log(sendSave)
                    console.log(response)
                    if ($location.checkAuthorized(response)) {
                        alert(response.data.message);
                    }
                });
        } else {
            alert("Подождите запрос на сохранение данных отправлен");
        }
    }
    $scope.addTime = function (taskId) {
        console.log("Другая");
        $location.TaskId = taskId;
        $location.path('/worktime');
    }
    let findNameWork = function (workId) {
        console.log("findNameWork")
        console.log(typeof workId);
        console.log(typeof workId === "undefined");
        if (typeof workId !== "undefined") {
            $http({
                url: constPatchWork + "/works/obj/little/" + workId,
                method: "get"

            }).then(function (response) {
                console.log(response.data);
                document.getElementById("WorkName").value = response.data.name;
                // showTask();
                showWorkNum();

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    console.log("status");
                    console.log(response.status);
                    if (response.status !== 404) {
                        alert(response.data.message);
                    } else {
                        document.getElementById("WorkName").value = response.data.message;
                        // document.getElementById("WorkName").value = "не удалось получить с сервера задачу с id " + workId;
                    }
                    // showFindWork();
                }
            });
        }
    }
    $scope.loadWork = function (diffPage) {
        console.log("loadWork")
        let page = parseInt(document.getElementById("PageWork").value) + diffPage;
        if (page < 1) {
            page = 1;
        }
        $location.saveFilter("taskEditFilter", $scope.FiltWork);
        document.getElementById("PageWork").value = page;
        console.log("page");
        console.log(page);
        console.log(diffPage);
        let FiltWork;
        FiltWork = $scope.FiltWork;

        $http({
            url: constPatchWork + "/works/obj/little",
            method: "get",
            params: {
                page: page,
                size: FiltWork ? FiltWork.size : null,
                name: FiltWork ? FiltWork.name : null

            }
        }).then(function (response) {
            console.log("response :");
            console.log(response);
            console.log("response,data :");
            console.log(response.data);
            $scope.WorkList = response.data.content;

        }, function errorCallback(response) {
            console.log(response)
            if ($location.checkAuthorized(response)) {
                //     alert(response.data.message);
            }

            // showFindTask();
        });

    }
    $scope.filterWork = function () {
        console.log("filterWork");
        document.getElementById("PageWork").value = parseInt("1");
        console.log(document.getElementById("PageWork").value)
        $scope.loadWork(0);
    };
    $scope.setWork = function (workId) {
        document.getElementById("WorkIdEdit").value = workId;
        $scope.Task.workId = workId;
        findNameWork(workId);

    }
    $scope.findWork = function () {
        $scope.loadWork(0);

        showFindWork();
    }

    $scope.Filt = $location.getFilter("taskFilter");
    $scope.FiltWork = $location.getFilter("taskEditFilter");
    if($scope.FiltWork == null){
        $scope.FiltWork ={size:10};
    }
    console.log($scope.Filt);
    if ($scope.Filt === null) {
        $scope.Filt = {
            workId: null,
            size: 10
        }
    }
    console.log("start");
    showTask();
    $scope.loadTask();
})