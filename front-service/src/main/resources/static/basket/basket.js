angular.module('market').controller("basketController", function ($scope, $http, $localStorage) {
    const constPatchBasket  = 'http://localhost:5555/basket-service/v1/baskets';
    const constPatchOrder   = 'http://localhost:5555/order-service/v1';

    $scope.loadBasket = function () {
        $http.get(constPatchBasket)
            .then(function (response) {
                $scope.Basket = response.data;
            });

    };
    $scope.addProductToBasket = function (productId) {
        $http({
            url: constPatchBasket + "/add",
            method: "GET",
            params: {
                productId: productId
            }
        }).then(function (response) {
            $scope.loadBasket();
        });

    };
    $scope.delProductToBasket = function (productId) {
        console.log(productId);
        $http({
            url: constPatchBasket + "/delete",
            method: "GET",
            params: {
                productId: productId
            }
        }).then(function (response) {
            $scope.loadBasket();
        });

    };
    $scope.clearBasket = function () {
        $http({
            url: constPatchBasket + "/clear",
            method: "GET"
        }).then(function (response) {
            $scope.loadBasket();
        });
    };
     $scope.saveOrder = function () {
        $http.get(constPatchOrder+ "/orders/save")
            .then(function (response) {
                $scope.Basket = null;
                alert("Заказ оформлен")
            });

    };


    $scope.loadBasket();

})