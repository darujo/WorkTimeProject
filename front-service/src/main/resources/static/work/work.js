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
    $scope.rightObj ={Edit:false,Create:false};

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
                $scope.rightObj[right] = true;
                if (right === "create") {
                    $scope.createWorkRun();
                }
            }, function errorCallback(response) {
                console.log("Check");
                console.log(response)
                $scope.rightObj[right] = false;
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
        $location.sendFilter(location.hash, $scope.Filt);
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
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load = true;
            $scope.WorkList = null;
            console.log("отправляем запрос /works");
            console.log($scope.Filt);
            if (typeof $scope.Filt === "undefined") {
                $scope.Filt = {size: 10};
            }
            let Filter = $scope.Filt;
            console.log($scope.Filt)
            $http({
                url: constPatchWork + "/works",
                method: "get",
                params: {
                    page: page,
                    size: Filter ? Filter.size : null,
                    name: Filter ? Filter.name : null,
                    codeSap: Filter ? Filter.codeSap : null,
                    codeZi: Filter ? Filter.codeZi : null,
                    task: Filter ? Filter.task : null,
                    releaseId: Filter ? Filter.releaseId : null,
                    sort: Filter ? Filter.sort : null,
                    stageZi: Filter ? Filter.stageZi : null
                }
            }).then(function (response) {
                console.log(response);
                $scope.WorkList = response.data._embedded.workDtoList;
                console.log($scope.WorkList);
                maxPage = response.data["totalPages"];
                $scope.load = false;
                showWork();
            }, function errorCallback(response) {
                maxPage = 1;
                console.log(response)
                $scope.load = false;
                if ($location.checkAuthorized(response)) {
                    alert(response.data.message);
                }

                // showFindTask();
            });
        }
    };

    $scope.workSort = function (sort) {
        $scope.Filt = {
            sort: sort,
            size: $scope.Filt ? $scope.Filt.size : null,
            name: $scope.Filt ? $scope.Filt.name : null,
            codeSap: $scope.Filt ? $scope.Filt.codeSap : null,
            codeZi: $scope.Filt ? $scope.Filt.codeZi : null,
            task: $scope.Filt ? $scope.Filt.task : null,
            releaseId: $scope.Filt ? $scope.Filt.releaseId : null,
            stageZi: $scope.Filt ? $scope.Filt.stageZi : null
        };
        console.log("sort");
        console.log(sort);
        $scope.filterWork();
    }
    $scope.filterWork = function () {
        $location.saveFilter("workFilter", $scope.Filt);
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
            issuePrototypeFact: null,
            debugEndFact: null,
            releaseEndFact: null,
            opeEndFact: null,
            analiseEndFact: null,
            issuePrototypePlan: null,
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
            issuingReleaseFact: null,
            rated: null

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

                $scope.Work.developStartFact = typeof response.data.developStartFact === "undefined" ? null : new Date(response.data.developStartFact);
                $scope.Work.debugStartFact = typeof response.data.debugStartFact === "undefined" ? null : new Date(response.data.debugStartFact);
                $scope.Work.releaseStartFact = typeof response.data.releaseStartFact === "undefined" ? null : new Date(response.data.releaseStartFact);
                $scope.Work.opeStartFact = typeof response.data.opeStartFact === "undefined" ? null : new Date(response.data.opeStartFact);
                $scope.Work.analiseStartFact = typeof response.data.analiseStartFact === "undefined" ? null : new Date(response.data.analiseStartFact);
                $scope.Work.developStartPlan = typeof response.data.developStartPlan === "undefined" ? null : new Date(response.data.developStartPlan);
                $scope.Work.debugStartPlan = typeof response.data.debugStartPlan === "undefined" ? null : new Date(response.data.debugStartPlan);
                $scope.Work.releaseStartPlan = typeof response.data.releaseStartPlan === "undefined" ? null : new Date(response.data.releaseStartPlan);
                $scope.Work.opeStartPlan = typeof response.data.opeStartPlan === "undefined" ? null : new Date(response.data.opeStartPlan);
                $scope.Work.analiseStartPlan = typeof response.data.analiseStartPlan === "undefined" ? null : new Date(response.data.analiseStartPlan);


                $scope.Work.developEndFact = typeof response.data.developEndFact === "undefined" ? null : new Date(response.data.developEndFact);
                $scope.Work.issuePrototypeFact = typeof response.data.issuePrototypeFact === "undefined" ? null : new Date(response.data.issuePrototypeFact);
                $scope.Work.debugEndFact = typeof response.data.debugEndFact === "undefined" ? null : new Date(response.data.debugEndFact);
                $scope.Work.releaseEndFact = typeof response.data.releaseEndFact === "undefined" ? null : new Date(response.data.releaseEndFact);
                $scope.Work.opeEndFact = typeof response.data.opeEndFact === "undefined" ? null : new Date(response.data.opeEndFact);
                $scope.Work.analiseEndFact = typeof response.data.analiseEndFact === "undefined" ? null : new Date(response.data.analiseEndFact);
                $scope.Work.developEndPlan = typeof response.data.developEndPlan === "undefined" ? null : new Date(response.data.developEndPlan);
                $scope.Work.issuePrototypePlan = typeof response.data.issuePrototypePlan === "undefined" ? null : new Date(response.data.issuePrototypePlan);
                $scope.Work.debugEndPlan = typeof response.data.debugEndPlan === "undefined" ? null : new Date(response.data.debugEndPlan);
                $scope.Work.releaseEndPlan = typeof response.data.releaseEndPlan === "undefined" ? null : new Date(response.data.releaseEndPlan);
                $scope.Work.opeEndPlan = typeof response.data.opeEndPlan === "undefined" ? null : new Date(response.data.opeEndPlan);
                $scope.Work.analiseEndPlan = typeof response.data.analiseEndPlan === "undefined" ? null : new Date(response.data.analiseEndPlan);

                $scope.Work.startTaskPlan = typeof response.data.startTaskPlan === "undefined" ? null : new Date(response.data.startTaskPlan);
                // Дата начала доработки Факт
                $scope.Work.startTaskFact = typeof response.data.startTaskFact === "undefined" ? null : new Date(response.data.startTaskFact);
                // Выдача релиза даты План
                console.log(typeof response.data.issuingReleasePlan === "undefined")
                $scope.Work.issuingReleasePlan = typeof response.data.issuingReleasePlan === "undefined" ? null : new Date(response.data.issuingReleasePlan);
                // Выдача релиза дата факт
                if (typeof response.data.issuingReleaseFact !== "undefined") {
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
        $location.path('/task').search({workId: workId});
        // window.open('#!/task',"_parent");
    }
    $scope.addRate = function (workId) {
        $location.path('/work_rate').search({workId: workId, view: "current"});
    }

    $scope.addAgreement = function (workId) {
        $location.path('/agreement').search({workId: workId});
    }
    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        if ($scope.Filt === null || load) {
            $scope.Filt = {
                stageZi: 15,
                size: 10
            }
        } else {
            $scope.Filt["stageZi"] = $scope.Filt.stageZi ? $scope.Filt.stageZi : 15;
            $scope.Filt["size"] = $scope.Filt.size ? $scope.Filt.size : 10;
        }

        console.log($scope.Filt);
        if (load) {
            $scope.filterWork();
        }
    }
    $scope.releaseOption = function (release) {
        // console.log(release)
        // console.log(typeof release !== "undefined" && release.issuingReleaseFact !== "undefined" && release.issuingReleaseFact !== null)
        if (typeof release !== "undefined" && typeof release.issuingReleaseFact !== "undefined" && release.issuingReleaseFact !== null) {
            return "disabled";
        }
        return false;
    }
    showWork();
    console.log("---workFilter---");
    $scope.Filt = $location.getFilter("workFilter");
    console.log($scope.Filt);

    $scope.clearFilter(false);

    $location.getReleases().then(function (result) {
        $scope.ReleaseList = result;
        console.log("result releaseList");
        console.log(result);
    });

    $location.getProjects().then(function (result) {
        $scope.ProjectList = result;
        console.log("result ProjectList");
        console.log(result);
    });

    checkRight("Edit", false);

    console.log("workFilter");
    console.log($scope.Filt);
    $scope.loadWork();
})