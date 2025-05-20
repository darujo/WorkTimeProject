angular.module('workTimeService').controller('workController', function ($scope, $http, $location) {
    console.log(window.location)
    const constPatchWork = window.location.origin + '/work-service/v1';
    let showWork = function () {
        document.getElementById("WorkList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    let showFormEdit = function () {
        console.log("showFormEdit");
        console.log($scope.Work)

        checkRight("Edit", false);
        console.log($scope.Work)

        document.getElementById("WorkList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
        console.log($scope.Work)

    };

    let checkRight = function (right, message) {
        document.getElementById("ButtonSaveDown").style.display = "none";
        document.getElementById("ButtonSaveUp").style.display = "none";
        let flag;
        $scope.Resp = {message: null}
        $http({
            url: constPatchWork + "/works/right/" + right,
            method: "get"
        }).then(function (response) {
                console.log(response)
                document.getElementById("ButtonSaveDown").style.display = "inline-block";
                document.getElementById("ButtonSaveUp").style.display = "inline-block";
                if (right === "create") {
                    $scope.createWorkRun();
                }
            }, function errorCallback(response) {
                console.log("Check");
                console.log(response)
                flag = false;
                if ($location.checkAuthorized(response)) {
                    if (message) {
                        alert(response.data.message);
                    } else {
                        $scope.Resp = {message: response.data.message};
                    }

                }
            }
        )
        return false;
    }


    $scope.loadWork = function () {
        $location.parserFilter($scope.Filt);
        $scope.findPage(0);
    };
    $scope.sendFilter = function () {
        $location.sendFilter(location.hash,$scope.Filt);
    }
    let maxPage = 1;
    $scope.findPage = function (diffPage) {
        console.log(diffPage)
        let page = parseInt(document.getElementById("Page").value) + diffPage;
        if (page > maxPage) {
            page = maxPage;

        }
        if (page < 1) {
            page = 1;
        }
        console.log("page");
        console.log(page);
        document.getElementById("Page").value = page;
        console.log("отправляем запрос /works");
        console.log($scope.Filt);
        if (typeof $scope.Filt === "undefined") {
            $scope.Filt = {size: 10};
        }
        let Filt = $scope.Filt;
        console.log($scope.Filt)
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
                releaseId: Filt ? Filt.releaseId :null,
                sort: Filt ? Filt.sort : null,
                stageZi: Filt ? Filt.stageZi : null
            }
        }).then(function (response) {
            console.log(response);
            $scope.WorkList = response.data.content;
            console.log($scope.WorkList);
            maxPage = response.data.totalPages;
            showWork();
        }, function errorCallback(response) {
            console.log(response)
            if ($location.checkAuthorized(response)) {
                alert(response.data.message);
            }

            // showFindTask();
        });

    };

    $scope.workSort = function (sort) {
        $scope.Filt = {
            sort: sort,
            size: $scope.Filt ? $scope.Filt.size : null,
            name: $scope.Filt ? $scope.Filt.name : null,
            codeSap: $scope.Filt ? $scope.Filt.codeSap : null,
            codeZi: $scope.Filt ? $scope.Filt.codeZi : null,
            task: $scope.Filt ? $scope.Filt.task : null,
            releaseId: $scope.Filt ? $scope.Filt.releaseId :null,
            stageZi: $scope.Filt ? $scope.Filt.stageZi : null
        };
        console.log("sort");
        console.log(sort);
        $scope.filterWork();
    }
    $scope.filterWork = function () {
        $location.saveFilter("workFilter",$scope.Filt);
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };

    let WorkIdEdit = null;

    $scope.createWork = function () {
        $scope.Work = {
            id: null,
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
        checkRight("create", true);
    }
    $scope.createWorkRun = function () {
        WorkIdEdit = null;
        console.log("сбрасываем значения");

        console.log("сбрасываем значения 4")
        showFormEdit();

    };

    $scope.editWork = function (workId) {
        $http.get(constPatchWork + "/works/" + workId)
            .then(function (response) {
                console.log("получили");
                // showFormEdit();
                WorkIdEdit = response.data.id;
                $scope.Work = response.data;
                console.log($scope.Work);
                $scope.Work.developEndFact = typeof  response.data.developEndFact === "undefined" ? null : new Date(response.data.developEndFact);
                $scope.Work.debugEndFact = typeof  response.data.debugEndFact === "undefined" ? null : new Date(response.data.debugEndFact);
                $scope.Work.releaseEndFact = typeof  response.data.releaseEndFact === "undefined" ? null : new Date(response.data.releaseEndFact);
                $scope.Work.opeEndFact = typeof  response.data.opeEndFact === "undefined" ? null : new Date(response.data.opeEndFact);
                $scope.Work.analiseEndFact = typeof  response.data.analiseEndFact === "undefined" ? null : new Date(response.data.analiseEndFact);
                $scope.Work.developEndPlan = typeof  response.data.developEndPlan === "undefined" ? null : new Date(response.data.developEndPlan);
                $scope.Work.debugEndPlan = typeof  response.data.debugEndPlan === "undefined" ? null : new Date(response.data.debugEndPlan);
                $scope.Work.releaseEndPlan = typeof  response.data.releaseEndPlan === "undefined" ? null : new Date(response.data.releaseEndPlan);
                $scope.Work.opeEndPlan = typeof  response.data.opeEndPlan === "undefined" ? null : new Date(response.data.opeEndPlan);
                $scope.Work.analiseEndPlan = typeof  response.data.analiseEndPlan === "undefined" ? null : new Date(response.data.analiseEndPlan);

                $scope.Work.startTaskPlan = typeof  response.data.startTaskPlan === "undefined" ? null : new Date(response.data.startTaskPlan);
                // Дата начала доработки Факт
                $scope.Work.startTaskFact = typeof  response.data.startTaskFact === "undefined" ? null : new Date(response.data.startTaskFact);
                // Выдача релиза даты План
                console.log(typeof  response.data.issuingReleasePlan === "undefined")
                $scope.Work.issuingReleasePlan = typeof  response.data.issuingReleasePlan === "undefined" ? null : new Date(response.data.issuingReleasePlan);
                // Выдача релиза дата факт
                if (typeof  response.data.issuingReleaseFact !== "undefined"){
                    $scope.Work.issuingReleaseFact = new Date(response.data.issuingReleaseFact);
                }
                console.log($scope.Work)
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
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

                // showFindTask();
            });
    };
    let sendSave = false;
    $scope.saveWork = function () {
        console.log()
        console.log($scope.Work);
        console.log(WorkIdEdit);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchWork + "/works", $scope.Work)
                .then(function (response) {
                    sendSave = false;
                    console.log(response);
                    showWork();
                    $scope.loadWork();
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log(response)
                    if ($location.checkAuthorized(response)) {
                            alert(response.data.message);
                    }

                    // showFindTask();
                });
        }
    };

    $scope.addTime = function (workId) {
        $location.path('/task').search({workId : workId});
        // window.open('#!/task',"_parent");
    }
    $scope.addRate = function (workId) {
        console.log("Другая");
        $location.path('/rate').search({workId: workId});
    }
    $scope.clearFilter =function (load){
        console.log("clearFilter");
        $scope.Filt = {
            stageZi: 15,
            size: 10
        }
        console.log($scope.Filt);
        if(load){
            $scope.filterWork();
        }
    }
    $scope.releaseOption= function (release){
        console.log("----- rlesewwww")
        console.log(release)
        console.log(typeof release.issuingReleaseFact !== "undefined" && release.issuingReleaseFact !== null)
        if(typeof release.issuingReleaseFact !== "undefined"  && release.issuingReleaseFact !== null){
            return "disabled";
        }
        return false;
    }
    showWork();
    console.log("---workFilter---");
    $scope.Filt = $location.getFilter("workFilter");
    console.log($scope.Filt );
    if($scope.Filt === null) {
        $scope.clearFilter(false);
    }
    $location.getReleases().then(function (result) {$scope.ReleaseList = result; console.log("result releaseList"); console.log(result); });
    console.log("workFilter");
    console.log($scope.Filt);
    $scope.loadWork();
})