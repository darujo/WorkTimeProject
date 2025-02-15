angular.module('market').controller('workController', function ($scope, $http, $location, $localStorage) {

    const constPatchWork = 'http://localhost:5555/work-service/v1';
    var showWork = function () {
        document.getElementById("WorkList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    var showFormEdit = function () {
        document.getElementById("WorkList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
        console.log("dddddddd");
    };



    $scope.loadWork = function () {
        // showWork();
        $scope.findPage(0);
    };
    var Filt;
    $scope.findPage = function (diffPage) {
        var page = parseInt(document.getElementById("Page").value) + diffPage;
        document.getElementById("Page").value = page;
        $http({
            url: constPatchWork + "/works",
            method: "get",
            params: {
                page: page,
                size: 10,
                name: Filt ? Filt.name : null,
            }
        }).then(function (response) {
            $scope.WorkList = response.data.content;
            showWork();
        });

    };
    $scope.filterWork = function () {
        Filt = $scope.Filt;
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };

    var WorkIdEdit = null;

    $scope.createWork = function () {
        WorkIdEdit = null;
        console.log("сбрасываем значения")
        document.getElementById("WorkName").value = "";
        console.log("сбрасываем значения 1")
        document.getElementById("StartDevelop").value = null;
        console.log("сбрасываем значения 2")
        document.getElementById("StartDebug").value = "";
        document.getElementById("StartRelease").value = "";
        document.getElementById("StartOPE").value = "";
        document.getElementById("StartWender").value = "";
        console.log("сбрасываем значения 3")
        if (typeof $scope.Work != "undefined") {
            $scope.Work.id = null;
        }
        console.log("сбрасываем значения 4")
        showFormEdit();

    };

    $scope.editWork = function (workId) {
        $http.get(constPatchWork + "/works/" + workId)
            .then(function (response) {
                console.log("получили");
                WorkIdEdit = response.data.id;
                $scope.Work = response.data;
                console.log($scope.Work);

                document.getElementById("WorkName").value = response.data.name;
                document.getElementById("StartDevelop").valueAsDate = new Date( response.data.dateStartDevelop);
                response.data.dateStartDevelop = document.getElementById("StartDevelop").valueAsDate;
                console.log(response.data.dateStartDevelop);
                document.getElementById("StartDebug").valueAsDate = new Date( response.data.dateStartDebug );
                response.data.dateStartDebug = document.getElementById("StartDebug").valueAsDate;
                document.getElementById("StartRelease").valueAsDate = new Date( response.data.dateStartRelease );
                response.data.dateStartReleas = document.getElementById("StartRelease").valueAsDate;
                document.getElementById("StartOPE").valueAsDate = new Date( response.data.dateStartOPE );
                response.data.dateStartOPE = document.getElementById("StartOPE").valueAsDate;
                document.getElementById("StartWender").valueAsDate = new Date( response.data.dateStartWender );
                response.data.dateStartWender =document.getElementById("StartWender").valueAsDate;
                showFormEdit();
            });
    };
    $scope.deleteWork = function (workId) {
        $http.delete(constPatchWork + "/works/" + workId)
            .then(function (response) {
                $scope.loadWork();
            });
    };
    $scope.saveWork = function () {
        console.log(document.getElementById("StartDevelop").value)
        // $scope.Work.dateStartDevelop = document.getElementById("StartDevelop").value;
        console.log($scope.Work);
        console.log(WorkIdEdit);

        $http.post(constPatchWork + "/works",$scope.Work)
            .then(function (response) {
                $scope.loadWork();
            });
    };
    $scope.addTime = function (workId ,event){
        event.preventDefault();
        $scope.WorkId = workId;
        $location.path('/worktime');
    }
    window.onload = function() {
        document.getElementById('myLinkId').addEventListener('click', function(event) {
            event.preventDefault();
            // Функционал обработки клика
        });
    };
    $scope.loadWork();
})