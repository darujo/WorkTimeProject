angular.module('workTimeService').controller('workController', function ($scope, $http, $location, $localStorage) {

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
        console.log("отправляем запрос /works")
        console.log(Filt)
        $http({
            url: constPatchWork + "/works",
            method: "get",
            params: {
                page: page,
                size: 10,
                name: Filt ? Filt.name : null,
            }
        }).then(function (response) {
            console.log(response);
            $scope.WorkList = response.data.content;
            console.log($scope.WorkList);
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
        console.log("сбрасываем значения");
        document.getElementById("CodeSap").value = "";
        document.getElementById("CodeZI").value = "";

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
        // № внутренней задачи (DEVBO)
        document.getElementById("Task").value = "";
        // Краткое описание внутренней задачи
        document.getElementById("Description").value = "";
        // Плановая дата завершения 0 этапа
        document.getElementById("PlanDateStage0").value = "";
        // Дата начала доработки План
        document.getElementById("StartTaskPlan").value = "";
        // Дата начала доработки Факт
        document.getElementById("StartTaskFact").value = "";
        // Плановые трудозатраты, чел/час Разработка прототипа
        document.getElementById("LaborDevelop").value = 0;
        // Плановые трудозатраты, чел/час Стабилизация прототипа
        document.getElementById("LaborDebug").value = 0;
        // Плановые трудозатраты, чел/час Стабилизация релиза
        document.getElementById("LaborRelease").value = 0;
        // Плановые трудозатраты, чел/час ОПЭ
        document.getElementById("LaborOPE").value = 0;
        // Текущий этап ЗИ
        document.getElementById("StageZI").value = 0;
        // Порядковый номер релиза
        document.getElementById("Release").value = 0;
        // Выдача релиза даты План
        document.getElementById("IssuingReleasePlan").value = "";
        // Выдача релиза дата факт
        document.getElementById("IssuingReleaseFact").value = "";
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
                document.getElementById("CodeSap").value = response.data.codeSap;
                document.getElementById("CodeZI").value = response.data.codeZI;

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
                // № внутренней задачи (DEVBO)
                document.getElementById("Task").value = response.data.task;
                // Краткое описание внутренней задачи
                document.getElementById("Description").value = response.data.description;
                // Плановая дата завершения 0 этапа
                document.getElementById("PlanDateStage0").valueAsDate = new Date(response.data.planDateStage0);
                response.data.planDateStage0 = document.getElementById("PlanDateStage0").valueAsDate;
                // Дата начала доработки План
                document.getElementById("StartTaskPlan").valueAsDate = new Date(response.data.startTaskPlan);
                response.data.startTaskPlan = document.getElementById("StartTaskPlan").valueAsDate;
                // Дата начала доработки Факт
                document.getElementById("StartTaskFact").valueAsDate = new Date(response.data.startTaskFact);
                response.data.startTaskFact = document.getElementById("StartTaskFact").valueAsDate;
                // Плановые трудозатраты, чел/час Разработка прототипа
                document.getElementById("LaborDevelop").value = response.data.laborDevelop;
                // Плановые трудозатраты, чел/час Стабилизация прототипа
                document.getElementById("LaborDebug").value = response.data.laborDebug;
                // Плановые трудозатраты, чел/час Стабилизация релиза
                document.getElementById("LaborRelease").value = response.data.laborRelease;
                // Плановые трудозатраты, чел/час ОПЭ
                document.getElementById("LaborOPE").value = response.data.laborOPE;
                // Текущий этап ЗИ
                document.getElementById("StageZI").selectedIndex = response.data.stageZI;
                // Порядковый номер релиза
                document.getElementById("Release").value = response.data.release;
                // Выдача релиза даты План
                document.getElementById("IssuingReleasePlan").valueAsDate = new Date(response.data.issuingReleasePlan);
                response.data.issuingReleasePlan = document.getElementById("IssuingReleasePlan").valueAsDate;
                // Выдача релиза дата факт
                document.getElementById("IssuingReleaseFact").valueAsDate = new Date(response.data.issuingReleaseFact);
                response.data.issuingReleaseFact = document.getElementById("IssuingReleaseFact").valueAsDate;

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
        console.log()
        console.log(document.getElementById("StartDevelop").value)
        // $scope.Work.dateStartDevelop = document.getElementById("StartDevelop").value;
        console.log($scope.Work);
        console.log(WorkIdEdit);

        $http.post(constPatchWork + "/works",$scope.Work)
            .then(function (response) {
                $scope.loadWork();
            });
    };
    $scope.addTime = function (workId){
        console.log("Другая");
        $location.WorkId = workId;
        // $location.path('/worktime');
        //window.open('#!/worktime',"_self");
        window.open('#!/task',"_parent");
        // console.log("Другая 2");
        // window.open('#!/worktime',"_self");
        // console.log("Другая 3");
    }
    //_self, _blank, _parent, _top.
    // window.onload = function() {
    //     document.getElementById('myLinkId').addEventListener('click', function(event) {
    //         event.preventDefault();
    //         // Функционал обработки клика
    //     });
    // };
    showWork();
    $scope.loadWork();
})