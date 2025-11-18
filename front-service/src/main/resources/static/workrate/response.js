angular.module('workTimeService').controller('responseController', function ($scope, $http, $location) {

    const constPatchWorkRate = window.location.origin + '/rate-service/v1';
    document.getElementById("FormResponse").style.display = "none";
    let showResponseEdit = function () {
        document.getElementById("FormResponse").style.display = "block";
    };
    let WorkId;
    let RequestId;
    let create = function () {
        $scope.Response = {
            id: null,
            workId: WorkId,
            requestId : RequestId,
            timestamp: null,
            comment: "",
            term: null,
            status: ""
        }
        showResponseEdit();
    }
    let edit = function (responseId) {
        $http.get(constPatchWorkRate + "/agreement/response" + responseId)
            .then(function (response) {
                // WorkTimeIdEdit = response.data.id;
                $scope.Response = response.data;
                console.log($scope.Response);

                showResponseEdit();

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                }
            });
    };
    let loadStatus = function () {
        $http.get(constPatchWorkRate + "/agreement/response/status")
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
        console.log($scope.Response);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchWorkRate + "/agreement/response", $scope.Response)
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
    RequestId = $scope.Filt.requestId;
    console.log("response_load")
    console.log($scope.Filt);

    console.log(WorkId === undefined);
    console.log($scope.Filt.workId === null);
    if (WorkId === undefined)
    {
        $scope.Cancel();
        return;
    }
    if (RequestId === undefined)
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