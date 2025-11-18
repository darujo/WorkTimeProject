angular.module('workTimeService').controller('requestController', function ($scope, $http, $location) {

    const constPatchWorkRate = window.location.origin + '/rate-service/v1';
    // const constPatchRight = window.location.origin + '/work-service/v1';
    // const constPatchWork = window.location.origin + '/work-service/v1';
    // $scope.work = {
    //     roleStr: null,
    //     stage0Fact: null,
    //     stageAll: null,
    //     criteriaStr: null
    // }
    // $scope.showWorkStageAdd = function () {
    //     document.getElementById("WorkStageAdd").style.display = "block";
    //     document.getElementById("FormWorkStage").style.display = "none";
    //     console.log("stageCreate");
    //     console.log($scope.stageCreate);
    //
    // };
    document.getElementById("FormRequest").style.display = "none";
    let showRequestEdit = function () {
        document.getElementById("FormRequest").style.display = "block";
    };
    let WorkId;
    // $scope.loadWork = function () {
    //     console.log("loadWork");
    //     $http({
    //         url: constPatchWork + "/works/obj/little/" + WorkId,
    //         method: "get"
    //     }).then(function (response) {
    //         console.log(response.data);
    //         $scope.ZI = response.data;
    //     }, function errorCallback(response) {
    //         console.log(response)
    //         if ($location.checkAuthorized(response)) {
    //         }
    //     });
    // };
    // $scope.changeRated = function (rated) {
    //     console.log("changeRated");
    //     $scope.ZI[rated] = false;
    //     console.log(rated)
    //     $http({
    //         url: constPatchWork + "/works/change/" + WorkId + "/rated",
    //         method: "get",
    //         params: {
    //             rated: rated
    //         }
    //     }).then(function (response) {
    //         console.log(response.data);
    //         $scope.ZI = response.data;
    //     }, function errorCallback(response) {
    //         console.log(response)
    //         if ($location.checkAuthorized(response)) {
    //         }
    //     });
    // }

    // $scope.loadWorkStage = function () {
    //     console.log("loadWorkStage");
    //     if ($scope.load1) {
    //         alert("Подождите обрабатывается предыдущий запрос")
    //     } else {
    //         $scope.load1 = true;
    //         $scope.RequestList = null;
    //         $http({
    //             url: constPatchWorkRate + "/agreement/request/full",
    //             method: "get",
    //             params: {
    //                 workId: WorkId
    //
    //             }
    //         }).then(function (response) {
    //             $scope.load1 = false;
    //             console.log(response.data);
    //             $scope.RequestList = response.data;
    //         }, function errorCallback(response) {
    //             $scope.load1 = false;
    //             console.log(response)
    //             if ($location.checkAuthorized(response)) {
    //             }
    //         });
    //     }
    // };

    let create = function () {
        $scope.Request = {
            id: null,
            workId: WorkId,
            timestamp: null,
            comment: "",
            term: null,
            status: ""
        }
        showRequestEdit();
    }
    let edit = function (requestId) {
        $http.get(constPatchWorkRate + "/agreement/request" + requestId)
            .then(function (response) {
                // WorkTimeIdEdit = response.data.id;
                $scope.Request = response.data;
                console.log($scope.Request);

                showRequestEdit();

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                }
            });
    };
    let loadStatus = function () {
        $http.get(constPatchWorkRate + "/agreement/request/status")
            .then(function (response) {
                // WorkTimeIdEdit = response.data.id;
                $scope.StatusList = response.data;
                console.log($scope.StatusList);


            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                }
            });
    };
    // $scope.deleteWorkStage = function (workStageId) {
    //     $http.delete(constPatchWorkRate + "/stage/" + workStageId)
    //         .then(function (response) {
    //             // WorkTimeIdEdit = response.data.id;
    //             $scope.WorkStage = response.data;
    //             console.log($scope.WorkStage);
    //
    //             $scope.showWorkStageAdd();
    //             $scope.loadWorkStage();
    //
    //         }, function errorCallback(response) {
    //             console.log(response)
    //             if ($location.checkAuthorized(response)) {
    //             }
    //         });
    // }
    let sendSave = false;
    $scope.save = function () {
        console.log()
        console.log($scope.Request);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchWorkRate + "/agreement/request", $scope.Request)
                .then(function (response) {
                    sendSave = false;
                    console.log(response);
                    $scope.Cancel();
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log(response)
                    if ($location.checkAuthorized(response)) {
                        alert(response.data.message);
                    }
                });
        }
    };
    $scope.Cancel = function () {
        console.log("workPage")
        $location.path('/agreement').search({workId: WorkId});
    }
    // $scope.showWorkCriteriaAdd = function () {
    //     document.getElementById("WorkCriteriaAdd").style.display = "block";
    //     document.getElementById("FormWorkCriteria").style.display = "none";
    // };
    // let showWorkCriteriaEdit = function () {
    //     document.getElementById("WorkCriteriaAdd").style.display = "none";
    //     document.getElementById("FormWorkCriteria").style.display = "block";
    // };
    // $scope.loadWorkCriteria = function () {
    //     console.log("loadWorkCriteria");
    //     if ($scope.load2) {
    //         alert("Подождите обрабатывается предыдущий запрос")
    //     } else {
    //         $scope.load2 = true;
    //         $scope.WorkCriteriaList = null;
    //         $http({
    //             url: constPatchWorkRate + "/criteria",
    //             method: "get",
    //             params: {
    //                 workId: WorkId
    //
    //             }
    //         }).then(function (response) {
    //             $scope.load2 = false;
    //             console.log(response.data);
    //             $scope.WorkCriteriaList = response.data;
    //         }, function errorCallback(response) {
    //             $scope.load2 = false;
    //             console.log(response)
    //             if ($location.checkAuthorized(response)) {
    //             }
    //         });
    //     }
    // };
    //
    // $scope.createWorkCriteria = function () {
    //     $scope.WorkCriteria = {
    //         id: null,
    //         workId: WorkId,
    //         criteria: "",
    //         develop10: "",
    //         develop50: "",
    //         develop100: ""
    //     }
    //     showWorkCriteriaEdit();
    // }
    // $scope.editWorkCriteria = function (workCriteriaId) {
    //     $http.get(constPatchWorkRate + "/criteria/" + workCriteriaId)
    //         .then(function (response) {
    //             // WorkTimeIdEdit = response.data.id;
    //             $scope.WorkCriteria = response.data;
    //             console.log($scope.WorkCriteria);
    //
    //             showWorkCriteriaEdit();
    //
    //         }, function errorCallback(response) {
    //             console.log(response)
    //             if ($location.checkAuthorized(response)) {
    //             }
    //         });
    // }
    // $scope.deleteWorkCriteria = function (workCriteriaId) {
    //     $http.delete(constPatchWorkRate + "/criteria/" + workCriteriaId)
    //         .then(function (response) {
    //             // WorkTimeIdEdit = response.data.id;
    //             $scope.WorkCriteria = response.data;
    //             console.log($scope.WorkCriteria);
    //
    //             $scope.showWorkCriteriaAdd();
    //             $scope.loadWorkCriteria();
    //
    //         }, function errorCallback(response) {
    //             console.log(response)
    //             if ($location.checkAuthorized(response)) {
    //             }
    //         });
    // }
    // let sendSaveCriteria = false;
    // $scope.saveWorkCriteria = function () {
    //     console.log()
    //     console.log($scope.WorkCriteria);
    //     if (!sendSaveCriteria) {
    //         sendSaveCriteria = true;
    //         $http.post(constPatchWorkRate + "/criteria", $scope.WorkCriteria)
    //             .then(function (response) {
    //                 sendSaveCriteria = false;
    //                 console.log(response);
    //                 $scope.showWorkCriteriaAdd();
    //                 $scope.loadWorkCriteria();
    //                 loadRateStatusC();
    //             }, function errorCallback(response) {
    //                 sendSaveCriteria = false;
    //                 console.log(response)
    //                 if ($location.checkAuthorized(response)) {
    //                     alert(response.data.message);
    //                 }
    //             });
    //     }
    // };
    // ----------------------------------------------------------------------------
    // $scope.showWorkTypeAdd = function () {
    //     document.getElementById("WorkTypeAdd").style.display = "block";
    //     document.getElementById("FormWorkType").style.display = "none";
    // };
    // let showWorkTypeEdit = function () {
    //     document.getElementById("WorkTypeAdd").style.display = "none";
    //     document.getElementById("FormWorkType").style.display = "block";
    // };
    // $scope.loadWorkType = function () {
    //     console.log("loadWorkType");
    //     if ($scope.load3) {
    //         alert("Подождите обрабатывается предыдущий запрос")
    //     } else {
    //         $scope.load3 = true;
    //         $scope.WorkTypeList = null;
    //         $http({
    //             url: constPatchWorkRate + "/type",
    //             method: "get",
    //             params: {
    //                 workId: WorkId
    //
    //             }
    //         }).then(function (response) {
    //             $scope.load3 = false;
    //             console.log(response.data);
    //             $scope.WorkTypeList = response.data;
    //         }, function errorCallback(response) {
    //             $scope.load3 = false;
    //             console.log(response)
    //             if ($location.checkAuthorized(response)) {
    //             }
    //         });
    //     }
    // };
    //
    // $scope.createWorkType = function () {
    //     $scope.WorkType = {
    //         id: null,
    //         workId: WorkId,
    //         type: "",
    //         time: ""
    //     }
    //     showWorkTypeEdit();
    // }
    // $scope.editWorkType = function (workTypeId) {
    //     $http.get(constPatchWorkRate + "/type/" + workTypeId)
    //         .then(function (response) {
    //             // WorkTimeIdEdit = response.data.id;
    //             $scope.WorkType = response.data;
    //             console.log($scope.WorkType);
    //
    //             showWorkTypeEdit();
    //
    //         }, function errorCallback(response) {
    //             console.log(response)
    //             if ($location.checkAuthorized(response)) {
    //             }
    //         });
    // }
    // $scope.deleteWorkType = function (workTypeId) {
    //     $http.delete(constPatchWorkRate + "/type/" + workTypeId)
    //         .then(function (response) {
    //             // WorkTimeIdEdit = response.data.id;
    //             $scope.WorkType = response.data;
    //             console.log($scope.WorkType);
    //
    //             $scope.showWorkTypeAdd();
    //             $scope.loadWorkType();
    //
    //         }, function errorCallback(response) {
    //             console.log(response)
    //             if ($location.checkAuthorized(response)) {
    //             }
    //         });
    // }
    // let sendSaveType = false;
    // $scope.saveWorkType = function () {
    //     console.log()
    //     console.log($scope.WorkType);
    //     if (!sendSaveType) {
    //         sendSaveType = true;
    //         $http.post(constPatchWorkRate + "/type", $scope.WorkType)
    //             .then(function (response) {
    //                 sendSaveType = false;
    //                 console.log(response);
    //                 $scope.showWorkTypeAdd();
    //                 $scope.loadWorkType();
    //                 loadRateStatusT();
    //             }, function errorCallback(response) {
    //                 sendSaveType = false;
    //                 console.log(response)
    //                 if ($location.checkAuthorized(response)) {
    //                     alert(response.data.message);
    //                 }
    //             });
    //     }
    // };
// ---------------------------------------------------------------------------------
    console.log("Start workRate");
    $location.getUsers().then(function (result) {
        $scope.UserList = result;
        console.log("result UserList");
        console.log(result);
    });
    loadStatus();
    $scope.Filt = {}
    $location.parserFilter($scope.Filt);
    // let paramsStr = new URLSearchParams(location.href.substring(location.href.indexOf("?")));
    WorkId = $scope.Filt.workId;
    console.log("request_load")
    console.log($scope.Filt);

    console.log(WorkId === undefined);
    console.log($scope.Filt.workId === null);
    if (WorkId === undefined)
    {
        $scope.Cancel();
        return;
    }
    if($scope.Filt.id === undefined){
        create();
    } else {
        edit($scope.Filt.id);
    }

    // $scope.stageCreate = false;
    // $scope.stageEdit = false;
    // $scope.criteriaCreate = false;
    // $scope.criteriaEdit = false;
    // $scope.typeCreate = false;
    // $scope.typeEdit = false;
    // let checkRight = function (right, message, callBack) {
    //     // document.getElementById("ButtonSaveUp").style.display = "none";
    //     $scope.Resp = {message: null}
    //     $http({
    //         url: constPatchRight + "/works/right/" + right,
    //         method: "get"
    //     }).then(function (response) {
    //             console.log(response)
    //             callBack();
    //
    //         }, function errorCallback(response) {
    //             console.log("Check");
    //             console.log(response)
    //             if ($location.checkAuthorized(response)) {
    //                 if (message) {
    //                     alert(response.data.message);
    //                 } else {
    //                     $scope.Resp = {message: response.data.message};
    //                 }
    //             }
    //         }
    //     )
    // }
    //
    // let callBackStageCreate = function () {
    //     $scope.stageCreate = true;
    // }
    // let callBackStageEdit = function () {
    //     $scope.stageEdit = true;
    // }
    // let callBackCriteriaCreate = function () {
    //     $scope.criteriaCreate = true;
    // }
    // let callBackCriteriaEdit = function () {
    //     $scope.criteriaEdit = true;
    // }
    // let callBackTypeCreate = function () {
    //     $scope.typeCreate = true;
    // }
    // let callBackTypeEdit = function () {
    //     $scope.typeEdit = true;
    // }
    // let callBackZiEdit = function () {
    //     $scope.ziEdit = true;
    // }
    // checkRight("stageCreate", false, callBackStageCreate);
    // checkRight("stageEdit", false, callBackStageEdit);
    // checkRight("criteriaCreate", false, callBackCriteriaCreate);
    // checkRight("criteriaEdit", false, callBackCriteriaEdit);
    // checkRight("typeCreate", false, callBackTypeCreate);
    // checkRight("typeEdit", false, callBackTypeEdit);
    // checkRight("ziEdit", false, callBackZiEdit);
    // let loadRateStatus = function (){
    //     loadRateStatusST();
    //     loadRateStatusCT();
    //     loadRateStatusSC();
    // }
    // let loadRateStatusS = function (){
    //     loadRateStatusST();
    //     loadRateStatusSC();
    // }
    // let loadRateStatusC = function (){
    //     loadRateStatusCT();
    //     loadRateStatusSC();
    // }
    // let loadRateStatusT = function (){
    //     loadRateStatusST();
    //     loadRateStatusCT();
    // }
    // let loadRateStatusSC = function () {
    //     console.log("loadRateStatus1");
    //     $http({
    //         url: constPatchWorkRate + "/rate/compare/sc",
    //         method: "get",
    //         params: {
    //             workId: WorkId
    //
    //         }
    //     }).then(function (response) {
    //         console.log(response.data);
    //         $scope.RateStatusSC = response.data;
    //     }, function errorCallback(response) {
    //         console.log(response)
    //         if ($location.checkAuthorized(response)) {
    //         }
    //     });
    // };
    // let loadRateStatusST = function () {
    //     console.log("loadRateStatus2");
    //     $http({
    //         url: constPatchWorkRate + "/rate/compare/st",
    //         method: "get",
    //         params: {
    //             workId: WorkId
    //
    //         }
    //     }).then(function (response) {
    //         console.log(response.data);
    //         $scope.RateStatusST = response.data;
    //     }, function errorCallback(response) {
    //         console.log(response)
    //         if ($location.checkAuthorized(response)) {
    //         }
    //     });
    // };
    // let loadRateStatusCT = function () {
    //     console.log("loadRateStatusCT");
    //     $http({
    //         url: constPatchWorkRate + "/rate/compare/ct",
    //         method: "get",
    //         params: {
    //             workId: WorkId
    //
    //         }
    //     }).then(function (response) {
    //         console.log(response.data);
    //         $scope.RateStatusCT = response.data;
    //     }, function errorCallback(response) {
    //         console.log(response)
    //         if ($location.checkAuthorized(response)) {
    //         }
    //     });
    // };
    // $scope.addRequest = function (workId) {
    //     console.log("addRequest");
    //     $location.path('/agreement/request').search({workId: workId});
    //
    // }
    // $scope.getStyle = function (code) {
    //     let codeInt = parseInt(code);
    //     if (codeInt !== 0) {
    //         return {
    //             'background-color': 'red',
    //             'color': 'white'
    //         };
    //     } else {
    //         return {};
    //     }
    //
    // };

    // $scope.showWorkStageAdd();
    // $scope.showWorkCriteriaAdd();
    // $scope.showWorkTypeAdd();
    // $scope.loadWork();
    // $scope.loadWorkCriteria();
    // $scope.loadWorkType();
    // $scope.loadWorkStage();
    // loadRateStatus();
})