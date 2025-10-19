angular.module('workTimeService').controller('workStageController', function ($scope) {
    $scope.dragStart = function () {
        console.log("sssss");
            // ev.dataTransfer.effectAllowed = 'move';
            // ev.dataTransfer.setData("Text", ev.target.getAttribute('id'));
            // ev.dataTransfer.setDragImage(ev.target, 100, 100);
            return true;
        }
        $scope.dragEnter = function (ev) {
            ev.preventDefault();
            return true;
        }
        $scope.dragOver = function (ev) {
            ev.preventDefault();
        }
        $scope.dragDrop = function (ev) {
            var data = ev.dataTransfer.getData("Text");
            ev.target.appendChild(document.getElementById(data));
            ev.stopPropagation();
            return false;
        }
    }
)
// function dragStart($scope) {
//     console.log("sssss")
//     $scope.dragStart();
//     // ev.dataTransfer.effectAllowed='move';
//     // ev.dataTransfer.setData("Text", ev.target.getAttribute('id'));
//     // ev.dataTransfer.setDragImage(ev.target,100,100);
//     return true;
// }
function dragEnter(ev) {

    // ev.preventDefault();
    return true;
}
function dragOver(ev) {
    // ev.preventDefault();
}
function dragDrop(ev) {
    // var data = ev.dataTransfer.getData("Text");
    // ev.target.appendChild(document.getElementById(data));
    // ev.stopPropagation();
    return false;
}

        
