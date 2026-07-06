let fileApp = angular.module('workTimeService', []);

// DIRECTIVE - FILE MODEL
fileApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            let model = $parse(attrs.fileModel);
            let modelSetter = model.assign;

            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files);
                });
            });
        }
    };
}]);

fileApp.controller('fileController', function ($scope, $http, $location) {
        const constPatchFile = window.location.origin + '/file-service/v1/file';
        $scope.FormFile = {
            files: [],
            objectType: "hobby",
            objectId: "12354first3"

        }


        // $scope.sendMessageForAll = false;
        // $scope.onFileSelected = function(event) {
        //     console.log(event)
        //     this.fileToUpload = event.item(0);
        // }

        $scope.sendFile = function () {
            const formData = new FormData();
            // formData.append('file', $scope.uploadedFile);
            // console.log($scope.uploadedFile)
            console.log($scope.FormFile.files)
            console.log(formData);
            for (let i = 0; i < $scope.FormFile.files.length; i++) {
                formData.append("file", $scope.FormFile.files[i]);
            }
            console.log("SendFile");
            if (!$scope.sendFileAll) {
                $scope.sendFileAll = true;

                $http.post(constPatchFile, formData,
                    {
                        // url: constPatchUpdate,
                        // method: "post",
                        params: {
                            objectType: $scope.FormFile.objectType,
                            objectId: $scope.FormFile.objectId
                        },
                        // data: formData,
                        // reportProgress: true, // Без observe: 'events' не работает
                        // observe: 'events', // без reportProgress: true только HttpEventType.Sent и HttpEventType.Response
                        transformRequest: angular.identity,
                        // блокирует преобразование ответа в объект, а мы этого не хотим
                        // transformResponse: angular.identity,
                        headers: {
                            'Content-Type': undefined
                        },
                        uploadEventHandlers: {
                            progress: function (event) {
                                if (event.lengthComputable) {
                                    console.log(event);
                                    let progressPercentage = Math.round((event.loaded / event.total) * 100);
                                    $scope.uploadProgress = progressPercentage;
                                    console.log('Upload Progress: ' + progressPercentage + '%');
                                }
                            }
                        }

                        // , formData, config
                    }
                )
                    .then(function (response) {
                        console.log("Send Update")
                        console.log(response);
                        $scope.sendFileAll = false;
                        alert("Файлы успешно загружено");
                        getFiles();
                    }, function errorCallback(responseFile) {
                        $scope.sendFileAll = false;
                        console.log("Ошибка загрузки");
                        console.log(responseFile);

                        if ($location.checkAuthorized(responseFile)) {
                            console.log(1)
                            if (responseFile.data.message === undefined) {
                                alert(responseFile.data);
                            } else {
                                alert(responseFile.data.message);
                            }
                        } else {
                            console.log(2)
                            alert(responseFile.data);
                        }
                    });
            } else {
                alert("Подождите отправляется предыдущие файлы")
            }

        }
        let getFiles = function () {
            console.log("edit");
            $http.get(constPatchFile + "/documents")
                .then(function (response) {
                    $scope.FileList = response.data;
                    console.log($scope.UpdateTypes);


                }, function errorCallback(response) {
                    console.log(response)
                    if ($location.checkAuthorized(response)) {
                        //     alert(response.data.message);
                    }
                });
        };
        getFiles();
        console.log("$scope.FormFile.objectType", $scope.FormFile.objectType, $scope.FormFile.objectId);
        $scope.backUser = function () {
            $location.path('/');

        }
        console.log("Start");

        // $scope.loadInfoType();
        $scope.getProcess = function () {
            return {width: $scope.uploadProgress + "%"}
        }
    }
)