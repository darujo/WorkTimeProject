angular.module('workTimeService').controller('emailChangeController', function ($scope, $http, $location,) {

    const constPatchUser = window.location.origin + '/users/user/email/change';
    $scope.changEmail = function () {
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load = true;
            $http({
                url: constPatchUser,
                method: "get",
                params: {email: $scope.User.email}
            }).then(function (response) {
                $scope.load = false;
                console.log("response :");
                console.log(response);
                if (response.data) {
                    alert("Почта будет изменена после подтверждения. Почту отправлено ссылка для подтверждения.")
                    $scope.back()
                } else {
                    alert("Не верный код.")
                }

            }, function errorCallback(response) {
                alert(response.data.message);
                $scope.load = false;

            });
        }
    };
    $scope.back = function () {
        $location.path('/');

    }
    console.log("Start");


})