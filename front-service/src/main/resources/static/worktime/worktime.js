angular.module('workTimeService').controller('worktimeController', function ($scope, $http, $location, $localStorage) {

    const constPatchWorkTime = 'http://localhost:5555/worktime-service/v1';
    const constPatchTask = 'http://localhost:5555/task-service/v1';

    var showWorkTime = function () {
        document.getElementById("WorkTimeList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    var showFormEdit = function () {
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

    var Filt;
    $scope.loadWorkTime = function () {
        console.log("$location.TaskId " + $location.TaskId);
        if(typeof $location.TaskId != "undefined") {
            console.log("load5")
            if (typeof Filt =="undefined")
            {
                Filt ={taskId: null};
            }
            Filt.taskId = $location.TaskId;
            $scope.setFormWorkTime();
            console.log(Filt);
            $location.TaskId = null;
        }

        $scope.findPage(0);
    };

    $scope.setFormWorkTime = function () {
        if(typeof Filt != "undefined") {
            if (Filt.taskId != null) {
                document.getElementById("TaskId").value = Filt.taskId;
            }
            if (Filt.dateLe != null) {
                console.log("Filt.dateLe");
                console.log(Filt.dateLe);
                document.getElementById("DateLe").valueAsDate = new Date(Filt.dateLe);
            }
            if (Filt.dateGe != null) {
                document.getElementById("DateGe").valueAsDate = new Date(Filt.dateGe);
            }
        }
    }

    $scope.findPage = function (diffPage) {
        var page = parseInt(document.getElementById("Page").value) + diffPage;
        document.getElementById("Page").value = page;
        console.log("запрос данных7");
        console.log(Filt);
        $http({
            url: constPatchWorkTime + "/worktime",
            method: "get",
            params: {
                page: page,
                size: 10,
                dateLe: Filt ? Filt.dateLe : null,
                dateGe: Filt ? Filt.dateGe : null,
                taskId: Filt ? Filt.taskId : null

            }


        }).then(function (response) {
            console.log("sssssss");
            $scope.setFormWorkTime();
            console.log("response :" );
            console.log(response);
            console.log("response,data :" );
            console.log(response.data);
            $scope.WorkTimeList = response.data.content;
            showWorkTime();

        }, function errorCallback(response) {
            console.log(response)
            if($location.checkAuthorized(response)){
                //     alert(response.data.message);
            }

            // showFindTask();
        });

    };
    $scope.filterWorkTime = function () {
        Filt = $scope.Filt;
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };

    var WorkTimeIdEdit = null;

    $scope.createWorkTime = function () {
        showFindTask();
        WorkTimeIdEdit = null;
        console.log("создаем");
        $scope.WorkTime = {
            id : null,
            taskId : document.getElementById("TaskId").value,
            workDate: new Date(),
            userName :null,
            workTime: null,
            comment: ""};
            // $scope
        console.log($scope.WorkTime);
        console.log("создаем 6");
        console.log("создаем 3");

        showFormEdit();

    };

    $scope.editWorkTime = function (workTimeId) {
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
                if($location.checkAuthorized(response)){
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
                if($location.checkAuthorized(response)){
                    //     alert(response.data.message);
                }

                // showFindTask();
            });
    };
    $scope.saveWorkTime = function () {
        console.log($scope.WorkTime);
        console.log(WorkTimeIdEdit);

        $http.post(constPatchWorkTime + "/worktime",$scope.WorkTime)
            .then(function (response) {
                console.log("Save response")
                console.log(response);

                $scope.loadWorkTime();
            }, function errorCallback(response) {
                console.log(response.data);
                if($location.checkAuthorized(response)) {

                    alert(response.data.message);
                }
            });
    }
    var FiltTask;
    var findNameTask = function(taskId){
        console.log("findNameTask")

        $http({
            url: constPatchTask + "/task/" + taskId ,
            method: "get"

        }).then(function (response) {
            console.log(response.data);
            document.getElementById("TaskName").value = response.data.description;
            // showTask();
            showTaskNum();

        }, function errorCallback(response) {
            console.log(response)
            if($location.checkAuthorized(response)) {
                alert(response.data.message);
                showFindTask();
            }
        });

    }
    var loadTask = function(taskId, diffPage){
        console.log("loadTask")
        var page = parseInt(document.getElementById("PageTask").value) + diffPage;

        $http({
            url: constPatchTask + "/task",
            method: "get",
            params: {
                page: page,
                size: 10,
                workId: FiltTask ? FiltTask.workId : null,
                codeBTS: FiltTask ? FiltTask.bts : null,
                codeDEVBO: FiltTask ? FiltTask.devbo : null,
                description: FiltTask ? FiltTask.desc : null,
                ziName: FiltTask ? FiltTask.ziName : null

            }
        }).then(function (response) {
            console.log("sssssss");
            // $scope.setFormTask();
            console.log("response :" );
            console.log(response);
            console.log("response,data :" );
            console.log(response.data);
            $scope.TaskList = response.data.content;
            // showTask();

        }, function errorCallback(response) {
            console.log(response)
            if($location.checkAuthorized(response)){
                //     alert(response.data.message);
            }

            // showFindTask();
        });

    }
    $scope.filterTask = function () {
        console.log("filterTask");
        FiltTask = $scope.FiltTask;
        document.getElementById("PageTask").value = "1";
        loadTask(0,0);
    };
    $scope.setTask = function (taskId) {
        document.getElementById("TaskIdEdit").value = taskId;
        $scope.WorkTime.taskId = taskId;
        findNameTask(taskId)

    }
    $scope.findTask = function () {
        loadTask(null,0);

        showFindTask();
    }
    console.log("Start");
    showWorkTime();
    console.log("Show ok");

    $scope.loadWorkTime();
})