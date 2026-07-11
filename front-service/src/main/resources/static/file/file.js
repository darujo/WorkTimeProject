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
            objectId: "12354first3",
            objectName: "Имя объекта"

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
            console.log("getFiles");

            $http.get(constPatchFile + "/documents")
                .then(function (response) {
                    $scope.FileList = response.data;
                    console.log($scope.FileList);


                }, function errorCallback(response) {
                    console.log(response)
                    if ($location.checkAuthorized(response)) {
                        //     alert(response.data.message);
                    }
                });
        };
        $scope.getOneDoc = function (fileId) {
            getDocument(fileId);
        }
        $scope.getListDocCheckbox = function () {
            let fileId = [];
            for (let i = 0; i < $scope.FileList.length; i++) {
                if ($scope.FileList[i].active) {
                    // fileId = (fileId  ? fileId + "&" : "") + "fileId=" +  $scope.FileList[i].id
                    fileId.push($scope.FileList[i].id)
                }
            }
            console.log(fileId)
            if (fileId.length > 0) {
                getDocument(fileId)
            } else {
                if ($scope.FileList.length > 0) {
                    let downloadAll = confirm("Скачать все?");
                    if (downloadAll) {
                        for (let i = 0; i < $scope.FileList.length; i++) {
                            fileId.push($scope.FileList[i].id)
                            // fileId = (fileId ? fileId + "&" : "") + "fileId=" + $scope.FileList[i].id;
                        }
                        getDocument(fileId);
                    }
                }
            }
        }
        let getDocument = function (list) {
            console.log("getDocuments");
            console.log(list)
            // window.location = constPatchFile +'/document?' + list;
            $http({
                url: constPatchFile + "/document",
                method: "get",
                responseType: 'arraybuffer',
                params: {
                    fileId: list
                }
            }).then(function (response) {
                console.log(response.headers('content-disposition'))

                let downloadLink = document.createElement("a");

                document.body.appendChild(downloadLink);
                downloadLink.style = "display: none";
                console.log(response)
                let fName = "response.zip";
                const contentDisposition = response.headers('Content-Disposition');
                if (contentDisposition) {
                    // const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                    // const matches = fileNameRegex.exec(contentDisposition);
                    // if (matches != null && matches[1]) {
                    //     fName = matches[1].replace(/['"]/g, '');
                    // }
                    const utf8FilenameRegex = /filename\*=UTF-8''([\w%\-\.]+)(?:; ?|$)/i;
                    const asciiFilenameRegex = /^filename=(["']?)(.*?[^\\])\1(?:; ?|$)/i;

                    let fileName;
                    if (utf8FilenameRegex.test(contentDisposition)) {
                        fileName = decodeURIComponent(utf8FilenameRegex.exec(contentDisposition)[1]);
                    } else {
                        // prevent ReDos attacks by anchoring the ascii regex to string start and
                        //  slicing off everything before 'filename='
                        const filenameStart = contentDisposition.toLowerCase().indexOf('filename=');
                        if (filenameStart >= 0) {
                            const partialDisposition = contentDisposition.slice(filenameStart);
                            const matches = asciiFilenameRegex.exec(partialDisposition);
                            if (matches != null && matches[2]) {
                                fileName = matches[2];
                            }
                        }
                    }
                    console.log("!!!!!fileName= ", fileName)
                    fName = fileName;
                }
                let file = new Blob([response.data], {type: 'application/*'});
//Blob, client side object created to with holding browser specific download popup, on the URL created with the help of window obj.

                downloadLink.href = (window.URL || window.webkitURL).createObjectURL(file);
                downloadLink.download = fName;
                downloadLink.click();
                return response;


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