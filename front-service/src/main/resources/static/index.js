(function () {
    angular
        .module('workTimeService', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'welcome/welcome.html',
                controller: 'welcomeController'
            })
            .when('/worktime', {

                templateUrl: 'worktime/worktime.html?v=1.1',
                controller: 'worktimeController'
            })
            .when('/worktimerep', {

                templateUrl: 'rep/worktimerep.html',
                controller: 'workTimeRepController'
            })
            .when('/factwork', {

                templateUrl: 'rep/factwork.html',
                controller: 'workFactRepController'
            })
            .when('/work', {
                templateUrl: 'work/work.html',
                controller: 'workController'
            })
            .when('/task', {
                templateUrl: 'task/task.html',
                controller: 'taskController'
            })
            
            .otherwise({
                redirectTo: '/'
            });
    }

    function run($rootScope, $http, $localStorage) {
        if ($localStorage.authUser){
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.authUser.token;
            try {
                let jwt = $localStorage.authUser.token;
                let payLoad = JSON.parse(atob(jwt.split(".")[1]));
                let currTime = parseInt(new Date().getTime() / 1000);
                if(currTime > payLoad.exp){
                    console.log("токен просрочен");
                    delete $localStorage.authUser;
                    $http.defaults.headers.common.Authorization = '';
                }
            }
            catch (e){

            }
        }
    }

})();

angular.module('workTimeService').controller('indexController', function ($rootScope, $scope, $http, $location, $localStorage) {
    const constPatchAuth    = 'http://localhost:5555';

    $scope.tryToAuth = function () {
        $http.post(constPatchAuth + '/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.authUser = {username: $scope.user.username, token: response.data.token};

                    $scope.user.username = null;
                    $scope.user.password = null;

                    $location.path('/');
                }
            }, function errorCallback(response) {
                console.log(response);
                alert("Не удалось авторизоваться")
            });
    };

    $scope.tryToLogout = function () {
        $scope.clearUser();
        if ($scope.user.username) {
            $scope.user.username = null;
        }
        if ($scope.user.password) {
            $scope.user.password = null;
        }
        $location.path('/');
    };

    $scope.clearUser = function () {
        delete $localStorage.authUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $scope.isUserLoggedIn = function () {
        if ($localStorage.authUser) {
            return true;
        } else {
            $location.path('/');
            return false;
        }
    };

    $location.checkAuthorized= function (response){
        console.log("response.status");
        console.log(response.status);
        if(parseInt(response.status) == 401){
            checkToken("Вы не авторизованы");
            $scope.tryToLogout();
            return false;
        }
        return true;

    }
    var checkToken = function (message){
        if ($localStorage.authUser){
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.authUser.token;
            try {
                let jwt = $localStorage.authUser.token;
                let payLoad = JSON.parse(atob(jwt.split(".")[1]));
                let currTime = parseInt(new Date().getTime() / 1000);
                if(currTime > payLoad.exp){
                    alert("Токен просрочен авторизуйтесь заново.");
                    delete $localStorage.authUser;
                    $http.defaults.headers.common.Authorization = '';
                }
                else {
                    alert(message);
                }
            }
            catch (e){

            }
        }
    }
})