<#include "/common/commoncss.ftl">
<style type="text/css">
    .xiangqing {
        background-color: white;
        position: relative;
        width: 100%;
        height: 510px;

    }

    iframe {
        position: absolute;
        width: 60%;
        height: 95%;
        border: #0a84ff;
        margin-left: 240px;
        background-color: white;
    }


    .box-footer {
        text-align: center;
    }

    .masking {
        width: 100%;
        height: 100%;
        position: fixed;
        background: rgba(150, 150, 150, 0.8);
        display: none;
        top: 0;
        left: 0;
    }

    .layer {
        position: relative;
        width: 600px;
        height: 350px;
        background: #ccc;
        border: 3px solid #fff;
        left: 50%;
        margin-left: -300px;
        top: 50%;
        margin-top: -300px;
    }

    .close {
        position: absolute;
        right: 15px;
        top: 15px;
    }

    #alinkisok {
        margin-top: 30px;
    }

    .middle {
        margin-top: 80px;
        text-align: center;
    }

    textarea {
        margin-top: 20px;
    }

    .text {
        font-size: 20px;
        margin-bottom: 30px;
    }

    a:hover {
        text-decoration: none;
    }

    .bgc-w {
        background-color: #fff;
    }

    .red {
        color: #d9534f;
        font-weight: 100;
        font-size: 1px;
    }

    .mulu {
        text-align: center
    }



    .all {
        display: flex;
        width: 100%;
        height: 100%;
        padding-top: 60px;
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
        text-align: center;
        margin-top: -39px;
    }

    #myput {
        width: 25px
    }

</style>
<div class="row" style="padding-top: 10px;">
    <div class="col-md-2">
        <h1 style="font-size: 24px; margin: 0;" class="">审核管理</h1>
    </div>
    <div class="col-md-10 text-right">
        <a href="##"><span class="glyphicon glyphicon-home"></span> 首页</a> > <a
                disabled="disabled">审核管理</a>
    </div>
</div>
<div class="box-header">
    <h3 class="box-title">
        <a href="javascript:history.back();" class="label label-default"
           style="padding: 5px;"> <i
                    class="glyphicon glyphicon-chevron-left"></i> <span>返回</span>
        </a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        提交目录：<input value="${submitpath}" readonly disabled>
    </h3>
</div>
<div class="xiangqing">
    <div class="all">
        <div class="left">
            <img id="img" src="imgshow?fileid=189">
        </div>


        <div class="right">
            <table class="table table-hover table-responsive">
                <tbody class="result_body" id="main_body">
                <div class="btn-group" role="group" aria-label="..." style="float: right;margin-right: 52px;margin-top: -42px;">
                    <button type="button"  class="btn btn-default btn-success glyphicon glyphicon-plus" style="word-break:break-all; word-wrap:break-word; white-space:inherit"
                            onclick="addRow(this)"></button>
                </div>

                <tr id="clone">
<#--                    <td class="itemNo">审核点</td>-->
                    <td><input class="form-control" autocomplete="off" placeholder="检查点" id="texts"></td>

<#--                    <td>-->
<#--                        <div class="btn-group" role="group" aria-label="...">-->
<#--                            <button type="button" class="btn btn-default btn-success glyphicon glyphicon-plus"-->
<#--                                    style="word-break:break-all; word-wrap:break-word; white-space:inherit"-->
<#--                                    onclick="addRow(this)"></button>-->
<#--                            <button type="button" class="btn btn-default btn-danger glyphicon glyphicon-trash"-->
<#--                                    style="word-break:break-all; word-wrap:break-word; white-space:inherit"-->
<#--                                    onclick="removeRow(this)"></button>-->

<#--                            <button type="button" class="btn btn-default btn-success glyphicon glyphicon-plus"-->
<#--                                    style="word-break:break-all; word-wrap:break-word; white-space:inherit"-->
<#--                                    onclick="addRow(this)"></button>-->
                    <#--                        </div>-->
                    <#--                    </td>-->

                    <td style="line-height: 36px;">
                        <a href="" class="label xiugai"><span
                                    class="glyphicon glyphicon-edit"></span> 修改</a>

                            <a href="" class="label shanchu deletethis"><span class="glyphicon glyphicon-remove"></span>
                                删除</a>

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
</div>
<div class="box-footer">
    <a href="agree?noticesListId=${noticesListId}&fileid=${fileid}" class="btn btn-primary"
       style="font-size: 13px">同意</a>
    <input class="btn btn-default" id="cancel" type="button" value="拒绝"
    />

</div>
<div class="masking"><!--遮罩-->
    <div class="layer"><!--弹窗-->
        <a class="close">关闭</a>
        <div class="middle">
            <div class="text">请输入拒绝原因
                <div>
                    <textarea onkeyup="remarkkeyup(${noticesListId},${fileid})" id="remark"
                              style="width:300px;height:100px;" class="texts"></textarea>
                </div>
                <#--                <input id="okk" class="btn btn-primary" type="submit" value="确定" onclick="ok()"/>-->
                <a id="alinkisok" href="refuse?noticesListId=${noticesListId}&remark=''&fileid=${fileid}"
                   class="btn btn-primary" style="font-size: 13px">确定</a>

            </div>
        </div>
    </div>
</div>
<#include "/common/modalTip.ftl">
<#--拒绝，删除-->
<script type="text/javascript">

    $(function () {
        $('#cancel').click(function () {
            $('.masking').fadeIn();
        })
        $('.close').click(function (event) {
            event.preventDefault;
            $('.masking').hide();
        });
        $('#okk').click(function (event) {
            event.preventDefault;
            $(".masking").hide();
        });
    })

    function remarkkeyup(noticesListId, fileid) {
        var is = $('.texts').val();
        $("#alinkisok").attr("href", "refuse?noticesListId=" + noticesListId + "&remark=" + is + "&fileid=" + fileid);
    }

    <#--function ok() {-->
    <#--    var is=$('.texts').val();-->

    <#--        $.ajax({-->
    <#--            url: "/refuse",-->
    <#--            data: {"noticesListId":${noticesListId} , "fileid":${fileid},"remark":is},-->
    <#--            type: "GET",-->
    <#--            success: function (result) {-->
    <#--                alert("删除成功")-->

    <#--            }-->
    <#--        })-->

    <#--    window.location.href = "infromlist"-->
    <#--    opener.location.reload()-->
    <#--}-->
</script>


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