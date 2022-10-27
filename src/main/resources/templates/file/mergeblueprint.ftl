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
    <script type="text/javascript" src="js/task/taskmanage.js"></script>
    <style type="text/css">
        a {
            color: black;
        }

        a:hover {
            text-decoration: none;
        }

        .label-back {
            background-color: #6C7B8B;
            color: white;
        }

        .label-back:hover {
            color: white !important;
            background-color: #5c666b !important;
        }

        .block {
            display: inline-block;
            width: 20px;
        }

        .co {
            color: blue;
        }

        .bl {
            color: black;
        }

        .commen {
            cursor: pointer;
        }

        .bigbox1,.bigbox2,.bigbox3,.bigbox4{
            width: 93%;
            height: 500px;
            border: 5px solid black;
            margin: 30px auto;
            padding: 20px;
        }

        .bigbox1,.bigbox2,.bigbox3{
            display: none;
        }

        .foot{
            width: 100%;
            height: 60px;
        }

       .box1_left{
            width:77%;
            height: 100%;
            border: 3px solid black;
            float: left;
        }

       .box1_right{
            width:21%;
            height: 100%;
            border: 3px solid black;
            float: right;
        }

       .left1{
           width: 100%;
           height: 50%;
           border: 3px solid black;
       }

       .left2{
           width:50%;
           height: 50%;
           border: 3px solid black;
           float: left;
       }
       .pages{
           float: right;
           font-size: 16px;
           margin-right: 30px;
       }

       #nexts{
           float: left;
           margin-left:10px;
           padding:6px 16px;
           background: #337ab7;
           color: white;
           border: none;
           border-radius: 5px;

       }

       #finish{
           float: left;
           margin-left:10px;
           padding:6px 16px;
           background: #337ab7;
           color: white;
           border: none;
           border-radius: 5px;

       }

       #sele{
           float: left;
           margin-left:43px;
           height: 25px;
           width: 100px;
           border-radius: 5px;
           margin-top: 3px;

       }



       .source, .imgs{
           margin: 20px;
           float: left;
           height: 25px;
           width: 100px;
           border-radius: 5px;

       }
       .imgs{
           margin: 20px 0px;
       }


        .box1_right   .source{
            width: 92px;
            margin: 10px;
        }
        .box1_right   .imgs{
            width: 92px;
            margin: 10px;
        }

        .blacks {
            width: 100%;
            /*height: 673px;*/
            height: 0px;
            background: black;
            opacity: 0.9;
            position: absolute;
            top: 0px;
            left: 0;
            transition: 0.7s;

        }
        .blacks  .whites{
            width: 28%;
            height: 200px;
            background: white;
            border-radius: 8px;
            position: absolute;
            top: 33%;
            left: 33%;
            display: none;
            opacity: 1;
            z-index: 999;
            color: black;
            padding: 26px;
        }

        .sure{
            background: #5cb85c;
            color: white;
            padding: 6px 20px;
            border-radius: 5px;
            margin-left: 107px;
            margin-top: 22px;
        }

        .nams{
            margin-left: 16px;
        }

        #catalogue{
            margin-left: 10px;
            width: 185px;
            height: 29px;
        }

        .cler{
            font-size: 25px;
            color: white;
            position: absolute;
            top: 30px;
            right: 60px;
            cursor: pointer;
        }
    </style>
    <div class="row" style="padding-top: 10px;">
        <div class="col-md-2">
            <h1 style="font-size: 24px; margin: 0;" class="">合成图纸</h1>
        </div>
        <div class="col-md-10 text-right">
            <a href="##"><span class="glyphicon glyphicon-home"></span> 首页</a> > <a
                    disabled="disabled">合成图纸</a>
        </div>
    </div>


    <div class="row" style="padding-top: 15px;">
        <div class="col-md-12 thistable">
            <div class="bgc-w box box-primary">
                <!--盒子身体-->
                <div class="box-body no-padding">
                    <div class="table-responsive" style="margin-top: 20px">
                        <select id="sele">
                            <option selected="selected" disabled="disabled"  style='display: none' value=''>选择模板</option>
                            <option value="1">模板一</option>
                            <option value="2">模板二</option>
                            <option value="3">模板三</option>
                        </select>
                        <button id="nexts" type="button">下一页</button>
                        <button id="finish" type="button">完成</button>
                        <p id="pageNoYes" class="pages">1</p>
                    </div>
                    <div class="bigbox1">
                        <div id="1b" class="box1_left">
                            <select id="1ls" class="source" onchange="getImgList(1)">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>
                                <#list materialLib as item>
                                    <option value="${item.id}">${item.name}</option>
                                </#list>

                            </select>

                            <select id="1rs" onchange="img(1)"  class="imgs">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                            </select>
                        </div>
                        <div id="2b" class="box1_right">
                            <select id="2ls" onchange="getImgList(2)" class="source">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>
                                <#list materialLib as item>
                                    <option value="${item.id}">${item.name}</option>
                                </#list>

                            </select>

                            <select id="2rs" onchange="img(2)" class="imgs" >
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                            </select>
                        </div>

                    </div>
                    <div class="bigbox2">
                        <div class="box1_left">
                            <div id="3b" class="left1">
                                <select id="3ls" onchange="getImgList(3)" class="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>
                                    <#list materialLib as item>
                                        <option value="${item.id}">${item.name}</option>
                                    </#list>
                                </select>

                                <select id="3rs" onchange="img(3)" class="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>
                            <div id="4b" class="left1">
                                <select id="4ls" onchange="getImgList(4)" class="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>
                                    <#list materialLib as item>
                                        <option value="${item.id}">${item.name}</option>
                                    </#list>
                                </select>

                                <select id="4rs" onchange="img(4)" class="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>
                        </div>
                        <div id="5b" class="box1_right">
                            <select  id="5ls" onchange="getImgList(5)" class="source">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>
                                <#list materialLib as item>
                                    <option value="${item.id}">${item.name}</option>
                                </#list>
                            </select>

                            <select id="5rs" onchange="img(5)" class="imgs">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                            </select>
                        </div>

                    </div>
                    <div class="bigbox3">
                        <div class="box1_left">
                            <div id="6b" class="left2">
                                <select id="6ls" onchange="getImgList(6)" class="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>
                                    <#list materialLib as item>
                                        <option value="${item.id}">${item.name}</option>
                                    </#list>
                                </select>

                                <select id="6rs" onchange="img(6)" class="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>
                            <div id="7b" class="left2">
                                <select id="7ls" onchange="getImgList(7)" class="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>
                                    <#list materialLib as item>
                                        <option value="${item.id}">${item.name}</option>
                                    </#list>
                                </select>

                                <select id="7rs" onchange="img(7)" class="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>
                            <div id="8b" class="left2">
                                <select id="8ls" onchange="getImgList(8)" class="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>
                                    <#list materialLib as item>
                                        <option value="${item.id}">${item.name}</option>
                                    </#list>
                                </select>

                                <select id="8rs" onchange="img(8)" class="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>
                            <div id="9b" class="left2">
                                <select id="9ls" onchange="getImgList(9)" class="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>
                                    <#list materialLib as item>
                                        <option value="${item.id}">${item.name}</option>
                                    </#list>
                                </select>

                                <select id="9rs" onchange="img(9)" class="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>

                        </div>
                        <div id="10b" class="box1_right">
                            <select id="10ls" onchange="getImgList(10)" class="source">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>
                                <#list materialLib as item>
                                    <option value="${item.id}">${item.name}</option>
                                </#list>
                            </select>

                            <select id="10rs" onchange="img(10)" class="imgs">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                            </select>
                        </div>
                    </div>
                    <div class="bigbox4">
                    </div>

                </div>



            </div>
            <div class="blacks">
                <div class="cler">X</div>
                <div class="whites">
                    <div style="margin-left: 27px;margin-top: 22px">
                        名字:<input type="text" placeholder="请输入文件名字"class="nams">
                    </div>
                    <div style="margin-top: 20px">
                        提交目录: <select id="catalogue">
                            <option selected="selected" disabled="disabled"  style='display: none' value=''>目录</option>

                        </select>
                    </div>

                    <button type="button" class="sure">确定</button>
                </div>
            </div>
        </div>
        <p id="dirId" hidden></p>
        <p id="materialIds" hidden></p>
    </div>

<script >
    $(function (){
        <#-- 选择模板 下拉框  -->
        $("#sele").on('click',(e) => {
            e.preventDefault();
            var options= $("#sele").val()
            console.log(options);
            if(options==1){
                $('.bigbox1').css('display','block')
                $('.bigbox2').css('display','none')
                $('.bigbox3').css('display','none')
                $('.bigbox4').css('display','none')
            }else if (options==2){
                $('.bigbox1').css('display','none')
                $('.bigbox2').css('display','block')
                $('.bigbox3').css('display','none')
                $('.bigbox4').css('display','none')
            }else if (options==3){
                $('.bigbox1').css('display','none')
                $('.bigbox2').css('display','none')
                $('.bigbox3').css('display','block')
                $('.bigbox4').css('display','none')
            }
        })



        //下一页  按钮
        $('#nexts').on('click',function (e){
            // e.preventDefault();
            var pan = Number($("#pageNoYes").html())
            var dirId = Number($("#dirId").html())
            var type =$("#sele").val();
            var materialIds= $("#materialIds").html();
            if(dirId==""){
                dirId=null;
            }
            var data={
                "page":pan,
                "dirId":dirId,
                "type":type,
                "materialIds":materialIds
            }
            $.ajax({
                url: "mergedImages",
                type: "post",
                dataType: "JSON",
                data:data,
                success(res) {
                    console.log(res)
                },
                error(res){
                    console.log('error')
                }
            })
            pan++
            $("#pageNoYes").html(pan)
            // $('.box1_right').css('background','white')
        })


        //完成   按钮
        $('#finish').on('click',function (e){
            $.ajax({
                url:"getSubmitPath",
                type:'post',
                dataType:"json",
                success(res) {
                    console.log(res)
                    res.forEach(item =>{
                        console.log(item.name)

                        $('#catalogue').append("<option value='"+item.id+"'>"+item.name+"</option>")
                    })

                }
            })

            $('.blacks').css('height','673px')
            $('.whites').css('display','block')
            $('.nams').val('')
        })

            //确定  按钮
        $('.sure').click(function (){
            var nam = $('.nams').val()
            var cata = $('#catalogue').val()
            console.log(nam,cata)
            if(nam!=''&&cata!=null){
                $.ajax({
                    url:"mergedPDF",
                    type:'post',
                    dataType:"json",
                    data:{"fileListId":'',"dirId":cata,"pdfName":nam},
                    success(res) {
                     alert('合成成功')
                    },
                    error(res){
                        alert('合成失败')
                    }
                })

                $('.blacks').css('height','0px')
                $('.whites').css('display','none')
            }else {
                alert('请填写')
            }

        })
    })

    //完成的关闭 按钮
    $('.cler').click(function (){
        $('.blacks').css('height','0px')
        $('.whites').css('display','none')
    })

    function getImgList(id){
        console.log(id)
        var sourceval = $("#"+id+"ls").val();
        console.log(sourceval)
        $.ajax({
            url: "getMaterialList",
            type: "post",
            dataType: "JSON",
            data:{'id':sourceval},
            success(res) {
                console.log(res)
                var html="";
                for(var i=0;i<res.length;i++){
                    html+="<option value='"+res[i].id+"'>"+res[i].name+"</option>"
                }
                $("#"+id+"rs").append(html)
            },
            error(res){
                console.log('error')
            }
        })
    }
    function img(id){
        var sourceval = $("#"+id+"rs").val();
        var materialIds= $("#materialIds").html();
        if(materialIds!=""){
            materialIds=materialIds+","+sourceval;
        }else{
            materialIds=sourceval;
        }
        $("#materialIds").html(materialIds);
        $("#"+id+"b").css("background","url(imgshow?fileid="+sourceval+")")
    }
</script>