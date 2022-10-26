<#include "/common/commoncss.ftl">
<style type="text/css">
    form {

    }

    .all {
        display: flex;
        width: 100%;
        height: 80%;
    }

    .left {
        width: 60%;
        height: 80%;
        margin-left: 15px;
    }

    .right {
        position: relative;
        margin-left: 5%;
        width: 40%;
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

    .deptselects {
        width: 100px;
        height: 25px;

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

    #add {
        border: transparent;
        padding: 8px;
        margin-left: 20px;

    }

    .del {
        margin-left: 27px;
        background-color: red;
        padding: 3px 7px;
        color: white;
        border-radius: 3px;
        border: transparent;
        font-size: 12px;
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
        <img src="imgshow?fileid=184">
    </div>
    <div class="right">

        <div><span><label id="control" class="control-label">部门列表</label>
									<select class="deptselects" name="deptid">
<#--										<#if positiondept??>-->
                                        <#--                                            <option value="${(positiondept.deptId)!''}">${positiondept.deptName}</option>-->
                                        <#--                                        </#if>-->
                                        <#--                                        <#list depts as dept>-->
                                        <#--                                            <option value="${dept.deptId}">${dept.deptName}</option>-->
                                        <#--                                        </#list>-->
									</select></span><span> <input class="text" type="text" placeholder="检查点"/></span>
            <button class="label label-success" id="add">+新增</button>
            <ul></ul>
        </div>
        <div class="next"><a href="" class="btn btn-primary" style="font-size: 13px">下一页</a></div>
    </div>
</div>
<script type="text/javascript">
    var addone = "<div id='allbox'>部门列表:<select class=\"deptselects\" name=\"deptid\"><option value=''> </option></select> <input class=\"text\" type=\"text\" placeholder=\"检查点\"/><button class=\"del\" onclick='myfunction(this)' id =\"dels\">删除</button></br></div>"

    $("#add").on("click", function () {
        $(".right").append(addone)
    })

    // function myfunction(){
    //
    //     alert("123")
    //     this.parentNode.remove()
    //     console.log(this.parentNode)
    //
    // }
    // $(function () {
    //     // $('#dels').on("click", function (row) {
    //         alert('123')
    //         // $(this).parent().remove();
    //     // })
    // })
</script>