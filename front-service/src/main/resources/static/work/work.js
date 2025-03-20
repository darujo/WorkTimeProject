angular.module('workTimeService').controller('workController', function ($scope, $http, $location, $localStorage) {
    console.log(window.location)
    const constPatchWork = window.location.origin + '/work-service/v1';
    var showWork = function () {
        document.getElementById("WorkList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    var showFormEdit = function () {
        console.log("showFormEdit");
        document.getElementById("WorkList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";

    };



    $scope.loadWork = function () {
        $scope.findPage(0);
    };
    var Filt;
    $scope.findPage = function (diffPage) {
        var page = parseInt(document.getElementById("Page").value) + diffPage;
        document.getElementById("Page").value = page;
        console.log("отправляем запрос /works");
        console.log($scope.Filt);
        if (typeof  $scope.Filt === "undefined")
        {
            $scope.Filt ={size:10};
            Filt = $scope.Filt;
        }
        console.log(Filt)
        $http({
            url: constPatchWork + "/works",
            method: "get",
            params: {
                page: page,
                size: Filt ? Filt.size : null,
                name: Filt ? Filt.name : null,
                codeSap: Filt ? Filt.codeSap : null,
                codeZi: Filt ? Filt.codeZi : null,
                task: Filt ? Filt.task : null,
                sort: Filt ? Filt.sort : null,
                stageZi: Filt ? Filt.stageZi : null
            }
        }).then(function (response) {
            console.log(response);
            $scope.WorkList = response.data.content;
            console.log($scope.WorkList);
            showWork();
        }, function errorCallback(response) {
            console.log(response)
            if($location.checkAuthorized(response)){
                alert(response.data.message);
            }

            // showFindTask();
        });

    };

    $scope.workSort = function (sort){
        $scope.Filt  ={sort:sort,
            size: $scope.Filt ? $scope.Filt.size : null,
            name: $scope.Filt ? $scope.Filt.name : null,
            codeSap: $scope.Filt ? $scope.Filt.codeSap : null,
            codeZi: $scope.Filt ? $scope.Filt.codeZi : null,
            task: $scope.Filt ? $scope.Filt.task : null,
            stageZi: $scope.Filt ? $scope.Filt.stageZi : null};
        console.log("sort");
        console.log(sort);
        $scope.filterWork();
    }
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
            developEndFact: null,
            debugEndFact: null,
            releaseEndFact: null,
            opeEndFact: null,
            analiseEndFact: null,
            developEndPlan: null,
            debugEndPlan: null,
            releaseEndPlan: null,
            opeEndPlan: null,
            analiseEndPlan: null,
            task: "",
            description: "",
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
                $scope.Work.developEndFact = new Date(response.data.developEndFact);
                $scope.Work.debugEndFact = new Date(response.data.debugEndFact);
                $scope.Work.releaseEndFact = new Date( response.data.releaseEndFact );
                $scope.Work.opeEndFact = new Date( response.data.opeEndFact );
                $scope.Work.analiseEndFact = new Date( response.data.analiseEndFact );
                $scope.Work.developEndPlan = new Date( response.data.developEndPlan );
                $scope.Work.debugEndPlan = new Date( response.data.debugEndPlan );
                $scope.Work.releaseEndPlan = new Date( response.data.releaseEndPlan );
                $scope.Work.opeEndPlan = new Date( response.data.opeEndPlan );
                $scope.Work.analiseEndPlan = new Date( response.data.analiseEndPlan );

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
                console.log(response);
                $scope.loadWork();
            }, function errorCallback(response) {
                console.log(response)
                if($location.checkAuthorized(response)){
                //     alert(response.data.message);
                }

                // showFindTask();
            });
    };
    var sendSave= false;
    $scope.saveWork = function () {
        console.log()
        console.log($scope.Work);
        console.log(WorkIdEdit);
      if (!sendSave){
          sendSave =true;
          $http.post(constPatchWork + "/works",$scope.Work)
              .then(function (response) {
                  sendSave= false;
                  console.log(response);
                  showWork();
                  $scope.loadWork();
                }, function errorCallback(response) {
                  sendSave= false;
                  console.log(response)
                  if($location.checkAuthorized(response)){
                        //     alert(response.data.message);
                  }

                    // showFindTask();
                });
      }
    };
    $scope.Filt = {stageZi: 15}
    $scope.addTime = function (workId){
        console.log("Другая");
        $location.WorkId = workId;
        $location.path('/task');
        // window.open('#!/task',"_parent");
    }
    showWork();
    $scope.loadWork();
})