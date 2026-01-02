angular.module('workTimeService').controller('updateController', function ($scope, $http, $location) {

    const constPatchInfo = window.location.origin + '/info-service/v1/mes_info/update';

    $scope.InfoTypes = null;
    $scope.Message = {};
    let sendMessageForAll = false;
    $scope.onFileSelected = function(event) {
        console.log(event)
        this.fileToUpload = event.item(0);
    }
    $scope.sendMessage = function () {
        const formData = new FormData();
        formData.append('file', $scope.Message.fileToUpload);
        console.log($scope.Message.fileToUpload)
        console.log(formData);

        console.log("SendMessage");
        if (!sendMessageForAll) {
            sendMessageForAll = true;
            // $http.post(constPatchInfo + "?type=" + $scope.Message.type, $scope.Message.text)
            var config = {
                transformRequest: angular.identity,
                transformResponse: angular.identity,
                headers : {
                    'Content-Type': undefined
                }
            }

            $http.post(constPatchInfo, formData, config)
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