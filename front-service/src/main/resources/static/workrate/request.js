angular.module('workTimeService').controller('requestController', function ($scope, $http, $location) {

    const constPatchWorkRate = window.location.origin + '/rate-service/v1';
    document.getElementById("FormRequest").style.display = "none";
    let showRequestEdit = function () {
        document.getElementById("FormRequest").style.display = "block";
    };
    let WorkId;

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

    console.log("Start workRate");
    $location.getUsers().then(function (result) {
        $scope.UserList = result;
        console.log("result UserList");
        console.log(result);
    });
    loadStatus();
    $scope.Filt = {}
    $location.parserFilter($scope.Filt);
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


})