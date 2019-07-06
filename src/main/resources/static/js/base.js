$(document).ready(function () {
    var path = location.pathname;
    var reg = new RegExp("^(?:"+httpManager+"|"+wsManager+")", 'g');
    path = path.replace(reg, '');
    path = path==''?'/':path;
    $('.path').val(path);
});

function request(opt) {
    $.ajax(opt);
}

function parentPath() {
    var reg = RegExp("(^(?:"+httpManager+"|"+wsManager+")/?.*)/(.+$)?", 'g');
    var url = location.pathname.replace(reg, "$1");
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
    location.pathname = httpManager + path;
}