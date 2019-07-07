$(document).ready(function () {
(function() {
    var support = fileSupport['ws'];
    var newText = location.pathname.replace('/wsRoom', support.api);
    var newUrl = location.pathname.replace(support.manager, '/wsRoom');
    newText = location.protocol+'//'+location.host + newText;
    newUrl = location.protocol+'//'+location.host + newUrl;
    $('#goto').attr('href', newUrl).html(newText);
})();

$('.submit').click(function() {
    var usecase = $('#usecase').val();
    var room = $('.list>.item[usecase="'+usecase+'"]');
    if (room.length) {
        checkedItem(room);
        return;
    }
    room = roomDom(usecase);
    checkedItem(room);
    $('.list').append(room);
});




});

function WebSocketClose(event) {
    alert(event.data);
};
function WebSocketClose(event) {
    alert(event.data);
};
function WebSocketClose(event) {
    alert(event.data);
};

function initData(data) {
}
function checkedItem($this) {
    $('.list>.item').removeClass('checked');
    var room = $this instanceof jQuery?$this:$($this);
    room.addClass('checked');
}
function roomDom(usecase) {
    return $('<li class="item" usecase="'+usecase+'" onclick="checkedItem(this)">\
                          <p class="name">'+usecase+'</p>\
                          <a href="javascript:void(0);" onclick="entryRoom(this)">进入房间</a>\
                      </li>'
    );
}
function entryRoom($this) {
    var usecase = $($this).parents('.item').attr('usecase');
    var name = $('#name').val();
    if (name=='') {
        alert('请输入昵称！');
        setTimeout(function() {
            $('#name').select();
        }, 0);
        return;
    }
    window.open('?name='+name+'&usecase='+usecase);
    //location.search = '?name='+name+'&usecase='+usecase;
}