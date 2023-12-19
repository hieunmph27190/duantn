angular.module('myApp', []).controller('myCtrl', function($scope,$http) {
    $http.get("/ban-hang/product").then(
        function(response) {
            $scope.products = response.data.data;
        },
        function(error) {
            alert('Error:'+ error);
        }
    );
});
