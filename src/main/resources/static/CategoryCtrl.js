app.controller('CategoryCtrl',  function($scope, $rootScope, Category, Media){
    $scope.id = undefined;
    $scope.media = undefined;
    $scope.categories = [];
    $scope.base = {"id":0,"name":"ROOT"};
    $scope.server = "http://faui21f.informatik.uni-erlangen.de:9090";

    $scope.start = function(){
        Category.get($scope.server,0).then(function(resp){
            $scope.base = {"id":0,"name":"ROOT"};
            $scope.base.children = resp.data;
            $scope.getChildren($scope.base);


        });
    }


    $scope.getChildren = function(c){
        Category.get($scope.server,c.id).then(function(resp){
            c.children = resp.data;
            for(i=0;i<c.children.length;i++){
                $scope.getChildren(c.children[i]);
            }
        });
    }

    $scope.new = function(){
        console.log($scope.parentid);
        if(window.base64 != undefined){
            Media.create($scope.server, window.filename, window.base64).then(function(resp){
                Category.create($scope.server,$scope.name, $scope.parentid, resp.data).then(function(resp){
                    $scope.clearAll();
                    $scope.start();
                });
            });
        }else{
            Category.create($scope.server,$scope.name, $scope.parentid, null).then(function(resp){
                $scope.clearAll();
                $scope.start();
            });
        }
    }

    $scope.update = function(){
        if($scope.id == undefined)
            return;


        if(window.base64 != undefined){
            Media.create($scope.server, window.filename, window.base64).then(function(resp){
                Category.update($scope.server, $scope.id, $scope.name, $scope.parentid, resp.data).then(function(resp){
                    $scope.clearAll();
                    $scope.start();
                })
            });
        }else{
            Category.update($scope.server, $scope.id, $scope.name, $scope.parentid, $scope.media).then(function(resp){
                $scope.clearAll();
                $scope.start();
            })
        }
    }

    $scope.setSub = function(id){
        $scope.parentid = id;
    }

    $scope.remove = function(id){
        Category.remove($scope.server, id).then(function(resp){
            $scope.start();
        });
    }

    $scope.edit = function(item){
        $scope.id = item.id;
        $scope.media = item.image;
        $scope.parentid = item.parent;
        $scope.name = item.name;
        if(item.image != undefined)
            document.getElementById("preview").src = item.image.lowRes;
    }

    $scope.clearAll = function(){
        $scope.id = undefined;
        $scope.media = undefined;
        $scope.parentid = 0;
        $scope.name = "";
        document.getElementById("preview").src = undefined;
        window.clearFile();
    }

    $scope.start();

});

app.directive("tree", function(RecursionHelper) {
    return {
        restrict: "E",
        scope: {family: '=', parent: '='},
        template:
        '<img ng-if="family.image!=null" style="width:50px;height:50px;margin-right:10px" src="{{family.image.lowRes}}">{{ family.name + "("+family.id+")"}}'+
        '&nbsp;<button ng-click="parent.setSub(family.id)">set parent</button>'+
        '&nbsp;<button ng-if="family.id!=0" ng-click="parent.remove(family.id)">delete</button>'+
        '&nbsp;<button ng-if="family.id!=0" ng-click="parent.edit(family)">edit</button>'+
        '<ul >' +
        '<li ng-repeat="child in family.children">' +
        '<tree family="child" parent="parent"></tree>' +
        '</li>' +
        '</ul>',
        compile: function(element) {
            return RecursionHelper.compile(element, function(scope, iElement, iAttrs, controller, transcludeFn, $rootScope){
                // Define your normal link function here.
                // Alternative: instead of passing a function,
                // you can also pass an object with
                // a 'pre'- and 'post'-link function.
            });
        }
    };
});