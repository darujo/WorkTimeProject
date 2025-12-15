// var app = angular.module('workTimeService', ['dnd']);
//
// app.
angular.module('workTimeService').controller('workStageController', function ($scope, $http, $location) {
    // console.log(window.location)
    const constPatchReleaseStage = window.location.origin + '/work-service/v1/release/stage';
    // let showWork = function () {
    //     document.getElementById("WorkList").style.display = "block";
    //     document.getElementById("FormEdit").style.display = "none";
    // };
    // let showFormEdit = function () {
    //     console.log("showFormEdit");
    //     console.log($scope.Work)
    //
    //     checkRight("Edit", false);
    //     console.log($scope.Work)
    //
    //     document.getElementById("WorkList").style.display = "none";
    //     document.getElementById("FormEdit").style.display = "block";
    //     console.log($scope.Work)
    //
    // };
    //
    // let checkRight = function (right, message) {
    //     document.getElementById("ButtonSaveDown").style.display = "none";
    //     document.getElementById("ButtonSaveUp").style.display = "none";
    //     let flag;
    //     $scope.Resp = {message: null}
    //     $http({
    //         url: constPatchWork + "/works/right/" + right,
    //         method: "get"
    //     }).then(function (response) {
    //             console.log(response)
    //             document.getElementById("ButtonSaveDown").style.display = "inline-block";
    //             document.getElementById("ButtonSaveUp").style.display = "inline-block";
    //             if (right === "create") {
    //                 $scope.createWorkRun();
    //             }
    //         }, function errorCallback(response) {
    //             console.log("Check");
    //             console.log(response)
    //             flag = false;
    //             if ($location.checkAuthorized(response)) {
    //                 if (message) {
    //                     alert(response.data.message);
    //                 } else {
    //                     $scope.Resp = {message: response.data.message};
    //                 }
    //
    //             }
    //         }
    //     )
    //     return false;
    // }
    //
    //
    $scope.loadRelease = function () {
        $location.parserFilter($scope.Filt);
        $scope.findPage(0);
    };
    // $scope.sendFilter = function () {
    //     $location.sendFilter(location.hash, $scope.Filt);
    // }
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
            $scope.releaseList = null;
            console.log("отправляем запрос /releaseList");
            console.log($scope.Filt);
            if (typeof $scope.Filt === "undefined") {
                $scope.Filt = {size: 10};
            }
            let Filter = $scope.Filt;
            console.log($scope.Filt)
            $http({
                url: constPatchReleaseStage,
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
                $scope.releaseList = response.data;
                console.log($scope.WorkList);
                maxPage = response.data["totalPages"];
                $scope.load = false;
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
    //
    // $scope.workSort = function (sort) {
    //     $scope.Filt = {
    //         sort: sort,
    //         size: $scope.Filt ? $scope.Filt.size : null,
    //         name: $scope.Filt ? $scope.Filt.name : null,
    //         codeSap: $scope.Filt ? $scope.Filt.codeSap : null,
    //         codeZi: $scope.Filt ? $scope.Filt.codeZi : null,
    //         task: $scope.Filt ? $scope.Filt.task : null,
    //         releaseId: $scope.Filt ? $scope.Filt.releaseId : null,
    //         stageZi: $scope.Filt ? $scope.Filt.stageZi : null
    //     };
    //     console.log("sort");
    //     console.log(sort);
    //     $scope.filterWork();
    // }
    // $scope.filterWork = function () {
    //     $location.saveFilter("workFilter", $scope.Filt);
    //     document.getElementById("Page").value = "1";
    //     $scope.findPage(0);
    // };
    //
    // let WorkIdEdit = null;
    //
    // $scope.createWork = function () {
    //     $scope.Work = {
    //         id: null,
    //         codeZI: "",
    //         codeSap: "",
    //         WorkName: "",
    //         developEndFact: null,
    //         issuePrototypeFact: null,
    //         debugEndFact: null,
    //         releaseEndFact: null,
    //         opeEndFact: null,
    //         analiseEndFact: null,
    //         issuePrototypePlan: null,
    //         developEndPlan: null,
    //         debugEndPlan: null,
    //         releaseEndPlan: null,
    //         opeEndPlan: null,
    //         analiseEndPlan: null,
    //         task: "",
    //         description: "",
    //         startTaskPlan: null,
    //         startTaskFact: null,
    //         laborDevelop: null,
    //         laborDebug: null,
    //         laborRelease: null,
    //         laborOPE: null,
    //         stageZI: 0,
    //         release: 0,
    //         issuingReleasePlan: null,
    //         issuingReleaseFact: null
    //
    //     };
    //     checkRight("create", true);
    // }
    // $scope.createWorkRun = function () {
    //     WorkIdEdit = null;
    //     console.log("сбрасываем значения");
    //
    //     console.log("сбрасываем значения 4")
    //     showFormEdit();
    //
    // };
    //
    // $scope.editWork = function (workId) {
    //     $http.get(constPatchWork + "/works/" + workId)
    //         .then(function (response) {
    //             console.log("получили");
    //             // showFormEdit();
    //             WorkIdEdit = response.data.id;
    //             $scope.Work = response.data;
    //             console.log($scope.Work);
    //
    //             $scope.Work.developStartFact = typeof response.data.developStartFact === "undefined" ? null : new Date(response.data.developStartFact);
    //             $scope.Work.debugStartFact = typeof response.data.debugStartFact === "undefined" ? null : new Date(response.data.debugStartFact);
    //             $scope.Work.releaseStartFact = typeof response.data.releaseStartFact === "undefined" ? null : new Date(response.data.releaseStartFact);
    //             $scope.Work.opeStartFact = typeof response.data.opeStartFact === "undefined" ? null : new Date(response.data.opeStartFact);
    //             $scope.Work.analiseStartFact = typeof response.data.analiseStartFact === "undefined" ? null : new Date(response.data.analiseStartFact);
    //             $scope.Work.developStartPlan = typeof response.data.developStartPlan === "undefined" ? null : new Date(response.data.developStartPlan);
    //             $scope.Work.debugStartPlan = typeof response.data.debugStartPlan === "undefined" ? null : new Date(response.data.debugStartPlan);
    //             $scope.Work.releaseStartPlan = typeof response.data.releaseStartPlan === "undefined" ? null : new Date(response.data.releaseStartPlan);
    //             $scope.Work.opeStartPlan = typeof response.data.opeStartPlan === "undefined" ? null : new Date(response.data.opeStartPlan);
    //             $scope.Work.analiseStartPlan = typeof response.data.analiseStartPlan === "undefined" ? null : new Date(response.data.analiseStartPlan);
    //
    //
    //
    //             $scope.Work.developEndFact = typeof response.data.developEndFact === "undefined" ? null : new Date(response.data.developEndFact);
    //             $scope.Work.issuePrototypeFact = typeof response.data.issuePrototypeFact === "undefined" ? null : new Date(response.data.issuePrototypeFact);
    //             $scope.Work.debugEndFact = typeof response.data.debugEndFact === "undefined" ? null : new Date(response.data.debugEndFact);
    //             $scope.Work.releaseEndFact = typeof response.data.releaseEndFact === "undefined" ? null : new Date(response.data.releaseEndFact);
    //             $scope.Work.opeEndFact = typeof response.data.opeEndFact === "undefined" ? null : new Date(response.data.opeEndFact);
    //             $scope.Work.analiseEndFact = typeof response.data.analiseEndFact === "undefined" ? null : new Date(response.data.analiseEndFact);
    //             $scope.Work.developEndPlan = typeof response.data.developEndPlan === "undefined" ? null : new Date(response.data.developEndPlan);
    //             $scope.Work.issuePrototypePlan = typeof response.data.issuePrototypePlan === "undefined" ? null : new Date(response.data.issuePrototypePlan);
    //             $scope.Work.debugEndPlan = typeof response.data.debugEndPlan === "undefined" ? null : new Date(response.data.debugEndPlan);
    //             $scope.Work.releaseEndPlan = typeof response.data.releaseEndPlan === "undefined" ? null : new Date(response.data.releaseEndPlan);
    //             $scope.Work.opeEndPlan = typeof response.data.opeEndPlan === "undefined" ? null : new Date(response.data.opeEndPlan);
    //             $scope.Work.analiseEndPlan = typeof response.data.analiseEndPlan === "undefined" ? null : new Date(response.data.analiseEndPlan);
    //
    //             $scope.Work.startTaskPlan = typeof response.data.startTaskPlan === "undefined" ? null : new Date(response.data.startTaskPlan);
    //             // Дата начала доработки Факт
    //             $scope.Work.startTaskFact = typeof response.data.startTaskFact === "undefined" ? null : new Date(response.data.startTaskFact);
    //             // Выдача релиза даты План
    //             console.log(typeof response.data.issuingReleasePlan === "undefined")
    //             $scope.Work.issuingReleasePlan = typeof response.data.issuingReleasePlan === "undefined" ? null : new Date(response.data.issuingReleasePlan);
    //             // Выдача релиза дата факт
    //             if (typeof response.data.issuingReleaseFact !== "undefined") {
    //                 $scope.Work.issuingReleaseFact = new Date(response.data.issuingReleaseFact);
    //             }
    //             console.log($scope.Work)
    //             showFormEdit();
    //         });
    // };
    // $scope.deleteWork = function (workId) {
    //     $http.delete(constPatchWork + "/works/" + workId)
    //         .then(function (response) {
    //             console.log(response);
    //             $scope.loadWork();
    //         }, function errorCallback(response) {
    //             console.log(response)
    //             if ($location.checkAuthorized(response)) {
    //                 //     alert(response.data.message);
    //             }
    //
    //             // showFindTask();
    //         });
    // };
    let sendSave = false;
    let saveWork = async function (workId, releaseId, stageZi) {
        console.log()
        console.log($scope.Work);
        let flag;
        if (!sendSave) {
            sendSave = true;
            $http({
                url: constPatchReleaseStage + "/save",
                method: "get",
                params: {
                    workId: workId,
                    releaseId: releaseId,
                    stageZi: stageZi

                }
            }).then(function (response) {
                sendSave = false;
                console.log(response);
                flag = true;
            }, function errorCallback(response) {
                sendSave = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    alert(response.data.message);
                }
                flag = false;
            });
        }
        while (sendSave) {
            await wait();
        }
        return flag;
    };

    function wait() {
        return new Promise((resolve, reject) => {
            console.log(reject);
            setTimeout(() => {
                resolve('Timeout resolved');
            }, 10);
        });
    }

    //
    // $scope.addTime = function (workId) {
    //     $location.path('/task').search({workId: workId});
    //     // window.open('#!/task',"_parent");
    // }
    // $scope.addRate = function (workId) {
    //     console.log("Другая");
    //     $location.path('/rate').search({workId: workId});
    // }
    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        $scope.Filt = {
            stageZi: 15,
            size: 10
        }
        console.log($scope.Filt);
        if (load) {
            $scope.filterWork();
        }
    }
    // $scope.releaseOption = function (release) {
    //     // console.log(release)
    //     // console.log(typeof release !== "undefined" && release.issuingReleaseFact !== "undefined" && release.issuingReleaseFact !== null)
    //     if (typeof release !== "undefined" && typeof release.issuingReleaseFact !== "undefined" && release.issuingReleaseFact !== null) {
    //         return "disabled";
    //     }
    //     return false;
    // }
    // showWork();
    // console.log("---workFilter---");
    // $scope.Filt = $location.getFilter("workFilter");
    // console.log($scope.Filt);
    // if ($scope.Filt === null) {
    //     $scope.clearFilter(false);
    // }

    $location.getReleases().then(function (result) {
        $scope.ReleaseList = result;
        console.log("result releaseList");
        console.log(result);
    });
    // console.log("workFilter");
    // console.log($scope.Filt);
    // $scope.loadWork();
    // let zzz ={};
    $scope.onSortStart = function (a) {
        console.log('sort start');
        console.log(a)
        // console.log('sort start');
    }

    $scope.onSort = function (relId, stage) {
        console.log('sort');
        console.log(relId);
        console.log(stage);
    }
    /*$scope.dragover = function () {
        console.log('dragover', arguments);
    };*/
    $scope.onSortChange = function (relId, stage, a) {


        // console.log(zzz)
        // a["id"] =relId;
        // a["stage"]= stage;
        console.log('onSortChange');
        console.log(relId);
        console.log(stage);
    }


    $scope.onSortEnd = function (relId, stage, work) {
        console.log("onSortEnd res")
        console.log(relId);
        console.log(stage);

        console.log(work);
        saveWork(work.id, relId, stage).then(function (result) {
            console.log("onSortEnd result");
            console.log(result)
        });


        console.log('sort end');
        return false;
    }
    $scope.onSortEnter = function () {
        console.log('onSortEnter');
    }
    // $scope.list = [];
    // $scope.releaseList = [];

    // $scope.releaseList[1] ={list:[],list2:[]}
    // for(let i = 0; i < 5; i++){
    //     $scope.list[i] = {
    //         id: i,
    //         rel: 5,
    //         stage: 1,
    //         name: 'item1-'+(i),
    //         sortable: true
    //     };
    // }
    // $scope.releaseList[0] ={rel:6, list: $scope.list,list2:[]}
    // console.log($scope.releaseList);
    // console.log($scope.list);
    $scope.loadRelease();
})