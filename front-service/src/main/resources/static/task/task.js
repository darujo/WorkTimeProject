angular.module('workTimeService').controller('taskController', function ($scope, $http, $location, $localStorage) {

    const constPatchTask = 'http://localhost:5555/task-service/v1';
    var showTask = function () {
        document.getElementById("TaskList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    var showFormEdit = function () {
        document.getElementById("TaskList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
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
        if(typeof Filt != "undefined") {
            if (Filt.workId != null) {
                document.getElementById("WorkIdEdit").value = Filt.workId;
            }
            // if (Filt.dateLe != null) {
    //             document.getElementById("DateLe").value = Filt.dateLe;
    //         }
    //         if (Filt.dateGe != null) {
    //             document.getElementById("DateGe").value = Filt.dateGe;
    //         }
        }
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
                // dateLe: Filt ? Filt.dateLe : null,
                // dateGe: Filt ? Filt.dateGe : null,
                workId: Filt ? Filt.workId : null,
                codeBTS: Filt ? Filt.bts : null,
                codeDEVBO: Filt ? Filt.devbo : null,
                description: Filt ? Filt.desc : null,
                ziName: Filt ? Filt.ziName : null

            }
            // ,
            // data:
            //     Filt


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
            }
        , function errorCallback(response) {
            console.log(response);
            if(response.status = 401){

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
        console.log("create");
        TaskIdEdit = null;
        console.log("создаем");
        document.getElementById("TaskIdEdit").value = null;
        console.log("создаем 1");
        console.log(typeof $scope.Task);
        if (typeof $scope.Task == "undefined") {
            // $scope.WorkTime = {id : null, workId : document.getElementById("WorkId").value, workDate: document.getElementById("WorkTimeDate").valueAsDate };
            $scope.Task = {
                id : null,
                workId : null,
                codeBTS: null,
                userName :null,
                codeDEVBO:null,
                description: null,
                type: null
            };
            // $scope
            console.log($scope.Task);
            console.log("создаем 6");
        }
        // else {
           console.log("создаем 5");
        $scope.Task.id = null;
        $scope.Task.workId = document.getElementById("WorkId").value;
        $scope.Task.codeBTS = null;
        $scope.Task.codeDEVBO = null;
        $scope.Task.userName = null;
        $scope.Task.description = null;
        $scope.Task.type = 1;

        // }


        console.log("создаем 3");

        showFormEdit();

    };

    $scope.editTask = function (taskId) {
        console.log("edit");
        $http.get(constPatchTask + "/task/" + taskId)
            .then(function (response) {
                TaskIdEdit = response.data.id;
                $scope.Task = response.data;
                console.log($scope.Task);

                // document.getElementById("WorkTimeDate").valueAsDate = new Date(response.data.workDate);
                // console.log("eeee 1");
                // response.data.workDate = document.getElementById("WorkTimeDate").value.
                // $scope.Task.workDate = document.getElementById("WorkTimeDate").valueAsDate;
                // console.log("eeee 3")
                // document.getElementById("WorkTimeTime").value = response.data.workTime;
                // console.log("eeee 4")

                showFormEdit();
            });
    };
    $scope.deleteTask = function (taskId) {
        $http.delete(constPatchTask + "/task/" + taskId)
            .then(function (response) {
                $scope.loadTask();
            });
    };
    $scope.saveTask = function () {
        console.log("save");
        console.log($scope.Task);
        console.log(TaskIdEdit);

        $http.post(constPatchTask + "/task",$scope.Task)
            .then(function (response) {
                $scope.loadTask();
            }, function errorCallback(response) {
                // console.log(response);
                console.log(response.data);
                // console.log(response.config);

                alert(response.data.message);
            });
    }
    $scope.addTime = function (taskId){
        console.log("Другая");
        $location.TaskId = taskId;
        // $location.path('/worktime');
        //window.open('#!/worktime',"_self");
        // window.open('#!/task',"_parent");
        // console.log("Другая 2");
        window.open('#!/worktime',"_self");
        // console.log("Другая 3");
    }
    console.log("start");
    showTask();
    $scope.loadTask();
})