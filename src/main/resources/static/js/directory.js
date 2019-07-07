$(document).ready(function () {

});

function entryFile($this) {
    var parent = $($this).parents('.file');
    var filename = parent.attr('filename');
    var current = location.pathname;
    var fileext = parent.attr('fileext');
    var support = fileSupport[fileext];
    var name = filename;
    if (support) {
        var reg = new RegExp('^'+httpManager, 'g');
        current = current.replace(reg, support.manager);
        name = filename.substring(0, filename.lastIndexOf('.'));
    }
    current = current.replace(/\/$/, '');
    location.pathname = current + '/' + name;
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
function enableInput($this) {
    var parent = $($this).parents('.file');
    parent.find('.name').removeAttr('disabled').select().click(function (event) {
        event.stopPropagation();
    });
}
function disableInput($this) {
    $($this).attr('disabled', 'true').unbind('click');
}
function renameFile($this) {
    var self = $($this);
    var to = self.val();
    var parent = self.parents('.file');
    var filename = parent.attr('filename');
    var fileext = parent.attr('fileext');
    var support = fileSupport[fileext];
    if (support) {
        to += '.'+fileext;
    }
    request({
        url: httpManager+'/rename',
        data: {
            from: location.pathname+'/'+filename,
            to: location.pathname+'/'+to,
        },
        dataType: 'json',
        type: 'post',
        success: function(response) {
            if (response) {
                parent.attr('filename', to);
            } else {
                alert('重命名失败');
                var name = filename;
                if (support) {
                    var name = filename.substring(0, filename.lastIndexOf('.'));
                }
                self.val(name);
            }
        }
    });
}