$(document).ready(function () {
(function() {
    var support = fileSupport['ws'];
    var newText = location.pathname.replace(support.manager, support.api);
    var newUrl = location.pathname.replace(support.manager, '/wsRoom');
    newText = location.protocol+'//'+location.host + newText;
    newUrl = location.protocol+'//'+location.host + newUrl;
    $('#goto').attr('href', newUrl).html(newText);
})()

$('.file').change(readFileContent);

$('.data').blur(checkFormat);

$('.submit').click(function() {
    var checkedTab = $('.tab.checked');
    if (!checkedTab.length) {
        alert('请先创建tab标签！');
        return;
    }
    var oldKey = checkedTab.attr('key');
    var input = checkedTab.find('input[type=text]');
    var newKey = input.val();
    if (newKey=='') {
        alert('tab标签名不能为空！');
        setTimeout(function() {
            input.trigger('dblclick');
        },0);
        return;
    }

    var error = $('#error');
    if (error.hasClass('Bad')) {
        alert(error.text());
        return;
    }

    var data = $('.data').text().trim();
    if (data == '') {
        if ($('#status').text()=='修改' && confirm('是否要删除？')) {
            deleteData(oldKey);
        } else {
            alert('请输入json文本！');
        }
        return;
    }

    putData(oldKey, newKey, data);
});

});

function loadJSON(data) {
    var text = data?JSON.stringify(data):'';
    $('.data').html('<pre>'+text+'</pre>');
    checkFormat();
}

function checkFormat() {
    var text = $('.data').text().trim();
    if (text == '') {
        return;
    }
    JSONColor({
        CanvasId: 'data',
        text: text,
        ErrorId: 'error',
        onerror: function(e) {
            $('.tab.checked input[type=text]').addClass('error');
        },
        onsuccess: function(data) {
            $('.tab.checked input[type=text]').removeClass('error');
        }
    });
}

function readFileContent() {
    var files = this.files;
    if (files.length>0) {
        var file = files[0];
        var reader = new FileReader();//new一个FileReader实例
        if (/text+/.test(file.type)) {//判断文件类型，是不是text类型
            reader.onload = function() {
                loadJSON(this.result);
            }
            reader.readAsText(file);
        //} else if(/image+/.test(file.type)) {//判断文件是不是imgage类型

            // reader.onload = function() {
            //     $('body').append('<img src="' + this.result + '"/>');
            // }
            // reader.readAsDataURL(file);
        } else {
            alert('请选择一个json文件');
        }
    }
}
function putData(oldKey, newKey, data) {
    var url = location.pathname;
    request({
        url: url,
        data: {
            oldKey: oldKey,
            newKey: newKey,
            data: data
        },
        dataType: 'json',
        type: 'put',
        success: function(response) {
            data = response;
            $('.tab.checked').attr('key', newKey).find('input[type=text]').removeClass('unsave').removeClass('error');
            loadStatus(response);
            alert('保存成功');
        }
    });
}

function deleteData(key) {
    function doDelete(){
        var checkedTab = $('.tab.checked');
        var prevTab = checkedTab.prev();
        checkedTab.remove();
        $('#data').html('');
        alert('已删除');
        setTimeout(function() {
            prevTab.trigger('click');
        }, 0);
    }
    if (!key) {
        doDelete();
        return;
    }
    var url = location.pathname;
    request({
        url: url,
        data: {
            key: key
        },
        dataType: 'json',
        type: 'delete',
        success: function(response) {
            data = response;
            if (response) {
                doDelete();
            } else {
                alert('删除失败');
            }
        }
    });
}

function loadStatus() {
    if (data) {
        $('#status').html('修改');
    } else {
        $('#status').html('新建');
    }
}

function initData(data) {
    window.data = data;
    loadStatus(data);
    var selectData;
    var key;
    for (key in data) {
        selectData = data[key];
        break;
    }
    var add = $('.tabloader>.add');
    for (var k in data) {
        add.before(tabDom(k, k==key));
    }
    loadJSON(selectData);
}

function toggleData($this) {
    var self = $($this);

    var untoggle=$('.tab>input[type=text].unsave, .tab>input[type=text].error');
    if (untoggle.length==1 && untoggle.get(0)==self.find('input[type=text]').get(0)) {
        // 排除掉选中自己
    } else if (untoggle.length) {
        alert('存在未保存的数据，请提交保存或删除后重试');
        setTimeout(function() {
            untoggle.trigger('dblclick');
        }, 0);
        return;
    }
    var key = self.attr('key');
    var json = data[key];
    loadJSON(json);
    $('.tab.checked').removeClass('checked');
    self.addClass('checked');
}

function tabDom(key, checked, status='') {
    return $('<div class="tab '+(checked?'checked':'')+'" key="'+key+'" ondblclick="enableInput(this)" onclick="toggleData(this)">\
                               <input class="'+status+'" type="text" value="'+key+'" placeholder="<未命名>" disabled onblur="disableInput(this)" onchange="changeInput(this)"/>\
                               <img src="/images/close.svg" class="del" onclick="deleteData('+key+')" />\
                           </div>');
}

function addTab($this) {
    var unadd=$('.tabloader>.tab>input[type=text].unsave, .tabloader>.tab>input[type=text].error');
    if (unadd.length) {
        alert('存在未保存的数据，请提交保存或删除后重试');
        setTimeout(function() {
            unadd.trigger('dblclick');
        }, 0);
        return;
    }
    $('.tabloader>.tab').removeClass('checked');
    var ele = tabDom('', true, 'unsave');
    $($this).before(ele);
    ele.trigger('dblclick');
    $('#data').html('');
}

function enableInput($this) {
    $($this).find('input[type=text]').removeAttr('disabled').select().click(function (event) {
        event.stopPropagation();
    });
}

function disableInput($this) {
    $($this).attr('disabled', 'true').unbind('click');
}

function changeInput($this) {
    $($this).addClass('unsave');
}

function modifyData($this) {
    var checkedTab = $('.tab.checked');
    try {
        var current = JSON.parse($($this).text());
    } catch(e) {
        return;
    }
    var last = data[checkedTab.attr('key')];
    if (eq(current, last)) {
        checkedTab.find('input[type=text]').removeClass('unsave');
    } else {
        checkedTab.find('input[type=text]').addClass('unsave');
    }
}