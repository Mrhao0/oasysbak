<div class="bgc-w box box-primary">
    <!--盒子头-->
    <div class="box-header">
        <h3 class="box-title">
            <a href="" class="label label-success" style="padding: 5px;margin-left:5px;">
                <span class="glyphicon glyphicon-refresh"></span> 刷新
            </a>
        </h3>
        <div class="box-tools">
            <div class="input-group" style="width: 150px;">
                <input type="text" class="form-control input-sm baseKey" placeholder="按标题查找" value="${(baseKey)!''}"/>
                <div class="input-group-btn">
                    <a class="btn btn-sm btn-default baseKetsubmit"><span
                                class="glyphicon glyphicon-search"></span></a>
                </div>
            </div>
        </div>
    </div>
    <!--盒子身体-->
    <div class="box-body no-padding">
        <div class="table-responsive">
            <table class="table table-hover">
                <tr>
                    <th style="text-align: center" scope="col"><span class="paixu thistype">类型
						<#if type?? && icon??>
                            <span class="glyphicon ${icon}"></span>
                        </#if>
						</span></th>
                    <th style="text-align: center" scope="col"><span class="paixu thisstatus">状态
						<#if status?? && icon??>
                            <span class="glyphicon ${icon}"></span>
                        </#if>
						</span></th>
                    <th style="text-align: center" scope="col">标题</th>
                    <th style="text-align: center" scope="col"><span class="paixu thistime">发布时间
						<#if time?? && icon??>
                            <span class="glyphicon ${icon}"></span>
                        </#if>
						</span></th>
                    <th style="text-align: center" scope="col">发布人</th>
                    <th style="text-align: center" scope="col">部门</th>
                    <th style="text-align: center" scope="col">详情</th>
                    <#--                    <th style="text-align: center" scope="col">置顶</th>-->
                    <#--                    <th style="text-align: center" scope="col">链接</th>-->
                    <th style="text-align: center" scope="col">操作</th>
                </tr>


                <#list list as this>
                    <tr>
                        <td style="text-align: center"><span class="label ${(this.statusColor)!''}"
                                                             style="background: #ccc;color: black">${this.type}</span>
                        </td>
                        <td style="text-align: center"><span
                                    class="label ${(this.statusColor)!''}">${this.status}</span></td>
                        <td style="text-align: center"><span>${this.title}</span></td>
                        <td style="text-align: center"><span>${this.modify_time}</span></td>
                        <td style="text-align: center"><span>${this.userName}</span></td>
                        <td style="text-align: center"><span>${this.deptName}</span></td>
                        <td style="text-align: center"><span
                                    title="${this.content2}"><#if this.content2?length gt 16>${this.content2?substring(0,15)}...<#else>${this.content2}</#if></span>
                        </td>
                        <#--                        <td style="text-align: center"><span></span></td>-->
                        <#--                        <td style="text-align: center"><span></span></td>-->

                        <td style="text-align: center">
                            <a href="newpdf?fileid=${this.file_id2}" class="label xiugai chakan look"
                               style="font-size: 13px" id="${this.notice_id}">查看</a>
                            <#if positionid?if_exists?number==1>
                            <#--							<a onclick="{console.log(${this.notice_id},$(this).parents('tr'));return confirm('删除该记录将不能恢复，确定删除吗？');};" href="" class="label shanchu"  style="font-size: 13px" id="${this.notice_id}">删除</a>-->
                            <#if (this.status_id)?number!=25>
                                <a onclick="{console.log(${this.file_id2},$(this).parents('tr'));};"
                                   href="tocheck?noticesListId=${this.notice_id}&fileid=${this.file_id2}"
                                   class="label xinzeng chakan forwardthis" id="${this.notice_id}"
                                   style="font-size: 13px">审核</a>
                            <#else>
                                <a class="label chakan forwardthis" id="${this.notice_id}"
                                   style="font-size: 13px;pointer-events: none;color: #999;">审核</a>
                            </#if>
                            </#if>

                        </td>


                        <#--<td>${this}</td>-->
                        <#--					<td><span class="label ${(this.statusColor)!''}">${this.status}</span></td>-->
                        <#--					<#if this.is_read==0>-->
                        <#--					<td class="c"><span>${(this.title)!''}</span></td>-->
                        <#--					<#else>-->
                        <#--					<td><span>${(this.title)!''}</span></td>-->
                        <#--					</#if>-->
                        <#--					<td><span>${this.notice_time}</span></td>-->
                        <#--					<td><span>${this.userName}</span></td>-->
                        <#--					<td><span>${this.deptName}</span></td>-->
                        <#--					<#if this.is_top==1>-->
                        <#--					<td><span class="labels"><label><input-->
                        <#--								type="checkbox" checked disabled><i>✓</i></label></span></td>-->
                        <#--					<#else>-->
                        <#--					<td><span class="labels"><label><input-->
                        <#--								type="checkbox" disabled><i>✓</i></label></span></td></#if>-->
                        <#--					<#if this.url!=''>-->
                        <#--					<td><span class="glyphicon glyphicon-link"></span></td>-->
                        <#--					<#else>-->
                        <#--					<td><span class="labels"></span></td></#if>-->
                        <#--					<td><a href="informshow?id=${this.notice_id}&read=${this.is_read}&relationid=${this.relatin_id}"-->
                        <#--						class="label xiugai chakan"><span class="glyphicon glyphicon-search"></span>-->
                        <#--							查看</a> -->
                        <#--							<#if this.contain!=1>-->
                        <#--								<#if this.contain==3>-->
                        <#--									<a href="forwardother?noticeId=${this.notice_id}" onclick="{return confirm('确定转发给自己的下属吗？');};" class="label xinzeng chakan forwardthis"><span class="glyphicon glyphicon-log-out"></span>-->
                        <#--									转发</a> -->
                        <#--								<#else>-->
                        <#--									<a href="javascript:void(0);" class="label sheding chakan"><span class="glyphicon glyphicon-log-out"></span>-->
                        <#--									已转发</a> -->
                        <#--								</#if>-->
                        <#--							</#if>-->
                        <#--							<#if this.is_read==0> -->
                        <#--							<#else> -->
                        <#--							<a onclick="{return confirm('删除该记录将不能恢复，确定删除吗？');};"-->
                        <#--							href="informlistdelete?id=${this.notice_id}" class="label shanchu"> -->
                        <#--							<span class="glyphicon glyphicon-remove"></span> 删除-->
                        <#--						</a></#if></td>-->
                    </tr>
                </#list>
            </table>
        </div>
    </div>
    <!--盒子尾-->
    <#include "/common/pagingmybatis.ftl">
</div>
<script>

    $(function () {

        $(".chakan").click(function () {
            var $information = $(this).parents("td").siblings(".c").find("span").text();
            if ($information != "") {
                parent.changeinformation();
            }
        });
    });
</script>