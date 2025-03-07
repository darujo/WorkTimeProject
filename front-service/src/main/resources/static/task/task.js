angular.module('workTimeService').controller('taskController', function ($scope, $http, $location, $localStorage) {

    const constPatchTask = 'http://localhost:5555/task-service/v1';
    const constPatchWork = 'http://localhost:5555/work-service/v1';

    var showTask = function () {
        document.getElementById("TaskList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    var showFormEdit = function () {
        document.getElementById("TaskList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
    };
    var showWorkNum = function () {
        document.getElementById("WorkNum").style.display = "block";
        document.getElementById("FindWork").style.display = "none";
    };
    var showFindWork = function () {
        document.getElementById("WorkNum").style.display = "none";
        document.getElementById("FindWork").style.display = "block";
        $scope.filterWork();
    };

    var Filt;
    var TaskIdEdit = null;
    $scope.loadTask = function () {
        console.log("loadTask");
        console.log("$location.WorkId " + $location.WorkId);
        if(typeof $location.WorkId != "undefined") {
            console.log("load5")
            if (typeof Filt =="undefined")
            {
                Filt ={workId: null};
            }
            Filt.workId = $location.WorkId;
            $scope.setFormTask();
            console.log(Filt);
            $location.WorkId = null;
        }

        $scope.findPage(0);
    };

    $scope.setFormTask = function () {
        console.log("setFormTask");
        console.log(Filt);
        if(typeof Filt != "undefined") {
            if (Filt.workId != null) {
                document.getElementById("WorkIdFilt").value = Filt.workId;
            }
        }
        console.log(document.getElementById("WorkIdEdit").value);
    }

    $scope.findPage = function (diffPage) {
        console.log("findPage");
        var page = parseInt(document.getElementById("Page").value) + diffPage;
        document.getElementById("Page").value = page;
        console.log("запрос данных7");
        console.log(Filt);
        $http({
            url: constPatchTask + "/task",
            method: "get",
            params: {
                page: page,
                size: 10,
                workId: Filt ? Filt.workId : null,
                codeBTS: Filt ? Filt.bts : null,
                codeDEVBO: Filt ? Filt.devbo : null,
                description: Filt ? Filt.desc : null,
                ziName: Filt ? Filt.ziName : null

            }
        }).then(function (response) {
            console.log("sssssss");
            $scope.setFormTask();
            console.log("response :" );
            console.log(response);
            console.log("response,data :" );
            console.log(response.data);
            console.log(response.data.content);
            if (typeof response.data.content =="undefined")
            {
                $scope.TaskList = response.data;
            }
            else {
                $scope.TaskList = response.data.content;
            }
            showTask();
            }, function errorCallback(response) {
                console.log(response)
                if($location.checkAuthorized(response)){
                    //     alert(response.data.message);
                }
            }
        );

    };
    $scope.filterTask = function () {
        console.log("filterTask");
        Filt = $scope.Filt;
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };

    $scope.createTask = function () {
        showFindWork();
        console.log("create");
        TaskIdEdit = null;
        console.log("создаем");
        // document.getElementById("TaskIdEdit").value = null;
        console.log("создаем 1");
        console.log(typeof $scope.Task);
        $scope.Task = {
            id : null,
            workId : null,
            codeBTS: null,
            userName :null,
            codeDEVBO:null,
            description: null,
            type: 1
        };
            // $scope
        console.log($scope.Task);
        console.log("создаем 5");
        $scope.Task.workId = parseInt(document.getElementById("WorkIdFilt").value);
        console.log($scope.Task.workId);
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
                if($location.checkAuthorized(response)){
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
                if($location.checkAuthorized(response)){
                    //     alert(response.data.message);
                }
            });
    };
    var sendSave = false;
    $scope.saveTask = function () {
        console.log("save");
        console.log($scope.Task);
        console.log(TaskIdEdit);
        if(!sendSave){
            sendSave = true;
            console.log("sendSave");
            console.log(sendSave);

            $http.post(constPatchTask + "/task",$scope.Task)
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
                    if($location.checkAuthorized(response)){
                        alert(response.data.message);
                    }
            });
        }
        else{
            alert("Подождите запрос на сохранение данных отправлен");
        }
    }
    $scope.addTime = function (taskId){
        console.log("Другая");
        $location.TaskId = taskId;
        $location.path('/worktime');
    }
    var FiltWork;
    var findNameWork = function(workId){
        console.log("findNameWork")

        $http({
            url: constPatchWork + "/works/obj/little/" + workId ,
            method: "get"

        }).then(function (response) {
            console.log(response.data);
            document.getElementById("WorkName").value = response.data.name;
            // showTask();
            showWorkNum();

        }, function errorCallback(response) {
            console.log(response)
            if($location.checkAuthorized(response)) {
                alert(response.data.message);
                // showFindWork();
            }
        });

    }
    $scope.loadWork = function( diffPage){
        console.log("loadWork")
        var page = parseInt(document.getElementById("PageWork").value) + diffPage;
        if(page < 1) {
            page = 1;
        }
        document.getElementById("PageWork").value = page;
        console.log("page");
        console.log(page);
        console.log(diffPage);
        $http({
            url: constPatchWork + "/works/obj/little",
            method: "get",
            params: {
                page: page,
                size: 10,
                name: FiltWork ? FiltWork.name : null

            }
        }).then(function (response) {
            console.log("sssssss");
            console.log("response :" );
            console.log(response);
            console.log("response,data :" );
            console.log(response.data);
            $scope.WorkList = response.data.content;

        }, function errorCallback(response) {
            console.log(response)
            if($location.checkAuthorized(response)){
                //     alert(response.data.message);
            }

            // showFindTask();
        });

    }
    $scope.filterWork = function () {
        console.log("filterWork");
        FiltWork = $scope.FiltWork;
        document.getElementById("PageWork").value = "1";
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
    console.log("start");
    showTask();
    $scope.loadTask();
})