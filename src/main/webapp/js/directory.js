$(document).ready(function () {

});

function prev() {
    var url = location.pathname.replace(/(^\/manager\/?.*)\/(.+$)?/g, "$1");
    location.pathname = url;
}

function entry(sub) {
    location.pathname += '/'+sub;
}