angular.module('workTimeService').controller('workFactRepController', function ($scope, $http, $location) {

    const constPatchWork = window.location.origin + '/work-service/v1';
    $scope.work = {
        num: null,
        timeAnalise: null,
        timeDevelop: null,
        timeDebug: null,
        timeRelease: null,
        timeOPE: null,
        timeWender: null
    }

    $scope.loadWork = function () {
        console.log("loadWorkTime");
        $location.parserFilter($scope.Filt);
        console.log($scope.Filt);

        $scope.filterWork();
    };
    $scope.sendFilter = function () {
        $scope.Filt["hideNotTime"] = $scope.Filt.hideNotTime;
        $location.sendFilter(location.hash, $scope.Filt);
    }
    $scope.filterWork = function () {
        $location.saveFilter("factWorkFilter", $scope.Filt);
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };
    let maxPage = 1;
    $scope.findPage = function (diffPage) {
        console.log(diffPage)
        let page = parseInt(document.getElementById("Page").value) + diffPage;
        if (page > maxPage) {
            page = maxPage;

        }
        if (page < 1) {
            page = 1;
        }
        document.getElementById("Page").value = page
        console.log("page");
        console.log(page);
        document.getElementById("Page").value = page;
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        }
        else {
            $scope.load = true;
            $scope.WorkTimeList = null;
            let Filter;
            Filter = $scope.Filt;

            $http({
                url: constPatchWork + "/works/rep/work/fact",
                method: "get",
                params: {
                    nikName: Filter ? Filter.nikName : null,
                    page: page,
                    size: Filter ? Filter.size : null,
                    name: Filter ? Filter.name : null,
                    codeSap: Filter ? Filter.codeSap : null,
                    codeZi: Filter ? Filter.codeZi : null,
                    task: Filter ? Filter.task : null,
                    releaseId: Filter ? Filter.releaseId : null,
                    sort: Filter ? Filter.sort : null,
                    stageZi: Filter ? Filter.stageZi : null,
                    hideNotTime: Filter ? Filter.hideNotTime : null
                }
            }).then(function (response) {
                $scope.load = false;
                console.log(response.data);
                $scope.WorkTimeList = response.data.content;
                maxPage = response.data["totalPages"];
            }, function errorCallback(response) {
                $scope.load = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
        }
    };

    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        if ($scope.Filt === null || load) {
            $scope.Filt = {
                stageZi: 15,
                size: 10,
                hideNotTime: true
            }
        } else {
            $scope.Filt["stageZi"] = $scope.Filt.stageZi ? $scope.Filt.stageZi : 15;
            $scope.Filt["size"] = $scope.Filt.size ? $scope.Filt.size : 10;
            $scope.Filt["hideNotTime"] = $scope.Filt.hideNotTime ? $scope.Filt.hideNotTime : true;
        }
        console.log($scope.Filt);
        if (load) {
            $scope.filterWork();
        }
    }
    document.getElementById("Page").value = "1";
    $scope.Filt = $location.getFilter("factWorkFilter");

    $scope.clearFilter(false);

    $location.getUsers().then(function (result) {
        $scope.UserList = result;
        console.log("result UserList");
        console.log(result);
    });

    $location.getReleases().then(function (result) {
        $scope.ReleaseList = result;
        console.log("result releaseList");
        console.log(result);
    });

    // $scope.loadWork();
})