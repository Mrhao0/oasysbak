<#include "/common/commoncss.ftl">
<style type="text/css">

    .xiangqing {width:500px;height: 300px;border: #0a84ff}
    .box-footer {text-align: center}


    a:hover {
        text-decoration: none;
    }

    .bgc-w {
        background-color: #fff;
    }
    .red{
        color:#d9534f;
        font-weight:100;
        font-size:1px;
    }

</style>
<div class="row" style="padding-top: 10px;">
    <div class="col-md-2">
        <h1 style="font-size: 24px; margin: 0;" class="">通知管理</h1>
    </div>
    <div class="col-md-10 text-right">
        <a href="##"><span class="glyphicon glyphicon-home"></span> 首页</a> > <a
                disabled="disabled">通知管理</a>
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
<form action="informcheck" method="post" id="thisForm" onsubmit="return check();">
    <iframe src="imgshow?fileid=${fileid}" width="100%" height="100%"></iframe>


    <#include "/common/modalTip.ftl">

