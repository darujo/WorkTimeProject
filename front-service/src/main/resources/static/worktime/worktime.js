angular.module('market').controller('productController', function ($scope, $http, $location, $localStorage) {

    const constPatchProduct = 'http://localhost:5555/product-service/v1';
    const constPatchBasket  = 'http://localhost:5555/basket-service/v1/baskets';
    var showProducts = function () {
        document.getElementById("ProductList").style.display = "block";
        document.getElementById("FormEdit").style.display = "none";
    };
    var showFormEdit = function () {
        document.getElementById("ProductList").style.display = "none";
        document.getElementById("FormEdit").style.display = "block";
    };



    $scope.loadProducts = function () {
        $scope.findPage(0);
    };
    var Price;
    $scope.findPage = function (diffPage) {
        var page = parseInt(document.getElementById("Page").value) + diffPage;
        document.getElementById("Page").value = page;
        $http({
            url: constPatchProduct + "/products",
            method: "get",
            params: {
                page: page,
                size: 10,
                min: Price ? Price.min : null,
                max: Price ? Price.max : null

            }
        }).then(function (response) {
            $scope.ProductList = response.data.content;
            showProducts();
        });

    };
    $scope.filterPrice = function () {
        Price = $scope.Price;
        document.getElementById("Page").value = "1";
        $scope.findPage(0);
    };

    var ProductIdEdit = null;

    $scope.createProduct = function () {
        ProductIdEdit = null;
        document.getElementById("ProductName").value = "";
        document.getElementById("ProductPrice").value = 0;
        $scope.Product.id = null;
        showFormEdit();

    };

    $scope.editProduct = function (productId) {
        $http.get(constPatchProduct + "/products/" + productId)
            .then(function (response) {
                ProductIdEdit = response.data.id;
                $scope.Product = response.data;
                console.log($scope.Product);

                document.getElementById("ProductName").value = response.data.title;
                document.getElementById("ProductPrice").value = response.data.price;
                showFormEdit();
            });
    };
    $scope.deleteProduct = function (productId) {
        $http.delete(constPatchProduct + "/products/" + productId)
            .then(function (response) {
                $scope.loadProducts();
            });
    };
    $scope.saveProduct = function () {
        console.log($scope.Product);
        console.log(ProductIdEdit);

        $http.post(constPatchProduct + "/products",$scope.Product)
            .then(function (response) {
                $scope.loadProducts();
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
            alert("Продукт добавлен в корзину")
       });

    };
       $scope.loadProducts();
})