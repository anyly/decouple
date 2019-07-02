var hostname = 'http://localhost:8080';

function request(opt) {
    opt.url = hostname + opt.url;
    $.ajax(opt);
}