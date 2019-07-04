$(document).ready(function () {
    var path = location.pathname;
    path = path.replace(/^\/files/, '');
    path = path==''?'/':path;
    $('.path').html(path);
});

function request(opt) {
    $.ajax(opt);
}

function prev() {
    var url = location.pathname.replace(/(^\/files\/?.*)\/(.+$)?/g, "$1");
    location.pathname = url;
}