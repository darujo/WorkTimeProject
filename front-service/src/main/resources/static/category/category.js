angular.module('workTimeService').controller('categoryController', function ($scope, $http, $location) {

    const constPatchCategory = window.location.origin + '/categories';
    const constPatchAdmin = window.location.origin + '/admin/categories';
    $scope.loadCategory = function () {
        $scope.findPage(0);
    };

    $scope.categoryDtoList = null;
    $scope.findPage = function () {
        console.log("findPage");
        console.log("запрос данных проектов");
        if ($scope.load) {
            alert("Подождите обрабатывается предыдущий запрос")
        } else {
            $scope.load = true;
            $scope.CategoryList = null;
            let Filter;
            Filter = $scope.Filt;
            console.log(Filter);
            $http({
                url: constPatchCategory,
                method: "get",
                params: {
                    name: Filter ? Filter.name : null

                }


            }).then(function (response) {
                console.log("response :");
                console.log(response);
                console.log("response,data :");
                console.log(response.data);
                if (response.data._embedded && response.data._embedded.categoryDtoList) {
                    $scope.CategoryList = response.data._embedded.categoryDtoList;
                }
                $scope.load = false;
            }, function errorCallback(response) {
                $scope.load = false;
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    //     alert(response.data.message);
                }

            });
        }
    };
    $scope.filterCategory = function () {
        console.log("filterCategory")
        $location.saveFilter("categoryFilter", $scope.Filt);
        $scope.findPage();
    };

    $scope.createCategory = function () {
        console.log("createCategory");
        $location.path('/categoryEdit').search({categoryId: null});
    };

    $scope.editCategory = function (categoryId) {
        console.log("edit");
        $location.path('/categoryEdit').search({categoryId: categoryId});
    };

    $scope.deleteCategory = function (categoryId) {
        $http.delete(constPatchAdmin + "/" + categoryId)
            .then(function (response) {
                console.log("Delete response")
                console.log(response);
                $scope.loadProject();
            }, function errorCallback(response) {
                console.log(response)
                if ($location.checkAuthorized(response)) {
                    alert(response.data.message);
                }
            });
    };

    $scope.clearFilter = function (load) {
        console.log("clearFilter");
        if (!load) {
            $scope.Filt = {};
        }
        console.log($scope.Filt);
        if (load) {
            $scope.filterRole();
        }
    }

    $scope.Filt = $location.getFilter("categoryFilter");

    $scope.clearFilter(false);
    console.log("Start");
    console.log("Show ok");

    $scope.loadCategory();
})