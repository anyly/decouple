$(document).ready(function () {

});

function entryFile($this) {
    var parent = $($this).parents('.file');
    var filename = parent.attr('filename');
    var current = location.pathname;
    current = current.replace(/\/$/, '');
    location.pathname = current + '/' + filename;
}

function showExtend($this) {
    $($this).css({'background-color': 'gainsboro', 'color':'#999'}).find('.extend').css('opacity', 1);
}

function hideExtend($this) {
    $($this).css({'background-color': '', 'color':''}).find('.extend').css('opacity', '');
}

function removeFile($this) {
    var parent = $($this).parents('.file');
    var filename = parent.attr('filename');
    var filetype = parent.attr('filetype');
    if (filetype == 'false') {
        if (!confirm('是否要删除此目录')) {
            return;
        }
    }
    request({
        url: location.pathname + '/'+filename,
        data: null,
        dataType: 'json',
        type: 'delete',
        success: function(response) {
            if (response) {
                parent.remove();
            } else {
                alert('删除失败');
            }
        }
    })
}