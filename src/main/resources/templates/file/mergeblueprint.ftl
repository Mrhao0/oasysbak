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
           display: none;
       }

       #finish{
           float: left;
           margin-left:10px;
           padding:6px 16px;
           background: #337ab7;
           color: white;
           border: none;
           border-radius: 5px;
           display: none;
       }

       #sele{
           float: left;
           margin-left:10px;
           height: 25px;
           width: 100px;
           border-radius: 5px;
           margin-top: 3px;
           display: none;
       }

       #satrt{
           float: left;
           padding:6px 16px;
           background: #337ab7;
           color: white;
           border: none;
           border-radius: 5px;
           margin-left: 35px;
       }

       #source, #imgs{
           margin: 20px;
           float: left;
           height: 25px;
           width: 100px;
           border-radius: 5px;

       }
       #imgs{
           margin: 20px 0px;
       }


        .box1_right   #source{
            width: 92px;
            margin: 10px;
        }
        .box1_right   #imgs{
            width: 92px;
            margin: 10px;
        }

        .blacks {
            width: 100%;
            /*height: 673px;*/
            height: 0px;
            background: black;
            opacity: 0.8;
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

        .names{
            margin-left: 16px;
        }

        #catalogue{
            margin-left: 10px;
            width: 185px;
            height: 29px;
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
                        <button type="button" id="satrt">开始合成</button>
                        <select id="sele">
                            <option selected="selected" disabled="disabled"  style='display: none' value=''>选择模板</option>
                            <option>模板一</option>
                            <option>模板二</option>
                            <option>模板三</option>
                        </select>
                        <button id="nexts" type="button">下一页</button>
                        <button id="finish" type="button">完成</button>
                    </div>
                    <div class="bigbox1">
                        <div class="box1_left">
                            <select id="source">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>
                                <option>素材一</option>

                            </select>

                            <select id="imgs">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                            </select>
                        </div>
                        <div class="box1_right">
                            <select id="source">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>

                            </select>

                            <select id="imgs">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                            </select>
                        </div>

                    </div>
                    <div class="bigbox2">
                        <div class="box1_left">
                            <div class="left1">
                                <select id="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>

                                </select>

                                <select id="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>
                            <div class="left1">
                                <select id="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>

                                </select>

                                <select id="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>
                        </div>
                        <div class="box1_right">
                            <select id="source">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>

                            </select>

                            <select id="imgs">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                            </select>
                        </div>

                    </div>
                    <div class="bigbox3">
                        <div class="box1_left">
                            <div class="left2">
                                <select id="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>

                                </select>

                                <select id="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>
                            <div class="left2">
                                <select id="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>

                                </select>

                                <select id="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>
                            <div class="left2">
                                <select id="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>

                                </select>

                                <select id="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>
                            <div class="left2">
                                <select id="source">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>

                                </select>

                                <select id="imgs">
                                    <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                                </select>
                            </div>

                        </div>
                        <div class="box1_right">
                            <select id="source">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择素材库</option>

                            </select>

                            <select id="imgs">
                                <option selected="selected" disabled="disabled"  style='display: none' value=''>选择图片</option>

                            </select>
                        </div>
                    </div>
                    <div class="bigbox4">
                    </div>

                </div>

                <div class="foot">
                    <p class="pages">1</p>
                </div>

            </div>
            <div class="blacks">
                <div class="whites">
                    <div style="margin-left: 27px;margin-top: 22px">
                        名字:<input type="text" placeholder="请输入文件名字"class="names">
                    </div>
                    <div style="margin-top: 20px">
                        提交目录: <select id="catalogue">
                            <option>目录</option>
                            <option>目录1</option>
                            <option>目录2</option>


                        </select>
                    </div>

                    <button type="button" class="sure">确定</button>
                </div>
            </div>

        </div>
    </div>

<script>
<#-- 选择模板 下拉框  -->
    $("#sele").on('click',(e) => {
        e.preventDefault();
        var options= $("#sele").val()
        if(options=="模板一"){
            $('.bigbox1').css('display','block')
            $('.bigbox2').css('display','none')
            $('.bigbox3').css('display','none')
            $('.bigbox4').css('display','none')
        }else if (options=="模板二"){
            $('.bigbox1').css('display','none')
            $('.bigbox2').css('display','block')
            $('.bigbox3').css('display','none')
            $('.bigbox4').css('display','none')
        }else if (options=="模板三"){
            $('.bigbox1').css('display','none')
            $('.bigbox2').css('display','none')
            $('.bigbox3').css('display','block')
            $('.bigbox4').css('display','none')
        }
    })

    //开始合成 按钮
    $('#satrt').on('click',function (){
        $('#sele').css('display','block')
        $('#nexts').css('display','block')
        $('#finish').css('display','block')

    })

    //下一页  按钮
    $('#nexts').on('click',function (e){
        e.preventDefault();
       var pan = Number($('.pages').html())
        pan++
        $('.pages').html(pan)
    })


    //完成   按钮
$('#finish').on('click',function (e){
    $('.blacks').css('height','673px')
    $('.whites').css('display','block')
})

//确定  按钮
$('.sure').on('click',function (){
    var nam = $('.names').val()
    var cata = $('#catalogue').val()
    console.log(nam,cata)
    $('.blacks').css('height','0px')
    $('.whites').css('display','none')
})

$(()=>{
    $('.names').val('')
})
</script>