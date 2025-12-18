angular.module('workTimeService').config(function ($routeProvider) {
    let ver = "2.0";
    console.log(ver);
    $routeProvider
        .when('/', {
            templateUrl: 'welcome/welcome.html',
            controller: 'welcomeController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('welcome'); // Resolve promise and load before view
                }]
            }
        })
        .when('/workTime'.toLowerCase(), {

            templateUrl: 'workTime/workTime.html?ver='.toLowerCase() + ver,
            controller: 'workTimeController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('workTime'); // Resolve promise and load before view
                }]
            }
        })
        .when('/workTimeRep'.toLowerCase(), {

            templateUrl: 'rep/workTimeRep.html?ver='.toLowerCase() + ver,
            controller: 'workTimeRepController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('workTimeRep'); // Resolve promise and load before view
                }]
            }
        })
        .when('/work/fact', {

            templateUrl: 'rep/factWork.html?ver='.toLowerCase() + ver,
            controller: 'workFactRepController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('factWork'); // Resolve promise and load before view
                }]
            }
        })
        .when('/work', {
            templateUrl: 'work/work.html?ver=' + ver,
            controller: 'workController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('work'); // Resolve promise and load before view
                }]
            }
        })
        .when('/release', {
            templateUrl: 'work/release.html?ver=' + ver,
            controller: 'releaseController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('release'); // Resolve promise and load before view
                }]
            }
        })
        .when('/task', {
            templateUrl: 'task/task.html?ver=' + ver,
            controller: 'taskController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('task'); // Resolve promise and load before view
                }]
            }
        })
        .when('/rate', {
            templateUrl: 'workRate/workRate.html?ver='.toLowerCase() + ver,
            controller: 'workRateController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('workRate'); // Resolve promise and load before view
                }]
            }
        })
        .when('/agreement/request', {
            templateUrl: 'workRate/request.html?ver='.toLowerCase() + ver,
            controller: 'requestController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('agreementRequest'); // Resolve promise and load before view
                }]
            }
        })
        .when('/agreement/response', {
            templateUrl: 'workRate/response.html?ver='.toLowerCase() + ver,
            controller: 'responseController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('agreementResponse'); // Resolve promise and load before view
                }]
            }
        })
        .when('/agreement', {
            templateUrl: 'workRate/agreement.html?ver='.toLowerCase() + ver,
            controller: 'agreementController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('agreement'); // Resolve promise and load before view
                }]
            }
        })
        .when('/calendar', {
            templateUrl: 'calendar/calendar.html?ver=' + ver,
            controller: 'calendarController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('calendar'); // Resolve promise and load before view
                }]
            }
        })
        .when('/weekWork'.toLowerCase(), {
            templateUrl: 'rep/weekWork/weekWork.html?ver='.toLowerCase() + ver,
            controller: 'weekWorkController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('weekWork'); // Resolve promise and load before view
                }]
            }
        })
        .when('/userWork'.toLowerCase(), {
            templateUrl: 'rep/userWork/userWork.html?ver='.toLowerCase() + ver,
            controller: 'userWorkController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('userWork'); // Resolve promise and load before view
                }]
            }
        })
        .when('/user', {
            templateUrl: 'admin/user/user.html?ver=' + ver,
            controller: 'userController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('user'); // Resolve promise and load before view
                }]
            }
        })
        .when('/user_role', {
            templateUrl: 'admin/user/user_role.html?ver=' + ver,
            controller: 'userRoleController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('userRole'); // Resolve promise and load before view
                }]
            }
        })
        .when('/role', {
            templateUrl: 'admin/role/role.html?ver=' + ver,
            controller: 'roleController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('role'); // Resolve promise and load before view
                }]
            }
        })
        .when('/role_right', {
            templateUrl: 'admin/role/role_right.html?ver=' + ver,
            controller: 'roleRightController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('roleRight'); // Resolve promise and load before view
                }]
            }
        })
        .when('/vacation', {
            templateUrl: 'calendar/vacation/vacation.html?ver=' + ver,
            controller: 'vacationController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('vacation'); // Resolve promise and load before view
                }]
            }
        })
        .when('/userVacation'.toLowerCase(), {
            templateUrl: 'rep/userVacation/userVacation.html?ver='.toLowerCase() + ver,
            controller: 'userVacationController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('userVacation'); // Resolve promise and load before view
                }]
            }
        })
        .when('/userPassword'.toLowerCase(), {
            templateUrl: 'admin/user/user_password.html?ver=' + ver,
            controller: 'userChangeController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('userPassword'); // Resolve promise and load before view
                }]
            }
        })
        .when('/user_info_type'.toLowerCase(), {
            templateUrl: 'admin/user/user_info_type.html?ver=' + ver,
            controller: 'userInfoTypeController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('userInfoType'); // Resolve promise and load before view
                }]
            }
        })
        .when('/message'.toLowerCase(), {
            templateUrl: 'admin/user/message.html?ver=' + ver,
            controller: 'messageController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('message'); // Resolve promise and load before view
                }]
            }
        })
        .when('/work_graph', {
            templateUrl: 'workGraph/workGraph.html?ver='.toLowerCase() + ver,
            controller: 'workGraphController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('workGraph'); // Resolve promise and load before view
                }]
            }
        })
        .when('/work_stage', {
            templateUrl: 'work/work_stage.html?ver='.toLowerCase() + ver,
            controller: 'workStageController',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('workStage'); // Resolve promise and load before view
                }]
            }
        })
        .when('/work_stage3', {
            templateUrl: 'work/work_stage3.html?ver='.toLowerCase() + ver,
            controller: 'workStage3Controller',
            resolve: {
                LazyLoadCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load('workStage3'); // Resolve promise and load before view
                }]
            }
        })
        .otherwise({
            redirectTo: '/'
        });
});