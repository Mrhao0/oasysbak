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

    </style>
    <div class="row" style="padding-top: 10px;">
        <div class="col-md-2">
            <h1 style="font-size: 24px; margin: 0;" class="">发布记录</h1>
        </div>
        <div class="col-md-10 text-right">
            <a href="##"><span class="glyphicon glyphicon-home"></span> 首页</a> > <a
                    disabled="disabled">发布记录</a>
        </div>
    </div>
    <div class="row" style="padding-top: 15px;">
        <div class="col-md-12 thistable">

            <div class="bgc-w box box-primary">
                <!--盒子头-->
                <div class="box-header">
                    <h3 class="box-title">
                        <a href="addproval" class="label label-success" style="padding: 5px;">
                            <span class="glyphicon glyphicon-plus"></span> 新增
                        </a>
                    </h3>
                    <div class="box-tools">
                        <div class="input-group" style="width: 150px;">
                            <input type="text" class="form-control input-sm cha"
                                   placeholder="查找..." />
                            <div class="input-group-btn chazhao">
                                <a class="btn btn-sm btn-default"><span
                                            class="glyphicon glyphicon-search"></span></a>
                            </div>
                        </div>
                    </div>
                </div>
                <!--盒子身体-->
<#--                <div class="box-body no-padding">-->
<#--                    <div class="table-responsive">-->
<#--                        <table class="table table-hover ">-->
<#--                            <tr>-->
<#--                                <th scope="col">名称</th>-->
<#--                                <th scope="col">备注</th>-->
<#--                                <th scope="col">类型</th>-->
<#--                                <th scope="col">操作</th>-->
<#--                            </tr>-->
<#--                            &lt;#&ndash;                            <#list pathList as item>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                <tr>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                    <td><span>${item.name}</span></td>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                    <td><span>${item.path}</span></td>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                    <#if item.remark??>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                        <td><span>${item.remark}</span></td>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                    <#else>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                        <td><span>0</span></td>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                    </#if>&ndash;&gt;-->

<#--                            &lt;#&ndash;                                    <#if item.type==0>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                        <td><span>图纸库</span></td>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                    <#else>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                        <td><span>素材库</span></td>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                    </#if>&ndash;&gt;-->

<#--                            &lt;#&ndash;                                    <td>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                        <a href="burse" class="label xiugai">&ndash;&gt;-->
<#--                            &lt;#&ndash;                                            <span class="glyphicon glyphicon-edit"></span> 发布</a>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                        <a href="burse" class="label shanchu">&ndash;&gt;-->
<#--                            &lt;#&ndash;                                            <span  class="glyphicon glyphicon-remove"></span> 设置</a>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                    </td>&ndash;&gt;-->
<#--                            &lt;#&ndash;                                </tr>&ndash;&gt;-->
<#--                            &lt;#&ndash;                            </#list>&ndash;&gt;-->
<#--                        </table>-->
<#--                    </div>-->
<#--                </div>-->
                <!--盒子尾-->
<#--                <#include "/common/paging.ftl">-->
            </div>
        </div>
    </div>


    <script>
    </script>
