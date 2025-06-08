angular.module('workTimeService').controller('roleRightController', function ($scope, $http, $location) {

    const constPatchUser = window.location.origin + '/admin/roles/role/rights';
    $scope.Role = {rights:null}
    $scope.Role = null;
    $scope.loadRight = function () {
        console.log("запрос данных");
        console.log(window.location);
        let paramsStr = new URLSearchParams(location.href.substring(location.href.indexOf("?")));
        let roleId =paramsStr.get('roleId');
        console.log(roleId)

        $http({
            url: constPatchUser + "/" + roleId ,
            method: "get"
        }).then(function (response) {
            console.log("response :");
            console.log(response);

            $scope.Role = response.data;
       }, function errorCallback(response) {
            console.log(response);
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
            $http.post(constPatchUser , $scope.Role)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendSave = false;
                    $scope.backRole();
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log(response.data);
                    if ($location.checkAuthorized(response)) {

                        alert(response.data.message);
                    }
                });
        }
    }
    $scope.backRole = function (){
        $location.path('/role' );

    }
    console.log("Start");

    $scope.loadRight();
})