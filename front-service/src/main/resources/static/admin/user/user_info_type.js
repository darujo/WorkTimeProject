angular.module('workTimeService').controller('userInfoTypeController', function ($scope, $http, $location,) {

    const constPatchUser = window.location.origin + '/users/user';
    const constPatchLink = window.location.origin + '/users/user/telegram';
    $scope.User = {infoTypes: null};
    $scope.User = null;
    $scope.Filter = {infoType: "Email", viewLink: false};

    $scope.loadInfoType = function () {
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос получения списка типов уведомлений")
        } else {
            $scope.User = null;
            $scope.load = true;
            console.log("запрос данных страницы");
            console.log(window.location);
            $scope.Filter["viewLink"] = $scope.Filter.infoType === "Telegram";
            $http({
                url: constPatchUser + "/info/type/" + userIdInfo,
                params: {senderType: $scope.Filter.infoType},
                method: "get"
            }).then(function (response) {
                $scope.load = false;
                console.log("response :");
                console.log(response);

                $scope.User = response.data;
                console.log($scope.Filter.viewLink)
            }, function errorCallback(response) {
                $scope.load = false;
                console.log(response);
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

            });
        }
    };
    let userIdInfo;
    let init = function () {
        let paramsStr = new URLSearchParams(location.href.substring(location.href.indexOf("?")));
        userIdInfo = paramsStr.get('userId');
        if (userIdInfo === undefined) {
            $scope.Cancel();
        }
    }

    $scope.loadUserType = function () {
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос получения списка типов уведомлений")
        } else {
            $scope.load = true;
            console.log("запрос данных страницы");
            console.log(userIdInfo)
            if (userIdInfo === undefined) {
                $scope.Cancel();
            }
            $http({
                url: constPatchUser + "/sender/type/" + userIdInfo,
                method: "get"
            }).then(function (response) {
                $scope.load = false;
                console.log("response :");
                console.log(response);

                $scope.InfoTypes = response.data;
                console.log("data :");
                console.log($scope.InfoTypes);

                if ($scope.InfoTypes.length > 0) {
                    $scope.Filter.infoType = $scope.InfoTypes[0].code;
                }


                console.log($scope.Filter)
                $scope.loadInfoType()
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
            $http.post(constPatchUser + "/info/type?senderType=" + $scope.Filter.infoType, $scope.User)
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
    $scope.link = function (nikName, infoType) {
        $http({
            url: constPatchLink + "/get",
            method: "get",
            params: {
                nikName: nikName,
                messageType: infoType.code,
                projectId: infoType.projectId

            }
        }).then(function (response) {
            console.log("Save response")
            console.log(response);
            infoType.message = response.data;

        }, function errorCallback(response) {
            console.log(response.data);
            if ($location.checkAuthorized(response)) {
                alert(response.data.message);
            }
        });
    }
    $scope.delLink = function (nikName, infoType) {
        $http({
            url: constPatchLink + "/delete/type",
            method: "get",
            params: {
                nikName: nikName,
                messageType: infoType.code
            }
        }).then(function (response) {
            console.log("Save response")
            console.log(response);
            $scope.loadInfoType();
        }, function errorCallback(response) {
            console.log(response.data);
            if ($location.checkAuthorized(response)) {
                alert(response.data.message);
            }
        });
    }

    $scope.Cancel = function () {
        $location.path('');

    }
    console.log("Start");
    init();
    $scope.loadUserType();
})