angular.module('workTimeService').controller('weekWorkController', function ($scope, $http, $location, $localStorage) {

    const constPatchWork = window.location.origin + '/work-service/v1/works';
    $scope.clearFilter =function (load){
        console.log("clearFilter");
        $scope.Filt  = {    dateStart: new Date(),
            dateEnd: new Date(),
            weekSplit: true,
            workTask: true,
            workTime: true,
            workPercent: true,
            stageZi: 15,
            page: 1,
            size: 10
        };
        console.log($scope.Filt);
        if(load){
            $scope.filterWorkTime();
            $scope.Filt.page = 1;
        }
    }

    $scope.loadWorkTime = function () {
        console.log("loadWorkTime");
        console.log($scope.Filt);

        $scope.findPage(0);
    };

    let maxPage;
    $scope.findPage = function (diff) {
        console.log("findPage");
        console.log($scope.Filt.page);
        let page = parseInt($scope.Filt.page) + diff;
        if (page > maxPage) {
            page = maxPage;

        }
        if (page < 1) {
            page = 1;
        }
        $scope.Filt.page = page;
        let Filt = $scope.Filt;

        $http({
            url: constPatchWork + "/rep/fact/week",
            method: "get",
            params: {
                nikName: Filt ? Filt.nikName : null,
                dateStart: Filt ? Filt.dateStart : new Date(),
                dateEnd: Filt ? Filt.dateEnd : new Date(),
                weekSplit: Filt ? Filt.weekSplit : null,
                ziSplit: Filt ? Filt.ziSplit : null,
                addTotal: Filt ? Filt.addTotal : null,
                //ЗИ
                page: Filt ? Filt.page :1,
                size: Filt ? Filt.size :10,
                name: Filt ? Filt.name : null,
                task: Filt ? Filt.task : null,
                codeSap: Filt ? Filt.codeSap : null,
                codeZi: Filt ? Filt.codeZi : null,
                stageZi: Filt ? Filt.stageZi : null,
                release: Filt ? Filt.release : null
            }
        }).then(function (response) {
            console.log(response.data);
            $scope.WeekWorkList = response.data;
            $scope.ziSplit = Filt.ziSplit;
        }, function errorCallback(response) {
            console.log(response)
            if($location.checkAuthorized(response)){
                //     alert(response.data.message);
            }
        });
    };

    $scope.filterWorkTime = function () {
        console.log("filterWorkTime");
        $scope.Filt.page = 1;
        $scope.findPage(0);
    };
    let callBackType = function (response) {
        console.log("TaskListType");
        console.log(response);
        $scope.TaskListType = response.data;
    }
    $scope.searchJson = function (list, searchField, searchVal, resultField ){
        // console.log("searchJson");
        // console.log(list);
        // console.log(searchField);
        // console.log(searchVal);
        // console.log(resultField);
        for (let i=0 ; i < list.length ; i++)
        {
            if (list[i][searchField] === searchVal) {
                return list[i][resultField]
            }
        }
    }

    $location.getCode ("task/code/type",callBackType);
    $scope.clearFilter (false);
    $scope.UserList = $location.UserList;
    $scope.loadWorkTime();
})