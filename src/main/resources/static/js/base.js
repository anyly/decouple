$(document).ready(function () {
    var path = location.pathname;
    var reg = new RegExp("^(?:"+regManager()+")", 'g');
    path = path.replace(reg, '');
    path = path==''?'/':path;
    $('.path').val(path);
});
function regManager() {
    var pattern = '';
    for (var name in fileSupport) {
        pattern += fileSupport[name].manager+'|';
    }
    pattern += httpManager;
    return pattern;
}

function request(opt) {
    $.ajax(opt);
}

function parentPath() {
    var reg = RegExp("^(?:"+regManager()+")(/?.*)/(.+$)?", 'g');
    var url = location.pathname.replace(reg, "$1");
    location.pathname = httpManager+url;
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