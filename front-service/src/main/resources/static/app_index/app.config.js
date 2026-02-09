angular.module('workTimeService').config(["$ocLazyLoadProvider", function ($ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
        'debug': true, // For debugging 'true/false'
        'events': true, // For Event 'true/false'
        'modules': [{ // Set modules initially
            name: 'agreement', // module
            files: ['workRate/agreement.js?ver=2.0'.toLowerCase()]
        }, {
            name: 'agreementRequest', // module
            files: ['workRate/request.js?ver=2.0'.toLowerCase()]
        }, {
            name: 'agreementResponse', // module
            files: ['workRate/response.js?ver='.toLowerCase()]
        }, {
            name: 'workTime', // module
            files: ['workTime/workTime.js?ver=2.0'.toLowerCase()]
        }, {
            name: 'workTimeRep', // module
            files: ['rep/workTimeRep.js?ver=2.0'.toLowerCase()]
        }, {
            name: 'factWork', // module
            files: ['rep/factWork.js?ver=2.0'.toLowerCase()]
        }, {
            name: 'work', // module
            files: ['work/work.js?ver=2.0']
        }, {
            name: 'welcome', // module
            files: ['welcome/welcome.js?ver=2.0']
        }, {
            name: 'release', // module
            files: ['work/release.js?ver=2.0']
        }, {
            name: 'task', // module
            files: ['task/task.js?ver=2.0']
        }, {
            name: 'workRate', // module
            files: ['workRate/workRate.js?ver=2.0'.toLowerCase()]
        }, {
            name: 'calendar', // module
            files: ['calendar/calendar.js?ver=2.0']
        }, {
            name: 'weekWork', // module
            files: ['rep/weekWork/weekWork.js?ver=2.0'.toLowerCase()]
        }, {
            name: 'userWork', // module
            files: ['rep/userWork/userWork.js?ver=2.0'.toLowerCase()]
        }, {
            name: 'user', // module
            files: ['admin/user/user.js?ver=2.0']
        }, {
            name: 'userRole', // module
            files: ['admin/user/user_role.js?ver=2.0']
        }, {
            name: 'role', // module
            files: ['admin/role/role.js?ver=2.0']
        }, {
            name: 'roleRight', // module
            files: ['admin/role/role_right.js?ver=2.0']
        }, {
            name: 'vacation', // module
            files: ['calendar/vacation/vacation.js?ver=2.0']
        }, {
            name: 'userVacation', // module
            files: ['rep/userVacation/userVacation.js?ver=2.0'.toLowerCase()]
        }, {
            name: 'userPassword', // module
            files: ['admin/user/user_password.js?ver=2.0']
        }, {
            name: 'userInfoType', // module
            files: ['admin/user/user_info_type.js?ver=2.0']
        }, {
            name: 'message', // module
            files: ['admin/user/message.js?ver=2.0']
        }, {
            name: 'workGraph', // module
            files: ['workGraph/workGraph.js?ver=2.0'.toLowerCase()]
        }, {
            name: 'workStage', // module
            files: ['work/work_stage.js?ver=2.0']
        }, {
            name: 'update', // module
            files: ['admin/user/update.js?ver=2.0']
        }, {
            name: 'project', // module
            files: ['project/project.js?ver=2.0']
        }, {
            name: 'project_edit', // module
            files: ['project/project_edit.js?ver=3.0']
        }, {
            name: 'rate', // module
            files: ['workRate/rate.js?ver=3.0'.toLowerCase()]
        }
        ]
    });
}]);