angular.module('market').controller('worktimeController', function ($scope, $http, $location, $localStorage) {

    const constPatchWorkTime = 'http://localhost:5555/worktime-service/v1';
    var showWorkTime = function () {
        document.getElementById("WorkTimeList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    var showFormEdit = function () {
        document.getElementById("WorkTimeList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
    };



    $scope.loadWorkTime = function () {
        $scope.findPage(0);
    };
    var Filt;
    $scope.findPage = function (diffPage) {
        var page = parseInt(document.getElementById("Page").value) + diffPage;
        document.getElementById("Page").value = page;
        $http({
            url: constPatchWorkTime + "/worktime",
            method: "get",
            params: {
                page: page,
                size: 10,
                dateLe: Filt ? Filt.datele : null,
                dateGe: Filt ? Filt.datege : null,
                workId: Filt ? Filt.workid : null

            }
        }).then(function (response) {
            $scope.WorkTimeList = response.data.content;
            showWorkTime();
        });

    };
    $scope.filterWorkTime = function () {
        Filt = $scope.Filt;
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };

    var WorkTimeIdEdit = null;

    $scope.createWorkTime = function () {
        WorkTimeIdEdit = null;
        document.getElementById("WorkTimeDate").value = new Date();
        document.getElementById("WorkTimeTime").value = 0;
        $scope.WorkTime.id = null;
        showFormEdit();

    };

    $scope.editWorkTime = function (workTimeId) {
        $http.get(constPatchWorkTime + "/WorkTime/" + workTimeId)
            .then(function (response) {
                WorkTimeIdEdit = response.data.id;
                $scope.WorkTime = response.data;
                console.log($scope.WorkTime);

                document.getElementById("WorkTimeDate").value = response.data.workDate;
                document.getElementById("WorkTimeTime").value = response.data.workTime;
                showFormEdit();
            });
    };
    $scope.deleteWorkTime = function (workTimeId) {
        $http.delete(constPatchWorkTime + "/worktime/" + workTimeId)
            .then(function (response) {
                $scope.loadWorkTime();
            });
    };
    $scope.saveWorkTime = function () {
        console.log($scope.WorkTime);
        console.log(WorkTimeIdEdit);

        $http.post(constPatchWorkTime + "/worktime",$scope.WorkTime)
            .then(function (response) {
                $scope.loadWorkTime();
            });
    };
    $scope.loadWorkTime();
})