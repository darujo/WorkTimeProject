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
            .when('/calendar', {
                templateUrl: 'calendar/calendar.html',
                controller: 'calendarController'
            })
            .when('/weekwork', {
                templateUrl: 'rep/weekwork/weekwork.html',
                controller: 'weekworkController'
            })
            .otherwise({
                redirectTo: '/'
            });

    }

    function run($rootScope, $http, $localStorage) {
        $rootScope.$on('$routeChangeStart', function (event, next, current) {
            if (typeof current !== 'undefined') {
                console.log("detail close");
                document.getElementById("DetailPrim").open = false;
            } else {
                console.log("detail open");
                document.getElementById("DetailPrim").open = true;
            }
        });
        console.log($localStorage.authUser)
        if ($localStorage.authUser) {
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.authUser.token;
            try {
                let jwt = $localStorage.authUser.token;
                let payLoad = JSON.parse(atob(jwt.split(".")[1]));
                let currTime = parseInt(new Date().getTime() / 1000);
                if (currTime > payLoad.exp) {
                    console.log("токен просрочен");
                    delete $localStorage.authUser;
                    $http.defaults.headers.common.Authorization = '';
                }

            } catch (e) {

            }
        }

    }

})();

angular.module('workTimeService').controller('indexController', function ($rootScope, $scope, $http, $location, $localStorage) {
    const constPatchAuth = window.location.origin;

    $scope.tryToAuth = function () {
        $http.post(constPatchAuth + '/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.authUser = {username: $scope.user.username, token: response.data.token};
                    $scope.getUser();
                    $scope.user = {
                        username: null,
                        password: null
                    };

                    $location.path('/');
                    document.getElementById("DetailPrim").open = true;
                }
            }, function errorCallback(response) {
                console.log(response);
                alert("Не удалось авторизоваться")
            });
    };
    $scope.getUser = function () {
        console.log($localStorage.authUser);
        if (typeof $localStorage.authUser !== "undefined") {
            var nikName = $localStorage.authUser.username;
            console.log("getUser")
            // document.getElementById("UserName").value = nikName;

            $http.get(constPatchAuth + '/users/user?nikName=' + nikName)
                .then(function successCallback(response) {
                    console.log(response)
                    $scope.user = response.data;
                    // document.getElementById("UserName").value = response.data.lastName + " " + response.data.firstName + " " + response.data.patronymic;
                }, function errorCallback(response) {
                    console.log(response);
                });
        }
    }
    $scope.addTime = function () {
        console.log("index addTime")
        $localStorage.WorkTime = {
            edit: true
        };
        console.log($localStorage.WorkTime);
        console.log(window.location);
        console.log(window.location.hash === "#!/worktime");
        if (window.location.hash === "#!/worktime") {
            $localStorage.openEdit();
        } else {
            $location.path('/worktime');
        }
    };


    $scope.tryToLogout = function () {
        $scope.clearUser();
        $scope.user = {
            username: null,
            password: null
        };
        // if ($scope.user.username) {
        //     $scope.user.username = null;
        // }
        // if ($scope.user.password) {
        //     $scope.user.password = null;
        // }
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


    $location.checkAuthorized = function (response) {
        console.log("response.status");
        console.log(response.status);
        if (parseInt(response.status) == 401) {
            checkToken("Вы не авторизованы");
            $scope.tryToLogout();
            return false;
        }
        return true;

    }
    var checkToken = function (message) {
        if ($localStorage.authUser) {
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.authUser.token;
            try {
                let jwt = $localStorage.authUser.token;
                let payLoad = JSON.parse(atob(jwt.split(".")[1]));
                let currTime = parseInt(new Date().getTime() / 1000);
                if (currTime > payLoad.exp) {
                    alert("Токен просрочен авторизуйтесь заново.");
                    delete $localStorage.authUser;
                    $http.defaults.headers.common.Authorization = '';
                } else {
                    alert(message);
                }
            } catch (e) {

            }
        }
    }
    $scope.getUser();
    console.log("dddddd");
})