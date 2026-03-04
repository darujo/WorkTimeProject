angular.module('workTimeService').controller('rateController', function ($scope, $http, $location) {

    const constPatchWorkRate = window.location.origin + '/rate-service/v1';
    const constPatchWork = window.location.origin + '/work-service/v1';
    $scope.work = {
        roleStr: null,
        stage0Fact: 0,
        stage1Fact: 0,
        stage2Fact: 0,
        stage3Fact: 0,
        stage4Fact: 0,
        stage5Fact: 0,
        stageAll: 0,
        stageAllFact: 0,
        criteriaStr: null
    }
    $scope.Filter = {viewFact: false};

    let WorkId;

    let loadWork = function () {
        console.log("loadWork");
        $http({
            url: constPatchWork + "/works/obj/little/" + WorkId,
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
    $scope.getStyle = function (flag) {
        if (flag) {
            return {
                'background-color': 'red',
                'color': 'white'
            };
        } else {
            return {};
        }
    };

    let loadRate = function () {
        console.log("loadWorkStage");
        if ($scope.load1) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.loadRateWait = true;
            $scope.WorkStageList = null;
            $http({
                url: constPatchWorkRate + "/rate",
                method: "get",
                params: {
                    workId: WorkId,
                    loadFact: true

                }
            }).then(function (response) {
                $scope.loadRateWait = false;
                console.log(response.data);
                $scope.WorkRate = response.data;
            }, function errorCallback(response) {
                $scope.loadRateWait = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                }
            });
        }
    };

    $scope.workPage = function () {
        $location.path('/work').search({});
    }

    $scope.Filt = {}
    $location.parserFilter($scope.Filt);
    WorkId = $scope.Filt.workId;

    if (WorkId === undefined) {
        $scope.workPage();
        return;
    }


    $scope.getStyleText = function (code) {
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
    loadWork();
    loadRate();
})