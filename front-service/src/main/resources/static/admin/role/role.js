 angular.module('workTimeService').controller('roleController', function ($scope, $http, $location) {

    const constPatchUser = window.location.origin + '/roles';
    const constPatchAdmin = window.location.origin + '/admin/roles';

    let showRoles = function () {
        document.getElementById("RoleList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    let showFormEdit = function () {
        document.getElementById("RoleList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
    };

    $scope.loadRole = function () {
        showRoles();
        $scope.findPage(0);
    };


    $scope.findPage = function () {
        console.log("findPage");
        console.log("запрос данных");

        let Filter;
        Filter = $scope.Filt;
        console.log(Filter);
        $http({
            url: constPatchUser ,
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
            $scope.RoleList = response.data;
            showRoles();

        }, function errorCallback(response) {
            console.log(response)
            if ($location.checkAuthorized(response)) {
                //     alert(response.data.message);
            }

        });

    };
    $scope.filterRole = function () {
        console.log("filterRole")
        $location.saveFilter("roleFilter", $scope.Filt);
        $scope.findPage();
    };

    $scope.createRole = function () {
        console.log("createRole");

        $scope.Role = {
            id: null,
            code: null,
            name: null
        };

        console.log($scope.Role);
        showFormEdit();

    };

    $scope.editRole = function (roleId) {
        console.log("edit");
        $http.get(constPatchAdmin + "/edit/role/" + roleId)
            .then(function (response) {
                $scope.Role = response.data;
                console.log($scope.Role);

                showFormEdit();

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };

    $scope.deleteRole = function (roleId) {
        $http.delete(constPatchAdmin + "/edit/role/" + roleId)
            .then(function (response) {
                console.log("Delete response")
                console.log(response);
                $scope.loadRole();
            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };
    let sendSave = false;
    $scope.saveRole = function () {
        console.log("saveRole");
        console.log($scope.Role);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchAdmin + "/edit/role", $scope.Role)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendSave = false;
                    showRoles();
                    $scope.loadRole();
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log(response.data);
                    if ($location.checkAuthorized(response)) {

                        alert(response.data.message);
                    }
                });
        }
    }

    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        $scope.Filt = {
        };
        console.log($scope.Filt);
        if (load) {
            $scope.filterRole();
        }
    }
    $scope.editRight = function (roleId){
        $location.path('/role_right' ).search({roleId: roleId});

    }

    $scope.Filt = $location.getFilter("roleFilter");
    if ($scope.Filt === null) {
        $scope.clearFilter(false);
    }
    console.log("Start");
    showRoles();
    console.log("Show ok");

    $scope.loadRole();
})