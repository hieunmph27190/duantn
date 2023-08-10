angular.module('myApp', []).controller('myCtrl', function($scope) {
    $scope.showProductDetails = function(product) {
        $scope.selectedProduct = product;
    };
});
