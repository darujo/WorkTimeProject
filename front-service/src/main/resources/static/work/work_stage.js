// var app = angular.module('workTimeService', ['dnd']);
//
// app.
angular.module('workTimeService').controller('workStageController', function ($scope, $http, $location) {
    // console.log(window.location)
    const constPatchReleaseStage = window.location.origin + '/work-service/v1/release/stage';
    let release ={works:null}
    console.log(release);
    $scope.loadRelease = function () {
        $location.parserFilter($scope.Filt);
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
        console.log("page");
        console.log(page);
        document.getElementById("Page").value = page;
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load = true;
            $scope.releaseList = null;
            console.log("отправляем запрос /releaseList");
            console.log($scope.Filt);
            if (typeof $scope.Filt === "undefined") {
                $scope.Filt = {size: 10};
            }
            let Filter = $scope.Filt;
            console.log(Filter)
            console.log(Filter.releaseId);

            $http({
                url: constPatchReleaseStage,
                method: "get",
                params: {
                    page: page,
                    size: Filter ? Filter.size : null,
                    name: Filter ? Filter.name : null,
                    codeSap: Filter ? Filter.codeSap : null,
                    codeZi: Filter ? Filter.codeZi : null,
                    task: Filter ? Filter.task : null,
                    releaseId: Filter.releaseId && Filter.releaseId[0] !== undefined ? Filter.releaseId : null,
                    sort: Filter ? Filter.sort : null,
                    stageZi: Filter ? Filter.stageZi : null
                }
            }).then(function (response) {
                console.log(response);
                $scope.releaseList = response.data;
                console.log($scope.WorkList);
                maxPage = response.data["totalPages"];
                $scope.load = false;
            }, function errorCallback(response) {
                maxPage = 1;
                console.log(response)
                $scope.load = false;
                if ($location.checkAuthorized(response)) {
                    alert(response.data.message);
                }

                // showFindTask();
            });
        }
    };

    let sendSave = false;
    let saveWork = async function (workId, releaseId, stageZi) {
        console.log("saveWork")
        console.log(workId);
        console.log(releaseId);
        console.log(stageZi);
        let flag;
        if (!sendSave) {
            sendSave = true;
            $http({
                url: constPatchReleaseStage + "/save",
                method: "get",
                params: {
                    workId: workId,
                    releaseId: releaseId,
                    stageZi: stageZi

                }
            }).then(function (response) {
                sendSave = false;
                console.log(response);
                flag = true;
            }, function errorCallback(response) {
                sendSave = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    alert(response.data.message);
                }
                flag = false;
            });
        }
        while (sendSave) {
            await wait();
        }
        return flag;
    };

    function wait() {
        return new Promise((resolve, reject) => {
            console.log(reject);
            setTimeout(() => {
                resolve('Timeout resolved');
            }, 10);
        });
    }

    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        $scope.Filt = {
            stageZi: 15,
            size: 10
        }
        console.log($scope.Filt);
        if (load) {
            $scope.filterWork();
        }
    }

    $location.getReleases().then(function (result) {
        $scope.ReleaseList = result;
        console.log("result releaseList");
        console.log(result);
    });

    $scope.drag = function (a) {
        console.log('drag start');
        console.log(a)
        this.active = undefined;
        // console.log('sort start');
    }

    $scope.dragend = function (dropModel, dragModel) {
        this.dropped = false;
        console.log('dragend');
        console.log(dragModel);
        console.log(dropModel);
        saveWork(dragModel.id, dropModel.release, dropModel.stage).then(function (result) {
            console.log("onSortEnd result");
            console.log(result)
            $scope.findPage(0);
        });

    }
    /*$scope.dragover = function () {
        console.log('dragover', arguments);
    };*/
    $scope.drop = function (relId, model) {
        this.dropped = model;

        // console.log(zzz)
        // a["id"] =relId;
        // a["stage"]= stage;
        console.log('drop');
        console.log(relId);
        console.log(model);
    }

    $scope.dragleave = function (relId, stage) {
        this.active = undefined;
        console.log("dragleave res")
        console.log(relId);
        console.log(stage);

        // console.log(work);
        // saveWork(work.id, relId, stage).then(function (result) {
        //     console.log("onSortEnd result");
        //     console.log(result)
        // });


        console.log('dragleave end');
        return false;
    }

    $scope.dragover = function (dragModel) {
        console.log("dragover res")
        console.log(dragModel);


        // console.log(work);


        console.log('dragover end');
        return false;
    }
    $scope.dragenter = function (dropModel, dragModel) {
        this.active = dropModel;
        console.log("dragenter res")
        console.log(dragModel);
        console.log(dropModel);

        // console.log(work);
        // saveWork(work.id, relId, stage).then(function (result) {
        //     console.log("onSortEnd result");
        //     console.log(result)
        // });


        console.log('dragenter end');
        return false;
    }
    $scope.dragstart = function () {
        console.log('dragstart');
    }
    $scope.isDropped = function (model) {
        return this.dropped === model;
    };

    $scope.isActive = function (model) {
        return this.active === model;
    };
    // ----------------------------------------------------------
    $scope.onSortStart = function () {
        console.log('sort start');
    }

    $scope.onSort = function () {
        console.log('sort');
    }
    $scope.onSortChange = function () {
         console.log('onSortChange');
    }


    $scope.onSortEnd = function () {
        console.log("onSortEnd res")
    }
    $scope.onSortEnter = function () {
        console.log('onSortEnter');
    }


    $scope.loadRelease();
    console.log("___________________________________________stage3________________________________________")
})