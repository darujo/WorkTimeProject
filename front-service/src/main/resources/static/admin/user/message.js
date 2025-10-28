angular.module('workTimeService').controller('messageController', function ($scope, $http, $location,) {

    const constPatchUser = window.location.origin + '/users/user/info/types';
    const constPatchInfo = window.location.origin + '/info-service/v1/mes_info/add/message';

    $scope.InfoTypes = null;
    $scope.Message = {};
    $scope.loadInfoType = function () {
        $scope.InfoTypes = null;
        console.log("запрос данных страницы");
        console.log(window.location);

        $http({
            url: constPatchUser ,
            method: "get"
        }).then(function (response) {
            $scope.load = false;
            console.log("response :");
            console.log(response);

            $scope.InfoTypes = response.data.infoTypes;
        }, function errorCallback(response) {
            $scope.load = false;
            console.log(response);
            if ($location.checkAuthorized(response)) {
                //     alert(response.data.message);
            }

        });

    };
    let sendMessageForAll = false;
    $scope.sendMessage = function () {
        console.log($scope.User);
        console.log("SendMessage");
        if (!sendMessageForAll) {
            sendMessageForAll = true;
            // $http.post(constPatchInfo + "?type=" + $scope.Message.type, $scope.Message.text)
            $http.post(constPatchInfo , $scope.Message)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendMessageForAll = false;
                    alert("Список уведомлений сохранен");
                }, function errorCallback(response) {
                    sendMessageForAll = false;
                    console.log(response.data);
                    if ($location.checkAuthorized(response)) {

                        alert(response.data.message);
                    }
                });
        }
    }
    $scope.backUser = function () {
        $location.path('/');

    }
    console.log("Start");

    $scope.loadInfoType();
})