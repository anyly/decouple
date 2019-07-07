$(document).ready(function () {
    var newUrl = location.pathname.replace(httpManager, httpApi);
    newUrl = location.protocol+'//'+location.host + newUrl
    $('#goto').attr('href', newUrl).html(newUrl);

    $('.file').change(readFileContent);

    $('.data').blur(checkFormat);

    $('.submit').click(function() {
        var error = $('#error');
        if (error.hasClass('Bad')) {
            alert(error.text());
            return;
        }

        var data = $('.data').text().trim();
        if (data == '') {
            if ($('#status').text()=='修改' && confirm('是否要删除？')) {
                deleteData();
            } else {
                alert('请输入json文本！');
            }
            return;
        }

        putData(data);
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
        ErrorId: 'error'
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
function putData(data) {
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
    });
}

function deleteData() {
    var url = location.pathname;
    request({
        url: url,
        data: null,
        dataType: 'json',
        type: 'delete',
        success: function(response) {
            if (response) {
                loadStatus('');
                alert('已删除');
            } else {
                alert('删除失败');
            }
        }
    });
}

function loadStatus(data) {
    if (data) {
        $('#status').html('修改');
    } else {
        $('#status').html('新建');
    }
}

function loadData(data) {
    loadStatus(data);

    loadJSON(data);

}