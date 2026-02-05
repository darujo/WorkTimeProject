angular.module('workTimeService').controller('projectEditController', function ($scope, $http, $location) {
    const constPatchProject = window.location.origin + '/projects';
    const constPatchAdmin = window.location.origin + '/admin/projects';

    $scope.backProject = function () {
        console.log("dfsgfdgdfgdf")
        $location.path('/project');
    };


    let create = function () {
        console.log("createProject");
        $scope.Project = {
            id: null,
            code: null,
            name: null
        };
        console.log($scope.Role);
    };

    let edit = function (projectId) {
        console.log("edit");
        $http.get(constPatchProject + "/" + projectId)
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
    let Filt = {};
    $location.parserFilter(Filt);

    console.log("project_edit")
    console.log(Filt);
    if (Filt.projectId === undefined) {
        create();
    } else {
        edit(Filt.projectId);
    }
})