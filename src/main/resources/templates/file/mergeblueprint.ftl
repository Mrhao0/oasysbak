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

        .bigbox{
            width: 80%;
            height: 500px;
            border: 5px solid black;
            margin: 0 auto;
            padding: 20px;
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
                        <button type="button" style="padding:6px 16px;background: #337ab7;color: white;border: none;border-radius: 5px">开始合成</button>
                        <select style="margin-left:10px;height: 25px;width: 100px;border-radius: 5px">
                            <option>选择模板</option>
                            <option>模板一</option>
                            <option>模板二</option>
                            <option>模板三</option>
                        </select>
                        <button type="button" style="margin-left:10px;padding:6px 16px;background: #337ab7;color: white;border: none;border-radius: 5px">下一页</button>
                        <button type="button" style="margin-left:10px;padding:6px 16px;background: #337ab7;color: white;border: none;border-radius: 5px">完成</button>
                    </div>
                    <div class="bigbox"></div>
                </div>

            </div>
        </div>

    </div>

