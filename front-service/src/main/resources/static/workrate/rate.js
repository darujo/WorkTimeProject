angular.module('workTimeService').controller('rateController', function ($scope, $http, $location) {

    const constPatchWorkRate = window.location.origin + '/rate-service/v1';
    const constPatchWork = window.location.origin + '/work-service/v1';
    $scope.work = {
        roleStr: null,
        stage0Fact: null,
        stage1Fact: null,
        stage2Fact: null,
        stage3Fact: null,
        stage4Fact: null,
        stage5Fact: null,
        stageAll: null,
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
    loadWork();
    loadRate();
})