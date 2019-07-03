var hostname = 'http://localhost:8080';

function request(opt) {
    opt.url = hostname + opt.url;
    $.ajax(opt);
}

function prev() {
    var url = location.pathname.replace(/(^\/files\/?.*)\/(.+$)?/g, "$1");
    location.pathname = url;
}