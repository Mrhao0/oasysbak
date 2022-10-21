<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title></title>
    <#include "/common/commoncss.ftl">
    <link href="css/common/checkbox.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="css/common/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="css/file/filebox.css" />
    <link rel="stylesheet" type="text/css" href="css/file/filemodal.css" />


    <style type="text/css">
        a {
            color: black;
        }

        a:hover {
            text-decoration: none;
            cursor: pointer;
        }

        .bgc-w {
            background-color: #fff;
        }

        .box .nav-stacked>li {
            border-bottom: 1px solid #f4f4f4;
            margin: 0;
        }

        .nav-stacked>li>a {
            border-radius: 0;
            border-top: 0;
            border-left: 3px solid transparent;
            color: #444;
        }

        li.activee>a {
            border-left-color: #3c8dbc !important;
        }

        .des {
            border: none;
            color: #9e9e9e;
        }
        .menu{
            position: fixed;
            left:0;
            top:0;
            min-width:114px;
            background-color: #fff;
            display: none;
            z-index:30;
            box-shadow:0 0 10px #999999;
            border-radius: 5px;
        }
        .menu .nav li a{
            padding:5px 15px;
        }
        .menu a.disabled{
            pointer-events: none;
            filter: alpha(opacity=50); /*IE滤镜，透明度50%*/
            -moz-opacity: 0.5; /*Firefox私有，透明度50%*/
            opacity: 0.5; /*其他，透明度50%*/
        }
        .pathtextarea .creatpathinput{
            height:23px;
            width:78px;
            font-size: 12px;
            border: 1px solid rgba(58,140,255,.3);
            border-radius: 2px;
            padding-top: 0px;
            padding-left: 4px;
        }
        .pathtextarea .creatpathinput:focus{
            outline: none;
            border: 1px solid #0099CC;
            border-radius: 2px;
        }
        .pathtextarea .btn-default{
            padding: 0px 4px 0px 4px;
            border:1px solid rgba(58,140,255,.3);
            color: #3b8cff;
        }
        .pathtextarea .btn-default:hover{
            background-color: #fff !important;
        }

    </style>
</head>

<body style="background-color: #ecf0f5;">
<div class="row" style="padding-top: 10px;">
    <div class="col-md-2">
        <h1 style="font-size: 24px; margin: 0;" class="">素材管理</h1>
    </div>
    <div class="col-md-10 text-right">
        <a href="##"><span class="glyphicon glyphicon-home"></span> 首页</a>
        <a disabled="disabled">素材管理</a>
    </div>

</div>
<div class="row" style="padding-top: 15px;">
    <div class="col-md-3">

        <div class="bgc-w box box-solid">
            <div class="box-header">
                <h3 class="box-title">云盘</h3>
                <span class="btn btn-default pull-right btn-xs des mm"> <i
                            class="glyphicon glyphicon-minus"></i>
					</span>
            </div>
            <ul class="nav nav-pills nav-stacked mm">
                <li class="activee filetypedocument"><a href="filemanage"> <span
                                class="iconfont icon-suoyougongneng"></span> 全部文件</a></li>
                <li><a href="##" class="filetypedocument"><span class="iconfont icon-icon4"></span>
                        文档</a></li>
            </ul>
        </div>
    </div>
    <div class="col-md-9">
        <!--id="container"-->
        <div class="loadfiletype">
            <#include "/file/filetypeload.ftl"/>
        </div>
    </div>
</div>

<div class="modal">
    <div class="file-one diplaynone">
        <div class="file-img">
            <img src="images/fileimg/Folder.png" />
        </div>
        <div class="file-name">
            <a></a>
        </div>
        <input type="hidden" class = "pathmessage" value="">
        <span class="file-check">
				<span class = "iconfont icon-xuanze" style="height:1.5em;width:1.5em"></span>
			</span>
    </div>
</div>

<!-- 移动复制模态框 -->
<div class="modal fade in" id="thismodal" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body box no-padding" style="display: none;">
                <div class="box-header">
                    <h3 class="box-title mc-title" style="font-size:15px;"></h3>
                </div>
                <div class="box-body no-padding" style="height: 50px">
                    <#--					<div style="margin-top: 60px;margin-left: 40px">	文件名:<input type="text" style="margin-left: 20px;width: 250px" class="finame"></div>-->
                    <div style="margin-top: 30px;margin-left: 40px"> 文件目录:<input type="text" style="margin-left: 10px;width: 250px" class="filcatalog"></div>
                </div>
                <div class="box-footer" style="text-align:right;">
                    <input class="userrootpath" type="hidden" name="userrootpath" value="${userrootpath.id}"/>
                    <form action="mcto" method="get">
                        <input class="mctoid" type="hidden" name="mctoid" value="${userrootpath.id}"/>
                        <input class="mcfileids" type="hidden" name="mcfileids" value=""/>
                        <input class="mcpathids" type="hidden" name="mcpathids" value=""/>
                        <input type="hidden" name="pathid" value="${nowpath.id}"/>
                        <input class="morc" type="hidden" name="morc" value=""/>
                        <button type="button" class="btn btn-primary" id="sure"
                        >确定</button>
                        <button type="button" class="btn btn-default mcmodalcancle"
                                data-dismiss="modal">取消</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
<#if message??>
    <script type="text/javascript">
        alert("${message}");
    </script>
</#if>
<script src="js/common/iconfont.js"></script>
<script src="js/file/filejs.js"></script>
<script src="js/file/fileajax.js"></script>
<script type="text/javascript">
    $(function() {

        /*
         * 收縮
         */
        $("body").on("click",".des",function(){
            console.log("aaaa");
            var $this = $(this).children();

            var $ul = $(this).parents(".box-header").next();

            if($(this).hasClass("mm")) {
                if($this.hasClass("glyphicon-minus")) {
                    $this.removeClass("glyphicon-minus").addClass("glyphicon-plus");
                } else {
                    $this.removeClass("glyphicon-plus").addClass("glyphicon-minus");
                }
                $ul.slideToggle(1000);
            } else {
                if($this.hasClass("glyphicon-minus")) {
                    $this.removeClass("glyphicon-minus").addClass("glyphicon-plus");
                } else {

                    $this.removeClass("glyphicon-plus").addClass("glyphicon-minus");
                }
                $ul.slideToggle(1000);
            }
        });

        $(".nav.mm").on("click", "li", function() {
            $(this).parent().children(".activee").removeClass("activee");
            $(this).addClass("activee");
        });

        $(".uploadfile input").bind("change",function(){
            $(".fileuploadform").submit();
        });

    });

</script>
</html>