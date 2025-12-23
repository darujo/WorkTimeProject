angular.module('workTimeService').controller('updateController', function ($scope, $http, $location,) {

    const constPatchInfo = window.location.origin + '/info-service/v1/mes_info/add/message';

    $scope.InfoTypes = null;
    $scope.Message = {};
    let sendMessageForAll = false;
    $scope.sendMessage = function () {
        console.log($scope.User);
        const formData = new FormData();
        formData.append('file', $scope.Message.fileUpload);
        console.log("SendMessage");
        if (!sendMessageForAll) {
            sendMessageForAll = true;
            // $http.post(constPatchInfo + "?type=" + $scope.Message.type, $scope.Message.text)
            $http.post(constPatchInfo, formData)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendMessageForAll = false;
                    alert("Сообщения отправлены");
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

    // $scope.loadInfoType();
})