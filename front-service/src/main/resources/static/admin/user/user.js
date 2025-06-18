 angular.module('workTimeService').controller('userController', function ($scope, $http, $location) {

    const constPatchUser = window.location.origin + '/users';
    const constPatchAdmin = window.location.origin + '/admin/users';

    let showUsers = function () {
        document.getElementById("UserList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    let showFormEdit = function () {
        document.getElementById("UserList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
    };

    $scope.loadUser = function () {
        showUsers();
        $scope.findPage(0);
    };

    let maxPage = 1;
    $scope.findPage = function (diffPage) {
        console.log("findPage user");
        let page = parseInt(document.getElementById("Page").value) + diffPage;
        if (maxPage < page) {
            page = maxPage;
        }
        // должно быть после если maxPage = 0
        if (page < 1) {
            page = 1;
        }
        document.getElementById("Page").value = page;
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        }
        else {
            $scope.load = true;
            $scope.UserList = null;
            console.log("запрос данных");

            if (typeof $scope.Filt === "undefined") {
                $scope.Filt = {size: 10};
            }
            let Filter;
            Filter = $scope.Filt;
            console.log(Filter);
            $http({
                url: constPatchUser,
                method: "get",
                params: {
                    page: page,
                    size: Filter ? Filter.size : null,
                    nikName: Filter ? Filter.nikName : null,
                    password: Filter ? Filter.password : null,
                    lastName: Filter ? Filter.lastName : null,
                    firstName: Filter ? Filter.firstName : null,
                    patronymic: Filter ? Filter.patronymic : null
                }


            }).then(function (response) {
                $scope.load = false;
                console.log("response :");
                console.log(response);
                console.log("response,data :");
                console.log(response.data);
                if (typeof response.data.content === "undefined") {
                    $scope.UserList = response.data;
                    maxPage = 1;
                } else {
                    $scope.UserList = response.data.content;
                    maxPage = response.data.totalPages;
                }

                showUsers();

            }, function errorCallback(response) {
                $scope.load = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

            });
        }
    };
    $scope.filterUser = function () {
        console.log("filterUser")
        $location.saveFilter("userFilter", $scope.Filt);
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };

    $scope.createUser = function () {
        console.log("createUser");

        $scope.User = {
            id: null,
            nikName: null,
            password: null,
            passwordText: null,
            lastName: null,
            firstName: null,
            patronymic: null,
            passwordChange: true
        };

        console.log($scope.User);
        showFormEdit();

    };

    $scope.editUser = function (userId) {
        console.log("edit");
        $http.get(constPatchAdmin + "/edit/user/" + userId)
            .then(function (response) {
                $scope.User = response.data;
                console.log($scope.User);

                showFormEdit();

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };

    $scope.deleteUser = function (userId) {
        $http.delete(constPatchAdmin + "/edit/user/" + userId)
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
    $scope.saveUser = function () {
        console.log("saveUser");
        console.log($scope.User);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchAdmin + "/edit/user", $scope.User)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendSave = false;
                    showUsers();
                    $scope.loadUser();
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
        $scope.Filt = {
            size: 10
        };
        console.log($scope.Filt);
        if (load) {
            $scope.filterUser();
        }
    }
    $scope.editRole = function (userId){
        $location.path('/user_role' ).search({userId: userId});

    }

     $scope.genHash = function (){
         $http({
             url: constPatchAdmin + "/user/password/hash" ,
             method: "get",
             params: {
                 textPassword: $scope.User ? $scope.User.textPassword : null
             }


         }).then(function (response) {
             console.log("response :");
             console.log(response);
             $scope.User.userPassword= response.data.value;

         }, function errorCallback(response) {

             console.log(response)
             if ($location.checkAuthorized(response)) {
                     alert(response.data.message);
             }

         });


     }

    // $scope.UserList = $location.UserList;
    $scope.Filt = $location.getFilter("userFilter");
    if ($scope.Filt === null) {
        $scope.clearFilter(false);
    }
    console.log("Start");
    showUsers();
    console.log("Show ok");

    $scope.loadUser();
})