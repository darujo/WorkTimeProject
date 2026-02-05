let updateApp = angular.module('workTimeService', []);

// DIRECTIVE - FILE MODEL
updateApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            let model = $parse(attrs.fileModel);
            let modelSetter = model.assign;
            console.log(element[0])
            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

updateApp.controller('updateController', function ($scope, $http, $location) {

    const constPatchUpdate = window.location.origin + '/update-service/v1/update';
        $scope.FormFile = {
            desc: "",
            files: [],
            dateUpdate: new Date(),
            type: "null"
        }
        $scope.InfoTypes = null;
        $scope.Message = {};
        let sendMessageForAll = false;
        // $scope.onFileSelected = function(event) {
        //     console.log(event)
        //     this.fileToUpload = event.item(0);
        // }
        $scope.sendMessage = function () {
            const formData = new FormData();
            // formData.append('file', $scope.uploadedFile);
            // console.log($scope.uploadedFile)
            console.log(formData);
            formData.append("description", $scope.FormFile.desc);
            for (let i = 0; i < $scope.FormFile.files.length; i++) {
                formData.append("file", $scope.FormFile.files[i]);
            }
            console.log("SendMessage");
            if (!sendMessageForAll) {
                sendMessageForAll = true;

                $http(
                    {
                        url: constPatchUpdate,
                        method: "post",
                        params: {
                            dateUpdate: $scope.FormFile.dateUpdate,
                            type: $scope.FormFile.type
                        },
                        data: formData,
                        // reportProgress: true, // Без observe: 'events' не работает
                        // observe: 'events', // без reportProgress: true только HttpEventType.Sent и HttpEventType.Response
                        transformRequest: angular.identity,
                        transformResponse: angular.identity,
                        headers: {
                            'Content-Type': undefined
                        }

                        // , formData, config
                    }
                )
                    .then(function (response) {
                        console.log("Send Update")
                        console.log(response);
                        sendMessageForAll = false;
                        alert("Обновление успешно отправлено");
                    }, function errorCallback(response) {
                        sendMessageForAll = false;
                        console.log(response.data);
                        if ($location.checkAuthorized(response)) {

                            alert(response.data.message);
                        } else {
                            alert(response.data);
                        }
                    });
            } else {
                alert("Подождите отправляется предыдущее обновление")
            }

        }
    let getService = function () {
        console.log("edit");
        $http.get(constPatchUpdate + "/services")
            .then(function (response) {
                $scope.UpdateTypes = response.data;
                console.log($scope.UpdateTypes);


            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };
    getService();
    console.log("$scope.FormFile.type", $scope.FormFile.type, $scope.FormFile.type === "null");
        $scope.backUser = function () {
            $location.path('/');

        }
        console.log("Start");

        // $scope.loadInfoType();
    }
)