angular.module('workTimeService').controller('roleController', function ($scope, $http, $location) {

    const constPatchAdmin = window.location.origin + '/admin/project';

    $scope.cancel = function () {
        $location.path('/project');
    };


    $scope.create = function () {
        console.log("createRole");
        $scope.Role = {
            id: null,
            code: null,
            name: null
        };
        console.log($scope.Role);
    };

    $scope.edit = function (projectId) {
        console.log("edit");
        $http.get(constPatchAdmin + "/" + projectId)
            .then(function (response) {
                $scope.Project = response.data;
                console.log($scope.Project);

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };

    let sendSave = false;
    $scope.saveProject = function () {
        console.log("saveRole");
        console.log($scope.Project);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchAdmin, $scope.Project)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendSave = false;
                    $scope.cancel();
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log(response.data);
                    if ($location.checkAuthorized(response)) {

                        alert(response.data.message);
                    }
                });
        }
    }
    console.log("Show ok");

    $scope.loadRole();
})