angular.module('workTimeService').controller('workTimeRepController', function ($scope, $http, $location, $localStorage) {

    const constPatchWork = 'http://localhost:5555/work-service/v1';
    // var showWorkTime = function () {
    //     document.getElementById("WorkTimeList").style.display = "block";
    //     document.getElementById("FormEdit").style.display = "none";
    // };

    var Filt;
    $scope.loadWorkTime = function () {
        console.log("qqqqqqqq 2");
        $scope.findPage();
    };

    // $scope.setFormWorkTime = function () {
    //     if(typeof Filt != "undefined") {
    //         if (Filt.workId != null) {
    //             document.getElementById("WorkId").value = Filt.workId;
    //         }
    //         if (Filt.dateLe != null) {
    //             document.getElementById("DateLe").value = Filt.dateLe;
    //         }
    //         if (Filt.dateGe != null) {
    //             document.getElementById("DateGe").value = Filt.dateGe;
    //         }
    //     }
    // }

    $scope.findPage = function () {
        console.log("qqqqqqqq 3");

        $http({
            url: constPatchWork + "/works/rep",
            method: "get",
            params: {
                userName: Filt ? Filt.userName : null

            }
        }).then(function (response) {
            console.log("qqqqqqqq 4");

            // $scope.setFormWorkTime();
            console.log(response.data);
            $scope.WorkTimeList = response.data;
            // showWorkTime();

        });
        console.log("qqqqqqqq 5");


    };
    $scope.filterWorkTime = function () {
        console.log("qqqqqqqq 6");

        Filt = $scope.Filt;
        // document.getElementById("Page").value = "1";
        $scope.findPage();
    };

    // var WorkTimeIdEdit = null;
    //
    // $scope.createWorkTime = function () {
    //     WorkTimeIdEdit = null;
    //     console.log("создаем");
    //     document.getElementById("WorkIdEdit").value = document.getElementById("WorkId").value;
    //     document.getElementById("WorkTimeDate").valueAsDate = new Date();
    //     console.log("создаем 1");
    //     document.getElementById("WorkTimeTime").value = 0;
    //     console.log("создаем 2");
    //     console.log(typeof $scope.WorkTime);
    //     if (typeof $scope.WorkTime == "undefined") {
    //         // $scope.WorkTime = {id : null, workId : document.getElementById("WorkId").value, workDate: document.getElementById("WorkTimeDate").valueAsDate };
    //         $scope.WorkTime = {id : null, workId : null, workDate: null,userName :null,workTime:null };
    //         // $scope
    //         console.log($scope.WorkTime);
    //         console.log("создаем 6");
    //     }
    //     // else {
    //        console.log("создаем 5");
    //     $scope.WorkTime.id = null;
    //     $scope.WorkTime.workId = document.getElementById("WorkId").value;
    //     $scope.WorkTime.workDate = document.getElementById("WorkTimeDate").valueAsDate;
    //     $scope.WorkTime.workTime = null;
    //     $scope.WorkTime.userName = null;
    //     // }
    //     console.log("создаем 3");
    //
    //     showFormEdit();
    //
    // };

    // $scope.editWorkTime = function (workTimeId) {
    //     console.log("edit");
    //     $http.get(constPatchWorkTime + "/worktime/" + workTimeId)
    //         .then(function (response) {
    //             WorkTimeIdEdit = response.data.id;
    //             $scope.WorkTime = response.data;
    //             console.log($scope.WorkTime);
    //
    //             document.getElementById("WorkTimeDate").valueAsDate = new Date(response.data.workDate);
    //             console.log("eeee 1");
    //             // response.data.workDate = document.getElementById("WorkTimeDate").value.
    //             $scope.WorkTime.workDate = document.getElementById("WorkTimeDate").valueAsDate;
    //             console.log("eeee 3")
    //             document.getElementById("WorkTimeTime").value = response.data.workTime;
    //             console.log("eeee 4")
    //
    //             showFormEdit();
    //         });
    // };
    // $scope.deleteWorkTime = function (workTimeId) {
    //     $http.delete(constPatchWorkTime + "/worktime/" + workTimeId)
    //         .then(function (response) {
    //             $scope.loadWorkTime();
    //         });
    // };
    // $scope.saveWorkTime = function () {
    //     console.log($scope.WorkTime);
    //     console.log(WorkTimeIdEdit);
    //
    //     $http.post(constPatchWorkTime + "/worktime",$scope.WorkTime)
    //         .then(function (response) {
    //             $scope.loadWorkTime();
    //         }, function errorCallback(response) {
    //             // console.log(response);
    //             console.log(response.data);
    //             // console.log(response.config);
    //
    //             alert(response.data.message);
    //         });
    // }
    console.log("qqqqqqqq 1");

    $scope.loadWorkTime();
})