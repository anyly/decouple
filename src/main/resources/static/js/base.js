
function request(opt) {
    $.ajax(opt);
}

function prev() {
    var url = location.pathname.replace(/(^\/files\/?.*)\/(.+$)?/g, "$1");
    location.pathname = url;
}