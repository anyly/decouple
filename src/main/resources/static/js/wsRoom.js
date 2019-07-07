$(document).ready(function () {
    var support = fileSupport['ws'];
    var newUrl = location.pathname.replace(support.manager, support.api);
    newUrl = location.protocol+'//'+location.host + newUrl
    $('#goto').attr('href', newUrl).html(newUrl);


    $('.submit').click(function() {
        var name = $('#name').val();
        var room = $('.list>.item[name="'+name+'"]');
        if (room.length) {
            checkedItem(room);
            return;
        }
        room = roomDom(name);
        checkedItem(room);
        $('.list').append(room);
    });
});

function initData(data) {
}
function checkedItem($this) {
    $('.list>.item').removeClass('checked');
    var room = $this instanceof jQuery?$this:$($this);
    room.addClass('checked');
}
function roomDom(name) {
    return $('<li class="item" name="'+name+'" onclick="checkedItem(this)">\
                          <p class="name">'+name+'</p>\
                          <a href="?room='+name+'">进入房间</a>\
                      </li>'
    );
}