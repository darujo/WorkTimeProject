angular.module('workTimeService').controller('releaseController', function ($scope, $http, $location) {

    const constPatchRelease = window.location.origin + '/work-service/v1/release';

    let showRelease = function () {
        document.getElementById("ReleaseList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    let showFormEdit = function () {
        document.getElementById("ReleaseList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
    };

    $scope.loadRelease = function () {
        showRelease();
        $scope.findPage(0);
    };
    $scope.Release = {
        issuingReleasePlanStr: null,
        issuingReleaseFactStr: null
    };
    $scope.Release = null;
    let maxPage = 1;
    $scope.findPage = function (diffPage) {
        console.log(diffPage);
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load = true;
            $scope.ReleaseList = null;
            if (typeof $scope.Filt === "undefined") {
                $scope.Filt = {size: 10};
            }
            let Filter;
            Filter = $scope.Filt;
            console.log(Filter);
            $http({
                url: constPatchRelease,
                method: "get"


            }).then(function (response) {
                $scope.load = false;
                console.log("response,data :");
                console.log(response.data);
                if (typeof response.data.content === "undefined") {
                    $scope.ReleaseList = response.data;
                    maxPage = 1;
                } else {
                    $scope.ReleaseList = response.data.content;
                    // maxPage = response.data.totalPages;
                }

                showRelease();

            }, function errorCallback(response) {
                $scope.load = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

            });
        }
    };
    $scope.filterRelease = function () {
        console.log("filterUser")
        $location.saveFilter("releaseFilter", $scope.Filt);
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };

    $scope.createRelease = function () {
        console.log("createRelease");

        $scope.Release = {
            id: null,
            name: null,
            password: null,
            issuingReleasePlan: null,
            issuingReleaseFact: null
        };

        console.log($scope.Release);
        showFormEdit();

    };

    $scope.editRelease = function (releaseId) {
        console.log("edit");
        $http.get(constPatchRelease + "/" + releaseId)
            .then(function (response) {
                $scope.Release = response.data;
                console.log($scope.Release);
                $scope.Release.issuingReleaseFact = new Date($scope.Release.issuingReleaseFact);
                $scope.Release.issuingReleasePlan = new Date($scope.Release.issuingReleasePlan);
                showFormEdit();

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };

    $scope.deleteRelease = function (releaseId) {
        $http.delete(constPatchRelease + "/" + releaseId)
            .then(function (response) {
                console.log("Delete response")
                console.log(response);
                $scope.loadUser();
            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };
    let sendSave = false;
    $scope.saveRelease = function () {
        console.log("saveRelease");
        console.log($scope.Release);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchRelease + "", $scope.Release)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendSave = false;
                    showRelease();
                    $scope.loadRelease();
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log(response.data);
                    if ($location.checkAuthorized(response)) {

                        alert(response.data.message);
                    }
                });
        }
    }

    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        console.log($scope.Filt);
        if (load) {
            $scope.filterRelease();
        }
    }

    $scope.Filt = $location.getFilter("releaseFilter");
    if ($scope.Filt === null) {
        $scope.clearFilter(false);
    }
    console.log("Start");
    showRelease();
    console.log("Show ok");

    $scope.loadRelease();
})