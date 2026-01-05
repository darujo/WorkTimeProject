angular.module('workTimeService').controller('projectController', function ($scope, $http, $location) {

    const constPatchProject = window.location.origin + '/project';
    // const constPatchAdmin = window.location.origin + '/admin/roles';

    $scope.loadProject = function () {
        $scope.findPage(0);
    };


    $scope.findPage = function () {
        console.log("findPage");
        console.log("запрос данных проектов");
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load = true;
            $scope.ProjectList = null;
            let Filter;
            Filter = $scope.Filt;
            console.log(Filter);
            $http({
                url: constPatchProject,
                method: "get",
                params: {
                    code: Filter ? Filter.code : null,
                    name: Filter ? Filter.name : null

                }


            }).then(function (response) {
                console.log("response :");
                console.log(response);
                console.log("response,data :");
                console.log(response.data);
                $scope.ProjectList = response.data;
                $scope.load = false;
            }, function errorCallback(response) {
                $scope.load = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

            });
        }
    };
    $scope.filterProject = function () {
        console.log("filterProject")
        $location.saveFilter("projectFilter", $scope.Filt);
        $scope.findPage();
    };

    $scope.createProject = function () {
        console.log("createProject");
        $location.path('/project_edit').search({projectId: null});
    };

    $scope.editRole = function (projectId) {
        console.log("edit");
        $location.path('/project_edit').search({projectId: projectId});
    };

    $scope.deleteProject = function (projectId) {
        $http.delete(constPatchAdmin + "/edit/project/" + projectId)
            .then(function (response) {
                console.log("Delete response")
                console.log(response);
                $scope.loadProject();
            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };

    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        if (!load) {
            $scope.Filt = {};
        }
        console.log($scope.Filt);
        if (load) {
            $scope.filterRole();
        }
    }

    $scope.Filt = $location.getFilter("projectFilter");

    $scope.clearFilter(false);
    console.log("Start");
    console.log("Show ok");

    $scope.loadProject();
})