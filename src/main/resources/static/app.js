var app = angular.module("app",['RecursionHelper']);


app.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
}]);

app.factory('Category', ['$http', function ($http, $scope) {
    return {
        get: function(server, id) {
            return $http.get(server+'/v1/categories/'+id+'/subcategories' );
        },
        create: function(server, name, parent, image){
            return $http.post(server+'/v1/categories/', {id: null, parent: parent,name: name, image: image} );
        },
        remove: function(server, id){
            return $http({
                method: 'DELETE',
                url: server+"/v1/categories/"+id,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        },
        update: function(server, id, name, parent, image){
            return $http.put(server+'/v1/categories/'+id, {id:id, name:name, parent:parent, image:image} );
        }
    };
}]);

app.factory('Media', ['$http', function ($http, $scope) {
    return {
        create: function(server, filename, base64){
            return $http({
                method: 'POST',
                url: server+"/v1/media",
                data: "filename="+encodeURIComponent(filename)+"&base64="+encodeURIComponent(base64),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        }
    };
}]);

app.factory('Password', ['$http', function ($http, $scope) {
    return {
        reset: function(server, token, password){
            return $http({
                method: 'POST',
                url: server+"/v1/users/password",
                data: "token="+encodeURIComponent(token)+"&password="+encodeURIComponent(password),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        }
    };
}]);

