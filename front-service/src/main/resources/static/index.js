(function () {
    angular
        .module('workTimeService', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        let ver = "1.2.1";
        console.log(ver);
        $routeProvider
            .when('/', {
                templateUrl: 'welcome/welcome.html',
                controller: 'welcomeController'
            })
            .when('/workTime'.toLowerCase(), {

                templateUrl: 'workTime/workTime.html?ver='.toLowerCase() + ver,
                controller: 'workTimeController'
            })
            .when('/workTimeRep'.toLowerCase(), {

                templateUrl: 'rep/workTimeRep.html?ver='.toLowerCase() + ver,
                controller: 'workTimeRepController'
            })
            .when('/work/fact', {

                templateUrl: 'rep/factWork.html?ver='.toLowerCase() + ver,
                controller: 'workFactRepController'
            })
            .when('/work', {
                templateUrl: 'work/work.html?ver=' + ver,
                controller: 'workController'
            })
            .when('/release', {
                templateUrl: 'work/release.html?ver=' + ver,
                controller: 'releaseController'
            })
            .when('/task', {
                templateUrl: 'task/task.html?ver=' + ver,
                controller: 'taskController'
            })
            .when('/rate', {
                templateUrl: 'workRate/workRate.html?ver='.toLowerCase() + ver,
                controller: 'workRateController'
            })
            .when('/agreement/request', {
                templateUrl: 'workRate/request.html?ver='.toLowerCase() + ver,
                controller: 'requestController'
            })
            .when('/agreement/response', {
                templateUrl: 'workRate/response.html?ver='.toLowerCase() + ver,
                controller: 'responseController'
            })
            .when('/agreement', {
                templateUrl: 'workRate/agreement.html?ver='.toLowerCase() + ver,
                controller: 'agreementController'
            })
            .when('/calendar', {
                templateUrl: 'calendar/calendar.html?ver=' + ver,
                controller: 'calendarController'
            })
            .when('/weekWork'.toLowerCase(), {
                templateUrl: 'rep/weekWork/weekWork.html?ver='.toLowerCase() + ver,
                controller: 'weekWorkController'
            })
            .when('/userWork'.toLowerCase(), {
                templateUrl: 'rep/userWork/userWork.html?ver='.toLowerCase() + ver,
                controller: 'userWorkController'
            })
            .when('/user', {
                templateUrl: 'admin/user/user.html?ver=' + ver,
                controller: 'userController'
            })
            .when('/user_role', {
                templateUrl: 'admin/user/user_role.html?ver=' + ver,
                controller: 'userRoleController'
            })
            .when('/role', {
                templateUrl: 'admin/role/role.html?ver=' + ver,
                controller: 'roleController'
            })
            .when('/role_right', {
                templateUrl: 'admin/role/role_right.html?ver=' + ver,
                controller: 'roleRightController'
            })
            .when('/vacation', {
                templateUrl: 'calendar/vacation/vacation.html?ver=' + ver,
                controller: 'vacationController'
            })
            .when('/userVacation'.toLowerCase(), {
                templateUrl: 'rep/userVacation/userVacation.html?ver='.toLowerCase() + ver,
                controller: 'userVacationController'
            })
            .when('/userPassword'.toLowerCase(), {
                templateUrl: 'admin/user/user_password.html?ver=' + ver,
                controller: 'userChangeController'
            })
            .when('/user_info_type'.toLowerCase(), {
                templateUrl: 'admin/user/user_info_type.html?ver=' + ver,
                controller: 'userInfoTypeController'
            })
            .when('/message'.toLowerCase(), {
                templateUrl: 'admin/user/message.html?ver=' + ver,
                controller: 'messageController'
            })
            .when('/work_graph', {
                templateUrl: 'workGraph/workGraph.html?ver='.toLowerCase() + ver,
                controller: 'workGraphController'
            })
            .otherwise({
                redirectTo: '/'
            });

    }

    function run($rootScope, $http, $localStorage, $location) {
        $rootScope.$on('$routeChangeStart', function (event, next, current) {
            if (typeof current !== 'undefined') {
                console.log("detail close");
                document.getElementById("DetailPrim").open = false;
            } else {
                console.log("detail open");
                document.getElementById("DetailPrim").open = true;
            }
        });
        $rootScope.$on('$routeChangeSuccess', function () {
            if (location.hash !== "#!/") {
                $location.parserFilter("");
            }

        });
        console.log($localStorage.authUser)
        if ($localStorage.authUser) {
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.authUser.token;
            try {
                let jwt = $localStorage.authUser.token;
                let payLoad = JSON.parse(atob(jwt.split(".")[1]));
                let currTime = parseInt((new Date().getTime() / 1000).toString());
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
    const constPatchRole = window.location.origin + '/roles';
    const constPatchCode = window.location.origin + '/task-service/v1/';
    const constPatchRelease = window.location.origin + '/work-service/v1/release';
    $scope.loadFilter = null;
    $scope.tryToAuth = function () {
        $http.post(constPatchAuth + '/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.authUser = {username: $scope.user.username, token: response.data.token};
                    init();

                    $scope.user = {
                        username: null,
                        password: null
                    };
                    // $location.path('/').search({});
                    $location.path(myHash).search(myFilter);
                    document.getElementById("DetailPrim").open = true;
                }
            }, function errorCallback(response) {
                console.log(response);
                alert("Не удалось авторизоваться")
            });
    };
    $location.backPage = function () {
        $scope.getUser();
        if (myHash === "userPassword".toLowerCase()) {
            $location.path("").search({});
        } else {
            $location.path(myHash).search(myFilter);
        }
    }

    $scope.getUser = function () {
        console.log($localStorage.authUser);
        if (typeof $localStorage.authUser !== "undefined") {
            let nikName = $localStorage.authUser.username;
            console.log("getUser")
            // document.getElementById("UserName").value = nikName;

            $http.get(constPatchAuth + '/users/user?nikName=' + nikName)
                .then(function successCallback(response) {
                    console.log(response)
                    $scope.UserLogin = response.data;
                    if ($scope.UserLogin.passwordChange) {
                        $location.path('/userPassword'.toLowerCase()).search({});

                    }


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
        if (window.location.hash === "#!/workTime".toLowerCase()) {
            $location.openEdit().search({});
        } else {
            $location.path('/workTime'.toLowerCase()).search({});
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
        $location.path('/').search({});
    };

    $scope.clearUser = function () {
        delete $localStorage.authUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $scope.isUserLoggedIn = function () {
        if ($localStorage.authUser) {
            return true;
        } else {
            $location.path('/').search({});
            return false;
        }
    };

    $scope.isUserLoggedInAndPasOk = function () {
        if (typeof $scope.UserLogin === "undefined") {
            return $scope.isUserLoggedIn();
        } else {
            return !$scope.UserLogin.passwordChange && $scope.isUserLoggedIn();
        }
    };

    $scope.addTelegram = function () {
            console.log("getUser")
            // document.getElementById("UserName").value = nikName;

            $http.get(constPatchAuth + '/users/user/telegram/get')
                .then(function successCallback(response) {
                    console.log(response)
                    $scope.CodeTelegram = response.data;


                    // document.getElementById("UserName").value = response.data.lastName + " " + response.data.firstName + " " + response.data.patronymic;
                }, function errorCallback(response) {
                    console.log(response);
                });
    };

    $scope.deleteTelegram = function () {
        console.log("getUser")
        // document.getElementById("UserName").value = nikName;

        $http.get(constPatchAuth + '/users/user/telegram/delete')
            .then(function successCallback(response) {
                console.log(response)
                $scope.getUser();
                $scope.CodeTelegram = null;


                // document.getElementById("UserName").value = response.data.lastName + " " + response.data.firstName + " " + response.data.patronymic;
            }, function errorCallback(response) {
                console.log(response);
            });
    };

    $location.checkAuthorized = function (response) {
        console.log("response.status");
        console.log(response.status);
        if (parseInt(response.status) === 500) {
            alert("Произошла ошибка. Обратитесь к администратору. " + response.data.message);
            return false;
        } else if (parseInt(response.status) === 403) {
            alert("Нет прав: " + response.data);
            return false;
        } else if (parseInt(response.status) === 401) {
            checkToken("Вы не авторизованы " + response.data);
            $scope.tryToLogout();
            return false;
        }
        return true;

    }
    $location.sendFilter = function (url, filter) {
        let localPath;
        if (url.indexOf("?") === -1) {
            localPath = url.substring(url.indexOf("/"));
        } else {
            localPath = url.substring(url.indexOf("/"), url.indexOf("?"));
        }
        console.log(localPath);
        $location.path(localPath).search(filter);
        console.log(location.href)
        navigator.clipboard.writeText(location.href)
            .then(() => {
                // Получилось!
            })
            .catch(err => {
                console.log('Something went wrong', err);
            });
    }

    $location.getCode = function (code, callBack) {
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
    let myHash;
    let myFilter = {};
    $location.parserFilter = function (filter) {
        console.log("parserFilter");
        console.log(location);
        console.log(location.href);
        let strPars;
        console.log(location.href.lastIndexOf("#!"))
        if (location.href.lastIndexOf("#!") > location.href.indexOf("?")) {
            strPars = location.href.substring(location.href.lastIndexOf("#!"));
            console.log(strPars)
            window.open(location.origin + "/" + strPars, "_self");
            return;
        } else {
            strPars = location.href;
        }
        if (strPars.indexOf("?") !== -1) {

            console.log(strPars)
            let paramsStr = new URLSearchParams(strPars.substring(strPars.lastIndexOf("?")));
            for (let [key, value] of paramsStr.entries()) {
                if (key.toLowerCase().indexOf("stage") !== -1
                    || key.toLowerCase().indexOf("size") !== -1
                    || key.toLowerCase().indexOf("sap") !== -1
                    || key.indexOf("period") !== -1
                    || key.toLowerCase().indexOf("kid") !== -1
                    || key.toLowerCase().indexOf("type") !== -1
                ) {
                    console.log("int");
                    console.log(key);
                    console.log(value);
                    filter[key] = parseInt(value);
                } else if (key.toLowerCase().indexOf("date") !== -1) {
                    console.log(key);

                    console.log(paramsStr.get(key))
                    console.log(new Date(paramsStr.get(key)));

                    filter[key] = new Date(paramsStr.get(key));
                } else if (key.indexOf("weekSplit") !== -1
                    || key.indexOf("workTask") !== -1
                    || key.indexOf("workTime") !== -1
                    || key.indexOf("workPercent") !== -1
                    || key.indexOf("addTotal") !== -1
                    || key.indexOf("ziSplit") !== -1
                    || key.indexOf("hideNotTime") !== -1
                    || key.indexOf("avail") !== -1

                ) {
                    console.log(key);

                    console.log(paramsStr.get(key))
                    console.log(value)
                    filter[key] = paramsStr.get(key) !== "false";
                } else if (key.indexOf("listId") !== -1) {
                    console.log("listId");
                    console.log(typeof filter[key]);
                    console.log(paramsStr.get(key));
                    console.log(paramsStr.getAll(key));

                    if (typeof filter[key] === "undefined") {
                        filter[key] = [];
                    }
                    filter[key].push(parseInt(value));
                    console.log(filter[key]);
                } else {
                    console.log(key);
                    paramsStr.get(key);
                    console.log(value);
                    filter[key] = paramsStr.get(key);
                }

            }
            myFilter = filter;
            myHash = location.hash.substring(location.hash.indexOf("/") + 1, location.hash.indexOf("?"));
        } else {
            myHash = location.hash.substring(location.hash.indexOf("/") + 1);
            myFilter = {};
        }
        console.log("myHash")
        console.log(myHash);
        console.log(filter);
        return filter;
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
                let currTime = parseInt((new Date().getTime() / 1000).toString());
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
    let userLoad = false;
    let UserList;
    let loadUsers = function () {
        console.log("Users")

        $http({
            url: constPatchUser,
            method: "get"

        }).then(function (response) {
            console.log("response user");
            console.log(response.data);
            UserList = response.data.content;
            userLoad = true;
        }, function errorCallback(response) {
            console.log(" --- response user");
            console.log(response)
            userLoad = true;
            if ($location.checkAuthorized(response)) {
                alert(response.data.message);
            }
        });

    }
    let roleLoad = false;
    let RoleList;
    let loadRoles = function () {
        console.log("Roles")
        roleLoad = false;
        $http({
            url: constPatchRole,
            method: "get"

        }).then(function (response) {
            console.log("response role");
            console.log(response.data);
            RoleList = response.data;
            roleLoad = true;

        }, function errorCallback(response) {
            console.log(" --- response user");
            console.log(response)
            if ($location.checkAuthorized(response)) {
                alert(response.data.message);
            }
            roleLoad = true;
        });

    }
    let releaseLoad;
    let ReleaseList;
    let loadRelease = function () {
        console.log("start loadRelease")
        releaseLoad = false;
        $http({
            url: constPatchRelease,
            method: "get"

        }).then(function (response) {
            console.log("response Release");
            console.log(response.data);
            ReleaseList = response.data;
            releaseLoad = true;
        }, function errorCallback(response) {
            console.log(" --- response Release");
            console.log(response)
            releaseLoad = true;
            if ($location.checkAuthorized(response)) {
                alert(response.data.message);
            }
        });

    }

    function wait() {
        return new Promise((resolve, reject) => {
            console.log(reject);
            setTimeout(() => {
                resolve('Timeout resolved');
            }, 10);
        });
    }

    $location.getRoles = async function () {
        while (!roleLoad) {
            await wait();
        }
        return RoleList;
    }
    $location.getReleases = async function () {
        while (!releaseLoad) {
            await wait();
        }
        return ReleaseList;
    }
    $location.getUsers = async function () {
        console.log("userLoad");
        console.log(userLoad);
        while (!userLoad) {
            await wait();
        }
        return UserList;
    }
    $scope.saveSetting = function () {
        $localStorage.UserSettingWorkTime = $scope.SettingUser;
    }
    $scope.SettingUser = $localStorage.UserSettingWorkTime;
    let init = function () {
        loadRoles();
        loadUsers();
        loadRelease();
        $scope.getUser();

    }
    if ($scope.isUserLoggedIn) {
        init();
    }

    console.log("loan index.js end");
})