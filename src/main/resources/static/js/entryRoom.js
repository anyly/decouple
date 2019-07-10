$(document).ready(function () {
(function() {
    window.wsURL = "ws://"+ location.host + location.pathname.replace(/^\/wsRoom/g, '/ws') + location.search;
    $('#goto').attr('href', wsURL).html(wsURL);

    var path = $('.path').val();
    path = path.replace(/^\/wsRoom/g, '');
    $('.path').val(path);
})();

$('#data').blur(checkFormat);

$('.submit').click(sendMessage);

(function() {
    var websocket = new WebSocket(wsURL);
    websocket.onopen=function (event) {
        var status = document.getElementById('status');
        status.innerHTML='已连接';
        status.className='connected';
    };
    websocket.onmessage=function (event) {
        // 收到消息去加载
        console.log('onmessage:'+event.data);
        var message = JSON.parse(event.data);
        loadMessage(message);
    };
    websocket.onerror=function (event) {
        var status = document.getElementById('status');
        status.innerHTML=event.toString();
        status.className='exception';
    };
    websocket.onclose=function (event) {
        var status = document.getElementById('status');
        status.innerHTML='已断开';
        status.className='disconnected';
    };
    window.sendMessage=function (data) {
        var text = JSON.stringify(data);
        websocket.send(text);
        console.log('sendMessage:'+text);
    }
})();

});
function initData(data) {
    window.data=data;
    var template=$('select#template');
    for (var key in data) {
        template.append('<option value="'+key+'">'+key+'</option>');
    }
}

function loadTemplate($this) {
    var key = $($this).val();
    if (key=='') {
        return;
    }
    var value = data[key];
    if (!value) {
        alert('该模板不存在！');
        return;
    }
    $('#data').html(JSON.stringify(value));
    checkFormat();
}

function checkFormat() {
    var text = $('#data').text().trim();
    if (text == '') {
        return;
    }
    JSONColor({
        CanvasId: 'data',
        text: text,
        ErrorId: 'error'
    });
}

function sendMessage() {
    if ($('#status').hasClass('disconnected')) {
        alert('已断开连接，请刷新页面！');
        return;
    }
    var error = $('#error');
    if (error.hasClass('Bad')) {
        alert(error.text());
        return;
    }
    var data = $('.data').text().trim();
    if (data == '') {
        alert('请输入json文本！');
        return;
    }
    var message = JSON.parse(data);
    message.meta = {
        from: window.name,
        to: 'all',
        avatar: null
    };
    loadMessage(message);
    sendMessage(message);
}

function loadMessage(message) {
    var dom = messageDom(message);
    $('.list').append(dom);
     //setTimeout(function() {
        var contentDom = dom.find('.content');
        JSONColor({
            Canvas: contentDom.get(0)
        });
     //}, 0);
}

/**
{
    from,
    to,
    avatar,
    content
}
*/
function messageDom(message) {
    var data = JSON.parse(JSON.stringify(message));
    var meta = data.meta;
    delete data.meta;

    var css = 'left';
    var avatar = '/images/avatar.svg';
    var name = '匿名';
    if (meta) {

        name = meta.from;
        if (name == window.name) {
            css = 'right';
        }

        if (meta.avatar) {
            avatar = meta.avatar;
        }
    }


    var content = JSON.stringify(data);
    dom = $('<li class="item '+css+'">\
                          <img class="avatar" src="'+avatar+'"/>\
                          <div class="message">\
                              <p class="name">'+name+'</p>\
                              <pre class="content">'+content+'</pre>\
                          </div>\
                      </li>'
    );
    return dom;
}