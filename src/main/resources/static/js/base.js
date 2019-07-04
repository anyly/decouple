$(document).ready(function () {
    var path = location.pathname;
    path = path.replace(/^\/files/, '');
    path = path==''?'/':path;
    $('.path').val(path);
});

function request(opt) {
    $.ajax(opt);
}

function parentPath() {
    var url = location.pathname.replace(/(^\/files\/?.*)\/(.+$)?/g, "$1");
    location.pathname = url;
}

function inputPath(event) {
    switch (event.keyCode) {
        case 13:
            event.preventDefault();
            applyPath();
            break;
    }
}

function applyPath() {
    var input = $('.path');
    var path = input.val();
    path = /^\//g.test(path)?path:'/'+path;
    path = path.replace(/\/+/g, '/');
    location.pathname = '/files' + path;
}