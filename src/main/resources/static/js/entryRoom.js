$(document).ready(function () {
    var newUrl = location.pathname.replace(httpManager, httpApi);
    newUrl = location.protocol+'//'+location.host + newUrl+location.search
    $('#goto').attr('href', newUrl).html(newUrl);

    $('#data').blur(checkFormat);

    $('.submit').click(sendMessage);
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
    var message = {
        from: 'system',
        to: 'all',
        avatar: null,
        content: JSON.parse(data)
    };
    loadMessage(message);
    /* websocket
    var url = location.pathname;
    request({
        url: url,
        data: {
            data: data
        },
        dataType: 'json',
        type: 'put',
        success: function(response) {
            loadStatus(data);
            alert('保存成功');
        }
    });*/
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
function messageDom(opt) {
    var css = 'left';
    var name = opt.name;
    if (opt.from=='system') {
        css = 'right';
        name = '';
    }
    var avatar = '/images/avatar.svg';
    if (opt.avatar) {
        avatar = opt.avatar;
    }
    var content = '';
    if (opt.content) {
        content = JSON.stringify(opt.content);
    }
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