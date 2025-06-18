angular.module('workTimeService').controller('workRateController', function ($scope, $http, $location) {

    const constPatchWorkRate = window.location.origin + '/rate-service/v1';
    const constPatchRight = window.location.origin + '/work-service/v1';
    const constPatchWork = window.location.origin + '/work-service/v1';
    $scope.work = {
        roleStr: null,
        stage0Fact: null,
        stageAll: null,
        criteriaStr: null
    }
    $scope.showWorkStageAdd = function () {
        document.getElementById("WorkStageAdd").style.display = "block";
        document.getElementById("FormWorkStage").style.display = "none";
        console.log("stageCreate");
        console.log($scope.stageCreate);

    };
    let showWorkStageEdit = function () {
        document.getElementById("WorkStageAdd").style.display = "none";
        document.getElementById("FormWorkStage").style.display = "block";
    };
    let WorkId;
    $scope.loadWork = function () {
        console.log("loadWork");
        $http({
            url: constPatchWork + "/works//obj/little/" + WorkId,
            method: "get"
        }).then(function (response) {
            console.log(response.data);
            $scope.ZI = response.data;
        }, function errorCallback(response) {
            console.log(response)
            if ($location.checkAuthorized(response)) {
            }
        });
    };

    $scope.loadWorkStage = function () {
        console.log("loadWorkStage");
        if ($scope.load1) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load1 = true;
            $scope.WorkStageList = null;
            $http({
                url: constPatchWorkRate + "/stage",
                method: "get",
                params: {
                    workId: WorkId,
                    loadFact: true

                }
            }).then(function (response) {
                $scope.load1 = false;
                console.log(response.data);
                $scope.WorkStageList = response.data;
            }, function errorCallback(response) {
                $scope.load1 = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                }
            });
        }
    };

    $scope.createWorkStage = function () {
        $scope.WorkStage = {
            workId: WorkId,
            nikName: "",
            role: "",
            stage0: "",
            stage1: "",
            stage2: "",
            stage3: "",
            stage4: ""
        }
        showWorkStageEdit();
    }
    $scope.editWorkStage = function (workStageId) {
        $http.get(constPatchWorkRate + "/stage/" + workStageId)
            .then(function (response) {
                // WorkTimeIdEdit = response.data.id;
                $scope.WorkStage = response.data;
                console.log($scope.WorkStage);

                showWorkStageEdit();

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                }
            });
    }
    $scope.deleteWorkStage = function (workStageId) {
        $http.delete(constPatchWorkRate + "/stage/" + workStageId)
            .then(function (response) {
                // WorkTimeIdEdit = response.data.id;
                $scope.WorkStage = response.data;
                console.log($scope.WorkStage);

                $scope.showWorkStageAdd();
                $scope.loadWorkStage();

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                }
            });
    }
    let sendSave = false;
    $scope.saveWorkStage = function () {
        console.log()
        console.log($scope.WorkStage);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchWorkRate + "/stage", $scope.WorkStage)
                .then(function (response) {
                    sendSave = false;
                    console.log(response);
                    $scope.showWorkStageAdd();
                    $scope.loadWorkStage();
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log(response)
                    if ($location.checkAuthorized(response)) {
                        alert(response.data.message);
                    }
                });
        }
    };
    $scope.workPage = function () {
        $location.path('/work').search({});
    }
    $scope.showWorkCriteriaAdd = function () {
        document.getElementById("WorkCriteriaAdd").style.display = "block";
        document.getElementById("FormWorkCriteria").style.display = "none";
    };
    let showWorkCriteriaEdit = function () {
        document.getElementById("WorkCriteriaAdd").style.display = "none";
        document.getElementById("FormWorkCriteria").style.display = "block";
    };
    $scope.loadWorkCriteria = function () {
        console.log("loadWorkCriteria");
        if ($scope.load2) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load2 = true;
            $scope.WorkCriteriaList = null;
            $http({
                url: constPatchWorkRate + "/criteria",
                method: "get",
                params: {
                    workId: WorkId

                }
            }).then(function (response) {
                $scope.load2 = false;
                console.log(response.data);
                $scope.WorkCriteriaList = response.data;
            }, function errorCallback(response) {
                $scope.load2 = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                }
            });
        }
    };

    $scope.createWorkCriteria = function () {
        $scope.WorkCriteria = {
            workId: WorkId,
            criteria: "",
            develop10: "",
            develop50: "",
            develop100: ""
        }
        showWorkCriteriaEdit();
    }
    $scope.editWorkCriteria = function (workCriteriaId) {
        $http.get(constPatchWorkRate + "/criteria/" + workCriteriaId)
            .then(function (response) {
                // WorkTimeIdEdit = response.data.id;
                $scope.WorkCriteria = response.data;
                console.log($scope.WorkCriteria);

                showWorkCriteriaEdit();

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                }
            });
    }
    $scope.deleteWorkCriteria = function (workCriteriaId) {
        $http.delete(constPatchWorkRate + "/criteria/" + workCriteriaId)
            .then(function (response) {
                // WorkTimeIdEdit = response.data.id;
                $scope.WorkCriteria = response.data;
                console.log($scope.WorkCriteria);

                $scope.showWorkCriteriaAdd();
                $scope.loadWorkCriteria();

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                }
            });
    }
    let sendSaveCriteria = false;
    $scope.saveWorkCriteria = function () {
        console.log()
        console.log($scope.WorkCriteria);
        if (!sendSaveCriteria) {
            sendSaveCriteria = true;
            $http.post(constPatchWorkRate + "/criteria", $scope.WorkCriteria)
                .then(function (response) {
                    sendSaveCriteria = false;
                    console.log(response);
                    $scope.showWorkCriteriaAdd();
                    $scope.loadWorkCriteria();
                }, function errorCallback(response) {
                    sendSaveCriteria = false;
                    console.log(response)
                    if ($location.checkAuthorized(response)) {
                        alert(response.data.message);
                    }
                });
        }
    };
    let paramsStr = new URLSearchParams(location.href.substring(location.href.indexOf("?")));
    WorkId = paramsStr.get('workId');
    $scope.stageCreate = false;
    $scope.stageEdit = false;
    $scope.criteriaCreate = false;
    $scope.criteriaEdit = false;
    let checkRight = function (right, message, callBack) {
        // document.getElementById("ButtonSaveUp").style.display = "none";
        $scope.Resp = {message: null}
        $http({
            url: constPatchRight + "/works/right/" + right,
            method: "get"
        }).then(function (response) {
                console.log(response)
                callBack();

            }, function errorCallback(response) {
                console.log("Check");
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    if (message) {
                        alert(response.data.message);
                    } else {
                        $scope.Resp = {message: response.data.message};
                    }
                }
            }
        )
    }

    let callBackStageCreate = function () {
        $scope.stageCreate = true;
    }
    let callBackStageEdit = function () {
        $scope.stageEdit = true;
    }
    let callBackCriteriaCreate = function () {
        $scope.criteriaCreate = true;
    }
    let callBackCriteriaEdit = function () {
        $scope.criteriaEdit = true;
    }
    checkRight("stageCreate", false, callBackStageCreate);
    checkRight("stageEdit", false, callBackStageEdit);
    checkRight("criteriaCreate", false, callBackCriteriaCreate);
    checkRight("criteriaEdit", false, callBackCriteriaEdit);
    $scope.loadRateStatus = function () {
        console.log("loadRateStatus");
        $http({
            url: constPatchWorkRate + "/rate/compare/sc",
            method: "get",
            params: {
                workId: WorkId

            }
        }).then(function (response) {
            console.log(response.data);
            $scope.RateStatus = response.data;
        }, function errorCallback(response) {
            console.log(response)
            if ($location.checkAuthorized(response)) {
            }
        });
    };
    $scope.getStyle = function (code) {
        let codeInt = parseInt(code);
        if (codeInt !== 0) {
            return {
                'background-color': 'red',
                'color': 'white'
            };
        } else {
            return {};
        }

    };
    console.log("Start workRate");
    $location.getUsers().then(function (result) {
        $scope.UserList = result;
        console.log("result UserList");
        console.log(result);
    });
    $scope.showWorkStageAdd();
    $scope.showWorkCriteriaAdd();
    $scope.loadWork();
    $scope.loadWorkCriteria();
    $scope.loadWorkStage();
    $scope.loadRateStatus();
})