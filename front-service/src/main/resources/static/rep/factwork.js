angular.module('workTimeService').controller('workFactRepController', function ($scope, $http, $location, $localStorage) {

    const constPatchWork = window.location.origin + '/work-service/v1';

    var Filt;
    $scope.loadWorkTime = function () {
        console.log("qqqqqqqq 2");
        $scope.findPage();
    };

    $scope.filterWork = function () {
        Filt = $scope.Filt;
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };
    var maxpage = 1;
    $scope.findPage = function (diffPage) {
        console.log(diffPage)
        var page = parseInt(document.getElementById("Page").value) + diffPage;
        if (page > maxpage) {
            page = maxpage;

        }
        if (page < 1) {
            page = 1;
        }
        document.getElementById("Page").value = page
        console.log("page");
        console.log(page);
        document.getElementById("Page").value = page;

        $http({
            url: constPatchWork + "/works/rep/factwork",
            method: "get",
            params: {
                userName: Filt ? Filt.userName : null,
                page: page,
                size: Filt ? Filt.size : null,
                name: Filt ? Filt.name : null,
                codeSap: Filt ? Filt.codeSap : null,
                codeZi: Filt ? Filt.codeZi : null,
                task: Filt ? Filt.task : null,
                release: Filt ? Filt.release :null,
                sort: Filt ? Filt.sort : null,
                stageZi: Filt ? Filt.stageZi : null,
                hideNotTime: Filt ? Filt.hideNotTime : null
            }
        }).then(function (response) {
            console.log(response.data);
            $scope.WorkTimeList = response.data.content;
            maxpage = response.data.totalPages;
        }, function errorCallback(response) {
            console.log(response)
            if($location.checkAuthorized(response)){
                //     alert(response.data.message);
            }
        });
    };
    document.getElementById("Page").value = "1";
    $scope.Filt = {
        stageZi: 15,
        size: 10,
        hideNotTime: true
    }
    $scope.UserList = $location.UserList;
    $scope.filterWork();
})