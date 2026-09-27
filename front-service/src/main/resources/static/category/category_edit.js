angular.module('workTimeService').controller('categoryEditController', function ($scope, $http, $location) {
    const constPatchAdmin = window.location.origin + '/admin/categories';

    $scope.backCategory = function () {
        $location.path('/category');
    };


    let create = function () {
        console.log("createCategory");
        $scope.Category = {
            id: null,
            name: null,
            amount: null
        };
        console.log($scope.Category);
    };

    let edit = function (categoryId) {
        console.log("edit");
        $http.get(constPatchAdmin + "/" + categoryId)
            .then(function (response) {
                $scope.Category = response.data;
                console.log($scope.Category);

            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }
            });
    };

    let sendSave = false;
    $scope.saveCategory = function () {
        console.log("saveCategory");
        console.log($scope.Category);
        if (!sendSave) {
            sendSave = true;
            $http.post(constPatchAdmin, $scope.Category)
                .then(function (response) {
                    console.log("Save response")
                    console.log(response);
                    sendSave = false;
                    $scope.backCategory();
                }, function errorCallback(response) {
                    sendSave = false;
                    console.log(response.data);
                    if ($location.checkAuthorized(response)) {

                        alert(response.data.message);
                    }
                });
        }
    }
    console.log("Show ok");
    let Filter = {};
    $location.parserFilter(Filter);

    console.log("category_edit")
    console.log(Filter);
    if (Filter.categoryId === undefined) {
        create();
    } else {
        edit(Filter.categoryId);
    }
})