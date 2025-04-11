(function () {
    angular
        .module('workTimeService', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        let ver = "1.2";
        $routeProvider
            .when('/', {
                templateUrl: 'welcome/welcome.html',
                controller: 'welcomeController'
            })
            .when('/worktime', {

                templateUrl: 'worktime/worktime.html?ver=' + ver,
                controller: 'worktimeController'
            })
            .when('/worktimerep', {

                templateUrl: 'rep/worktimerep.html?ver=' + ver,
                controller: 'workTimeRepController'
            })
            .when('/work/fact', {

                templateUrl: 'rep/factwork.html?ver=' + ver,
                controller: 'workFactRepController'
            })
            .when('/work', {
                templateUrl: 'work/work.html?ver=' + ver,
                controller: 'workController'
            })
            .when('/task', {
                templateUrl: 'task/task.html?ver=' + ver,
                controller: 'taskController'
            })
            .when('/rate', {
                templateUrl: 'workrate/workrate.html?ver=' + ver,
                controller: 'workRateController'
            })
            .when('/calendar', {
                templateUrl: 'calendar/calendar.html?ver=' + ver,
                controller: 'calendarController'
            })
            .when('/weekwork', {
                templateUrl: 'rep/weekwork/weekwork.html?ver=' + ver,
                controller: 'weekworkController'
            })
            .when('/userwork', {
                templateUrl: 'rep/userwork/userwork.html?ver=' + ver,
                controller: 'userworkController'
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
    const constPatchUser = window.location.origin + '/users';
    const constPatchCode = window.location.origin + '/task-service/v1/';

    $scope.tryToAuth = function () {
        $http.post(constPatchAuth + '/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.authUser = {username: $scope.user.username, token: response.data.token};
                    $scope.getUser();
                    loadUsers();

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
            let nikName = $localStorage.authUser.username;
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
            $location.openEdit();
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
        if (parseInt(response.status) === 401) {
            checkToken("Вы не авторизованы");
            $scope.tryToLogout();
            return false;
        }
        return true;

    }

    $location.getCode = function (code,callBack) {
        $http({
            url: constPatchCode + code,
            method: "get"

        }).then(function (response) {
           callBack(response);
        }, function errorCallback(response) {
            // console.log(response)
            if ($location.checkAuthorized(response)) {
                alert(response.data.message);
            }
        });

    }


    if (typeof $localStorage.filterWorkTime === "undefined") {
        $localStorage.filterWorkTime = {}
    }

    $location.saveFilter = function (name, filter) {
        if (typeof $scope.SettingUser !== "undefined") {
            if ($scope.SettingUser.saveFilter) {
                console.log("Save filter");
                console.log(name);
                console.log(filter);
                $localStorage.filterWorkTime[name] = filter;
            }
        }
    }
    $location.getFilter = function (name) {

        console.log("$scope.SettingUser.loadFilter")
        console.log($scope.SettingUser);
        if (typeof $scope.SettingUser !== "undefined") {
            if ($scope.SettingUser.loadFilter) {
                if (typeof $localStorage.filterWorkTime[name] === "undefined") {
                    return null;
                }
                console.log("loadFilter");
                console.log(name);
                console.log($localStorage.filterWorkTime[name]);
                return $localStorage.filterWorkTime[name];
            }
        }
        return null;

    }
    $scope.delFilter = function () {
        $localStorage.filterWorkTime = {};
    }
    let checkToken = function (message) {
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
    let loadUsers = function () {
        // console.log("Users")

        $http({
            url: constPatchUser,
            method: "get"

        }).then(function (response) {
            // console.log(response.data);
            $location.UserList = response.data;
        }, function errorCallback(response) {
            // console.log(response)
            if ($location.checkAuthorized(response)) {
                alert(response.data.message);
            }
        });

    }
    $scope.saveSetting = function (){
        $localStorage.UserSettingWorkTime = $scope.SettingUser;
    }
    $scope.SettingUser = $localStorage.UserSettingWorkTime;
    if ($scope.isUserLoggedIn) {

        loadUsers();
        $scope.getUser();
    }

    console.log("dddddd");
})