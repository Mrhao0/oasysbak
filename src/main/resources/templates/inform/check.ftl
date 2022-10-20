<#include "/common/commoncss.ftl">
<style type="text/css">
    form {
        background-color: white;
    }

    iframe {
        width: 70%;
        height: 60%;
        border: #0a84ff;
        margin-left: 190px;
        background-color: white;
        margin-bottom: 30px;
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

    #okk {
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
        </a>
    </h3>
</div>
<#--<form action="informcheck" method="post" id="thisForm" onsubmit="return check();">-->
    <div class="xiangqing">
        <iframe src="imgshow?fileid=${fileid}"></iframe>
    </div>
    <div class="box-footer">
        <input class="btn btn-primary" id="save" type="submit" value="通过" size="small" onclick="okay();"/>
        <input class="btn btn-default" id="cancel" type="button" value="拒绝"
               onclick="delblog(${noticesListId})"/>
        </div>
        <div class="masking"><!--遮罩-->
            <div class="layer"><!--弹窗-->
                <a class="close">关闭</a>
                <div class="middle">
                    <div class="text">请输入拒绝原因
                        <div>
                            <textarea style="width:300px;height:100px;"></textarea>
                        </div>
                        <input id="okk" class="btn btn-primary" type="submit" value="确定" onclick="ok(${noticesListId})"/>
                    </div>
                </div>
</div>
            </div>



            <#include "/common/modalTip.ftl">
            <#--拒绝，删除-->
            <script type="text/javascript">
                function delblog(obj) {

                    return true

                }

                $(function () {
                    $('#cancel').click(function () {
                        $('.masking').fadeIn();
                    })
                    $('.close').click(function (event) {
                        event.preventDefault;
                        $('.masking').hide();
                    });

                    $('.masking').click(function (event) {
                        event.preventDefault;
                        $(this).hide();
                    });
                })

                $(function () {
                    $('okay').click(function () {

                    });
                })


                function ok() {
                    
                }


                //表单提交前执行的onsubmit()方法；返回false时，执行相应的提示信息；返回true就提交表单到后台校验与执行
                // function check() {
                //     console.log("开始进入了");
                //     //提示框可能在提交之前是block状态，所以在这之前要设置成none
                //     $('.alert-danger').css('display', 'none');
                //     var isRight = 1;
                //     $('.form-control').each(function (index) {
                //         // 如果在这些input框中，判断是否能够为空
                //         if ($(this).val() == "") {
                //             // 排除哪些字段是可以为空的，在这里排除
                //             if (index == 3 || index == 4) {
                //                 return true;
                //             }
                //             if (index == 3) {
                //
                //             }
                //             // 获取到input框的兄弟的文本信息，并对应提醒；
                //             var brother = $(this).siblings('.control-label').text();
                //             var errorMess = "[" + brother + "输入框信息不能为空]";
                //             // 对齐设置错误信息提醒；红色边框
                //             $(this).parent().addClass("has-error has-feedback");
                //             $('.alert-danger').css('display', 'block');
                //             // 提示框的错误信息显示
                //             $('.error-mess').text(errorMess);
                //             // 模态框的错误信息显示
                //             $('.modal-error-mess').text(errorMess);
                //             isRight = 0;
                //             return false;
                //         } else {
                //             // 在这个里面进行其他的判断；不为空的错误信息提醒
                //             return true;
                //         }
                //     });
                //     if (isRight == 0) {
                //         //modalShow(0);
                //         return false;
                //     } else if (isRight == 1) {
                //         //modalShow(1);
                //         return true;
                //     }
                // }
            </script>