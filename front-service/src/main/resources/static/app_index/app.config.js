angular.module('workTimeService').config(["$ocLazyLoadProvider", function ($ocLazyLoadProvider) {
    let ver = "3.1"
    $ocLazyLoadProvider.config({
        'debug': true, // For debugging 'true/false'
        'events': true, // For Event 'true/false'
        'modules': [{ // Set modules initially
            name: 'agreement', // module
            files: ['workRate/agreement.js?ver='.toLowerCase() + ver]
        }, {
            name: 'agreementRequest', // module
            files: ['workRate/request.js?ver='.toLowerCase() + ver]
        }, {
            name: 'agreementResponse', // module
            files: ['workRate/response.js?ver='.toLowerCase() + ver]
        }, {
            name: 'workTime', // module
            files: ['workTime/workTime.js?ver='.toLowerCase() + ver]
        }, {
            name: 'workTimeRep', // module
            files: ['rep/workTimeRep.js?ver='.toLowerCase() + ver]
        }, {
            name: 'factWork', // module
            files: ['rep/factWork.js?ver='.toLowerCase() + ver]
        }, {
            name: 'work', // module
            files: ['work/work.js?ver=' + ver]
        }, {
            name: 'welcome', // module
            files: ['welcome/welcome.js?ver=' + ver]
        }, {
            name: 'release', // module
            files: ['work/release.js?ver=' + ver]
        }, {
            name: 'task', // module
            files: ['task/task.js?ver=' + ver]
        }, {
            name: 'workRate', // module
            files: ['workRate/workRate.js?ver='.toLowerCase() + ver]
        }, {
            name: 'calendar', // module
            files: ['calendar/calendar.js?ver=' + ver]
        }, {
            name: 'weekWork', // module
            files: ['rep/weekWork/weekWork.js?ver='.toLowerCase() + ver]
        }, {
            name: 'userWork', // module
            files: ['rep/userWork/userWork.js?ver='.toLowerCase() + ver]
        }, {
            name: 'user', // module
            files: ['admin/user/user.js?ver=' + ver]
        }, {
            name: 'userRole', // module
            files: ['admin/user/user_role.js?ver=' + ver]
        }, {
            name: 'role', // module
            files: ['admin/role/role.js?ver=' + ver]
        }, {
            name: 'roleRight', // module
            files: ['admin/role/role_right.js?ver=' + ver]
        }, {
            name: 'vacation', // module
            files: ['calendar/vacation/vacation.js?ver=' + ver]
        }, {
            name: 'userVacation', // module
            files: ['rep/userVacation/userVacation.js?ver='.toLowerCase() + ver]
        }, {
            name: 'userPassword', // module
            files: ['admin/user/user_password.js?ver=' + ver]
        }, {
            name: 'userInfoType', // module
            files: ['admin/user/user_info_type.js?ver=' + ver]
        }, {
            name: 'message', // module
            files: ['admin/user/message.js?ver=' + ver]
        }, {
            name: 'workGraph', // module
            files: ['workGraph/workGraph.js?ver='.toLowerCase() + ver]
        }, {
            name: 'workStage', // module
            files: ['work/work_stage.js?ver=' + ver]
        }, {
            name: 'update', // module
            files: ['admin/user/update.js?ver=' + ver]
        }, {
            name: 'project', // module
            files: ['project/project.js?ver=' + ver]
        }, {
            name: 'project_edit', // module
            files: ['project/project_edit.js?ver=' + ver]
        }, {
            name: 'rate', // module
            files: ['workRate/rate.js?ver='.toLowerCase() + ver]
        }
        ]
    });
}]);