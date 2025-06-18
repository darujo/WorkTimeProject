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
    $scope.Filt = {favouriteTask: null}
    let TaskIdEdit = null;
    let arrTaskId = [];
    $scope.loadTask = function () {
        console.log("loadTask");

        $location.parserFilter($scope.Filt);

        $scope.findPage(0);
    };

    $scope.setFormTask = function () {
        console.log("setFormTask");
        console.log(document.getElementById("WorkIdEdit").value);
    }
    let maxPage = 1;
    $scope.findPage = function (diffPage) {
        console.log("findPage");
        let page = parseInt(document.getElementById("Page").value) + diffPage;
        console.log("запрос данных7");
        if (page > maxPage) {
            page = maxPage;

        }
        if (page < 1) {
            page = 1;
        }
        document.getElementById("Page").value = page;
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load = true;
            $scope.TaskList = null;
            let Filter;
            Filter = $scope.Filt;
            console.log(Filter);
            $http({
                url: constPatchTask + "/task",
                method: "get",
                params: {
                    page: page,
                    size: Filter ? Filter.size : null,
                    workId: Filter ? Filter.workId : null,
                    codeBTS: Filter ? Filter.bts : null,
                    codeDEVBO: Filter ? Filter.devbo : null,
                    description: Filter ? Filter.desc : null,
                    ziName: Filter ? Filter.ziName : null,
                    type: Filter ? Filter.type : null,
                    arrTaskId: Filter.favouriteTask ? arrTaskId : Filter ? Filter.listId : null,

                }
            }).then(function (response) {
                    $scope.load = false;
                    $scope.setFormTask();
                    console.log("response :");
                    console.log(response);
                    console.log("response,data :");
                    console.log(response.data);
                    console.log(response.data.content);
                    if (typeof response.data.content === "undefined") {
                        $scope.TaskList = response.data;
                        maxPage = 1;
                    } else {
                        $scope.TaskList = response.data.content;
                        maxPage = response.data["totalPages"];
                    }
                    showTask();
                }, function errorCallback(response) {
                    $scope.load = false;
                    console.log(response)
                    if ($location.checkAuthorized(response)) {
                        //     alert(response.data.message);
                    }
                }
            );
        }
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
            codeDEVBO: "DEVBO-000",
            description: null,
            type: 1

        };
        // $scope
        console.log($scope.Task);
        console.log("создаем 5");
        $scope.Task.workId = parseInt(document.getElementById("WorkIdFilter").value);
        console.log($scope.Task.workId);
        if (!isNaN($scope.Task.workId)) {
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
                    if (parseInt(response.data.codeInt) === 0) {
                        console.log("Сохраняем сразу");
                        $scope.saveTaskPost();
                    } else {
                        let userResponse = confirm(response.data.value + " Продолжить сохранение?");
                        if (userResponse) {
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
        $location.path('/workTime'.toLowerCase()).search({taskId: taskId, currentUser: false});
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
        let FilterWork;
        FilterWork = $scope.FiltWork;

        $http({
            url: constPatchWork + "/works/obj/little",
            method: "get",
            params: {
                page: page,
                size: FilterWork ? FilterWork.size : null,
                name: FilterWork ? FilterWork.name : null

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
    $scope.clearWork = function () {
        $scope.Task.workId = null;
        document.getElementById("WorkName").value = "";
    }
    $scope.setWork = function (workId) {
        document.getElementById("WorkIdEdit").value = workId;
        $scope.Task.workId = workId;
        findNameWork(workId);

    }
    $scope.findWork = function () {
        $scope.loadWork(0);

        showFindWork();
    }
    $scope.clearFilter = function (load) {
        $scope.Filt = {
            workId: null,
            size: 10
        }
        if (load) {
            $scope.filterWork();
        }
    }

    $scope.sendFilter = function () {
        $location.sendFilter(location.hash, $scope.Filt);
    }

    let callBackType = function (response) {
        console.log("TaskListType");
        console.log(response);
        $scope.TaskListType = response.data;
    }

    if (typeof $localStorage.favourites !== "undefined"
        && typeof $localStorage.favourites.listTaskID !== "undefined") {
        arrTaskId = $localStorage.favourites.listTaskID;
    }
    $scope.addDelTaskId = function (element) {
        let i = arrTaskId.indexOf(element);
        if (-1 === i) {
            $scope.addTaskId(element);
        } else {
            $scope.delTaskId(element);
        }
    }

    $scope.addTaskId = function (element) {
        console.log("addTaskId");

        let i = arrTaskId.indexOf(element);
        if (-1 === i) {
            arrTaskId.push(element);
            $localStorage.favourites = {listTaskID: arrTaskId};
        }
        console.log(arrTaskId);
    }
    $scope.delTaskId = function (element) {
        console.log("delTaskId");
        let i = arrTaskId.indexOf(element);
        if (-1 !== i) {
            arrTaskId.splice(i, 1);
            $localStorage.favourites = {listTaskID: arrTaskId};
        }
    }
    $scope.getStyle = function (taskId) {
        let i = arrTaskId.indexOf(taskId);
        if (-1 !== i) {
            return {
                'background-color': 'yellow'
            };
        } else {
            return {};
        }
    };

    $location.getCode("task/code/type", callBackType);
    $scope.Filt = $location.getFilter("taskFilter");
    $scope.FiltWork = $location.getFilter("taskEditFilter");
    if ($scope.FiltWork == null) {
        $scope.FiltWork = {size: 10};
    }
    console.log($scope.Filt);
    if ($scope.Filt === null) {
        $scope.clearFilter(false);
    }
    console.log("start");
    showTask();
    $scope.loadTask();
})