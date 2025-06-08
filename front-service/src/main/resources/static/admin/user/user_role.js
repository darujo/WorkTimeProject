angular.module('workTimeService').controller('userRoleController', function ($scope, $http, $location, ) {

    const constPatchUser = window.location.origin + '/admin/users/user/roles';
    $scope.User = {roles:null};
    $scope.User = null
    $scope.loadRole = function () {
        console.log("запрос данных");
        console.log(window.location);
        let paramsStr = new URLSearchParams(location.href.substring(location.href.indexOf("?")));
        let userId =paramsStr.get('userId');
        console.log(userId)

        $http({
            url: constPatchUser + "/" + userId ,
            method: "get"
        }).then(function (response) {
            console.log("response :");
            console.log(response);

            $scope.User = response.data;
       }, function errorCallback(response) {
            console.log(response);
            if ($location.checkAuthorized(response)) {
                //     alert(response.data.message);
            }

        });

    };
    let sendSave = false;
    $scope.saveUser = function () {
        console.log("saveUser");
        console.log($scope.User);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchUser , $scope.User)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendSave = false;
                    $scope.backUser();
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log(response.data);
                    if ($location.checkAuthorized(response)) {

                        alert(response.data.message);
                    }
                });
        }
    }
    $scope.backUser = function (){
        $location.path('/user' );

    }
    console.log("Start");

    $scope.loadRole();
})