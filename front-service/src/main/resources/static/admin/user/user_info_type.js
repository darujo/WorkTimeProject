angular.module('workTimeService').controller('userInfoTypeController', function ($scope, $http, $location, ) {

    const constPatchUser = window.location.origin + '/users/user/info/type';
    $scope.User = {infoTypes:null};
    $scope.User = null
    $scope.loadInfoType = function () {
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос получения спсика типов уведомлений")
        }
        else {
            $scope.User = null;
            $scope.load = true;
            console.log("запрос данных страницы");
            console.log(window.location);
            let paramsStr = new URLSearchParams(location.href.substring(location.href.indexOf("?")));
            let userIdInfo = paramsStr.get('userId');
            console.log(userIdInfo)

            $http({
                url: constPatchUser + "/" + userIdInfo,
                method: "get"
            }).then(function (response) {
                $scope.load = false;
                console.log("response :");
                console.log(response);

                $scope.User = response.data;
            }, function errorCallback(response) {
                $scope.load = false;
                console.log(response);
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

            });
        }
    };
    let sendSaveInfoType = false;
    $scope.saveUserinfoType = function () {
        console.log($scope.User);
        console.log("saveUserinfoType");
        if (!sendSaveInfoType) {
            sendSaveInfoType = true;
            $http.post(constPatchUser , $scope.User)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendSaveInfoType = false;
                    alert("Список уведомлений сохранен");
                }, function errorCallback(response) {
                    sendSaveInfoType = false;
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

    $scope.loadInfoType();
})