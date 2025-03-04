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
        $scope.Work = {
            codeZI: "",
            codeSap: "",
            WorkName: "",
            dateStartDevelop: null,
            dateStartDebug: null,
            dateStartRelease: null,
            dateStartOPE: null,
            dateStartWender: null,
            dateStartDevelopPlan: null,
            dateStartDebugPlan: null,
            dateStartReleasePlan: null,
            dateStartOPEPlan: null,
            dateStartWenderPlan: null,
            task: "",
            description: "",
            planDateStage0: null,
            factDateStage0: null,
            startTaskPlan: null,
            startTaskFact: null,
            laborDevelop: null,
            laborDebug: null,
            laborRelease: null,
            laborOPE: null,
            stageZI: 0,
            release: 0,
            issuingReleasePlan: null,
            issuingReleaseFact: null

        };
        console.log("сбрасываем значения 4")
        showFormEdit();

    };

    $scope.editWork = function (workId) {
        $http.get(constPatchWork + "/works/" + workId)
            .then(function (response) {
                console.log("получили");
                showFormEdit();
                WorkIdEdit = response.data.id;
                $scope.Work = response.data;
                console.log($scope.Work);
                $scope.Work.dateStartDevelop = new Date(response.data.dateStartDevelop);
                $scope.Work.dateStartDebug = new Date(response.data.dateStartDebug);
                $scope.Work.dateStartRelease = new Date( response.data.dateStartRelease );
                $scope.Work.dateStartOPE = new Date( response.data.dateStartOPE );
                $scope.Work.dateStartWender = new Date( response.data.dateStartWender );
                $scope.Work.dateStartDevelopPlan = new Date( response.data.dateStartDevelopPlan );
                $scope.Work.dateStartDebugPlan = new Date( response.data.dateStartDebugPlan );
                $scope.Work.dateStartReleasePlan = new Date( response.data.dateStartReleasePlan );
                $scope.Work.dateStartOPEPlan = new Date( response.data.dateStartOPEPlan );
                $scope.Work.dateStartWenderPlan = new Date( response.data.dateStartWenderPlan );

                // Плановая дата завершения 0 этапа
                $scope.Work.planDateStage0 = new Date(response.data.planDateStage0);
                $scope.Work.factDateStage0 = new Date(response.data.factDateStage0);
                // Дата начала доработки План
                $scope.Work.startTaskPlan = new Date(response.data.startTaskPlan);
                // Дата начала доработки Факт
                $scope.Work.startTaskFact = new Date(response.data.startTaskFact);
                // Выдача релиза даты План
                $scope.Work.issuingReleasePlan = new Date(response.data.issuingReleasePlan);
                // Выдача релиза дата факт
                $scope.Work.issuingReleaseFact = new Date(response.data.issuingReleaseFact);

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
        window.open('#!/task',"_parent");
    }
    showWork();
    $scope.loadWork();
})