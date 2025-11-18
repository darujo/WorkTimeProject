angular.module('workTimeService').controller('agreementController', function ($scope, $http, $location) {

    const constPatchWorkRate = window.location.origin + '/rate-service/v1';
    const constPatchWork = window.location.origin + '/work-service/v1';
    $scope.work = {
        roleStr: null,
        stage0Fact: null,
        stageAll: null,
        criteriaStr: null
    }

    let WorkId;
    $scope.loadWork = function () {
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

    $scope.loadAgreement = function () {
        console.log("loadAgreement");
        if ($scope.load1) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load1 = true;
            $scope.RequestList = null;
            $http({
                url: constPatchWorkRate + "/agreement/request/full",
                method: "get",
                params: {
                    workId: WorkId

                }
            }).then(function (response) {
                $scope.load1 = false;
                console.log(response.data);
                $scope.RequestList = response.data;
            }, function errorCallback(response) {
                $scope.load1 = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                }
            });
        }
    };

    $scope.workPage = function () {
        console.log("workPage")
        $location.path('/work').search({});
    }
    $scope.Filt = {}
    $location.parserFilter($scope.Filt);
    WorkId = $scope.Filt.workId;
    console.log($scope.Filt);

    console.log(WorkId === undefined);
    console.log($scope.Filt.workId === null);
    if (WorkId === undefined)
    {
        $scope.workPage();
        return;
    }

    $scope.stageCreate = false;
    $scope.stageEdit = false;
    $scope.criteriaCreate = false;
    $scope.criteriaEdit = false;
    $scope.typeCreate = false;
    $scope.typeEdit = false;

    $scope.addRequest = function () {
        console.log("addRequest");
        $location.path('/agreement/request').search({workId: WorkId});

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
    $scope.loadWork();
    $scope.loadAgreement();

})