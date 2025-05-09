angular.module('workTimeService').controller('vacationController', function ($scope, $http, $location) {

    const constPatchVacation = window.location.origin + '/calendar-service/v1/vacation';

    let showList = function () {
        document.getElementById("VacationList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    let showFormEdit = function () {
        document.getElementById("VacationList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
    };

    $scope.loadVacation = function () {
        showList();
        $scope.findPage(0);
    };

    let maxPage = 1;
    $scope.findPage = function (diffPage) {
        // console.log("findPage");
        // let page = parseInt(document.getElementById("Page").value) + diffPage;
        // if (maxPage < page) {
        //     page = maxPage;
        // }
        // // должно быть после если maxPage = 0
        // if (page < 1) {
        //     page = 1;
        // }
        // document.getElementById("Page").value = page;
        // console.log("запрос данных");

        if (typeof $scope.Filt === "undefined") {
            $scope.Filt = {size: 10};
        }
        let Filter;
        Filter = $scope.Filt;
        console.log(Filter);
        $http({
            url: constPatchVacation,
            method: "get"
            // ,
            // params: {
            //     page: page,
            //     size: Filter ? Filter.size : null,
            //     nikName: Filter ? Filter.nikName : null,
            //     password: Filter ? Filter.password : null,
            //     lastName: Filter ? Filter.lastName : null,
            //     firstName: Filter ? Filter.firstName : null,
            //     patronymic: Filter ? Filter.patronymic : null
            // }


        }).then(function (response) {
            console.log("response,data :");
            console.log(response.data);
            if (typeof response.data.content === "undefined") {
                $scope.VacationList = response.data;
                maxPage = 1;
            } else {
                $scope.VacationList = response.data.content;
                // maxPage = response.data.totalPages;
            }

            showList();

        }, function errorCallback(response) {
            console.log(response)
            if ($location.checkAuthorized(response)) {
                //     alert(response.data.message);
            }

        });

    };
    $scope.filterVacation = function () {
        console.log("filterVacation")
        $location.saveFilter("vacationFilter", $scope.Filt);
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };

    $scope.createVacation = function () {
        console.log("createVacation");

        $scope.Vacation = {
            id: null,
            nikName: null,
            dateStart: null,
            dateEnd: null,
            days: null
        };

        console.log($scope.Vacation);
        showFormEdit();

    };

    $scope.editVacation = function (vacationId) {
        console.log("edit");
        $http.get(constPatchVacation + "/" + vacationId)
            .then(function (response) {
                $scope.Vacation = response.data;
                console.log($scope.Vacation);
                 $scope.Vacation.dateStart = new Date( $scope.Vacation.dateStart);
                 $scope.Vacation.dateEnd = new Date( $scope.Vacation.dateEnd);
                showFormEdit();

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };

    $scope.deleteVacation = function (vacationId) {
        $http.delete(constPatchVacation + "/" + vacationId)
            .then(function (response) {
                console.log("Delete response")
                console.log(response);
                $scope.loadVacation();
            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };
    let sendSave = false;
    $scope.saveVacation = function () {
        console.log("saveVacation");
        console.log($scope.Vacation);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchVacation + "", $scope.Vacation)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendSave = false;
                    showList();
                    $scope.loadVacation();
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
        // $scope.Filt = {
        //     size: 10
        // };
        console.log($scope.Filt);
        if (load) {
            $scope.filterVacation();
        }
    }

    // $scope.UserList = $location.UserList;
    $scope.Filt = $location.getFilter("vacationFilter");
    if ($scope.Filt === null) {
        $scope.clearFilter(false);
    }

    let callBackUser = function (response) {
        console.log("callBackUser");
        console.log(response);
        $scope.UserList = response;
    }

    $location.getUsers(callBackUser);
    console.log("Start");
    showList();
    console.log("Show ok");

    $scope.loadVacation();
})