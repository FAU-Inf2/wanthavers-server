app.controller('PasswordCtrl',  function($scope, Password){
    //$scope.server = "http://faui21f.informatik.uni-erlangen.de:9090";
    $scope.server = "http://localhost:8080";
    
    $scope.save = function(){
        $scope.error = false;
        $scope.pwdNotEqual = false;
        $scope.success = false;

        if($scope.password1 != $scope.password2){
            $scope.pwdNotEqual = true;
            return;
        }

        var token = location.hash.replace("#","");

        Password.reset($scope.server, token, $scope.password1).success(function(){
            $scope.success = true;
        }).error(function(){
            $scope.error = true;
        });
    }
});
