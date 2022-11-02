<#include "/common/commoncss.ftl">
<style type="text/css">

    .all {
        display: flex;
        width: 100%;
        height: 70%;
    }

    .left {
        width: 55%;
        height: 80%;
        margin-left: 15px;
    }

    .right {
        position: relative;
        width: 45%;
        height: 80%;
        padding: 10px;

    }

    img {
        width: 100%;
        height: 100%
    }

    a:hover {
        text-decoration: none;
    }

    .next {
        position: fixed;
        bottom: 240px;
        right: 80px;
    }

    ul {
        width: 450px;
    }

    ul li {
        line-height: 35px;
        font-weight: 700;
        display: none;
        list-style: none;
    }

    .num {
        text-align: center
    }

    #myput {
        width: 25px
    }

</style>
<div class="row" style="padding-top: 10px;">
    <div class="col-md-2">
        <h1 style="font-size: 24px; margin: 0;" class="">模板管理</h1>
    </div>
    <div class="col-md-10 text-right">
        <a href="##"><span class="glyphicon glyphicon-home"></span> 首页</a> > <a
                disabled="disabled">模板管理</a>
    </div>
</div>
<div class="box-header">
    <h3 class="box-title">
        <a href="javascript:history.back();" class="label label-default"
           style="padding: 5px;"> <i
                    class="glyphicon glyphicon-chevron-left"></i> <span>返回</span>
        </a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </h3>
</div>
<div class="all">
    <div class="left">
        <#--                <iframe src="imgshow?fileid=${fileid}"></iframe>-->
        <img id="img" src="imgshow?fileid=189">
    </div>
    <div class="right">

        <table class="table table-hover table-responsive">
            <tbody class="result_body" id="main_body">
            <tr id="clone">
                <td class="itemNo">部门列表</td>
                <td>
                    <select name="itemType" class="form-control" id="texts">
                        <option value="1">请选择</option>
                        <option value="2">数值</option>
                        <option value="3">合格</option>
                    </select>
                </td>
                <td><input class="form-control" autocomplete="off" placeholder="检查点" id="texts"></td>
                <td>
                    <div class="btn-group" role="group" aria-label="...">
                        <button type="button" class="btn btn-default btn-success glyphicon glyphicon-plus"
                                style="word-break:break-all; word-wrap:break-word; white-space:inherit"
                                onclick="addRow(this)"></button>
                        <button type="button" class="btn btn-default btn-danger glyphicon glyphicon-trash"
                                style="word-break:break-all; word-wrap:break-word; white-space:inherit"
                                onclick="removeRow(this)"></button>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="next">
            <button onclick="nextPage()" class="btn btn-primary" style="font-size: 13px">下一页</button>
        </div>
    </div>
</div>
<div class="num">第 <input type="text" value="1" id="myput" style="text-align: center"></input> 页</div>
<script type="text/javascript">

    // 点击添加
    function addRow(item) {
        var curRow = $(item).closest('tr');
        var cloneRow = $('#clone').clone();
        curRow.after(cloneRow);
        curRow.next().find(":input").val('');
        // console.log( $('#texts').val())
        // console.log($('#texts').val())
        // $("#texts").find('option:selected').text();

        <#--    var result=$('#texts').val();-->
        <#--        $.ajax({-->
        <#--            url: "/refuse",-->
        <#--            data: {"noticesListId":${noticesListId} , "fileid":${fileid},"remark":is},-->
        <#--            type: "GET",-->
        <#--            success: function (result) {-->
        <#--                alert("下一页")-->
        <#--            }-->
        <#--        })-->
    }


    // 点击删除
    function removeRow(item) {
        if (getRowLength() <= 1) {
            toastr.warning('至少保留一行数据！');
            return;
        } else {
            $(item).closest("tr").remove();
            formatIndex();
        }
    }


    // 防止删空
    function getRowLength() {
        // 统计当前表格行数，防止删空
        return $("#main_body").find("tr").length;
    }

    function nextPage() {
        var res = $('#myput').val();
        $('#myput').val(res * 1 + 1)
        $('#img').attr('src', "imgshow?fileid=190")
    }
</script>