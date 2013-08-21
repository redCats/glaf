<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String theme = com.glaf.core.util.RequestUtils.getTheme(request);
	request.setAttribute("theme", theme);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>出差申请审核</title>
<%@ include file="/WEB-INF/views/inc/init_style.jsp"%>
<%@ include file="/WEB-INF/views/inc/init_script.jsp"%>
<script type="text/javascript">
	var contextPath = "${contextPath}";

	function passData() {
		if (confirm("确认审核通过吗？")) {
			var params = jQuery("#iForm").formSerialize();
			params = params + "&approveOpinion="
					+ jQuery('#approveOpinion').val().trim();

			jQuery.ajax({
				type : "POST",
				url : '${contextPath}/mx/oa/ltravel/submitData',
				data : params,
				dataType : 'json',
				error : function(data) {
					alert('服务器处理错误！');
				},
				success : function(data) {
					if (data != null && data.message != null) {
						alert(data.message);
					} else {
						alert('审批成功！');
					}
					jQuery('#dlgApprove').dialog('close');
					if (window.opener) {
						window.opener.location.reload();
					} else if (window.parent) {
						window.parent.location.reload();
					}
				}
			});
		}
	}

	function noPassData() {
		if (jQuery('#approveOpinion').val().trim() == "") {
			alert("审批不通过需填写审批意见。");
			return;
		}
		if (confirm("确认审核不通过吗?")) {
			var params = jQuery("#iForm").formSerialize();
			params = params + "&approveOpinion="
					+ jQuery('#approveOpinion').val().trim();
			jQuery.ajax({
				type : "POST",
				url : '${contextPath}/mx/oa/ltravel/rollbackData',
				data : params,
				dataType : 'json',
				error : function(data) {
					alert('服务器处理错误！');
				},
				success : function(data) {
					if (data != null && data.message != null) {
						alert(data.message);
					} else {
						alert('该出差单已退回！');
					}
					jQuery('#dlgApprove').dialog('close');
					if (window.opener) {
						window.opener.location.reload();
					} else if (window.parent) {
						window.parent.location.reload();
					}
				}
			});
		}
	}

	jQuery(function() {
		if(${lookover}){
		  jQuery("#reviewDiv").hide();
		}
	});

	function approveWin() {
		//jQuery('#dlgApprove').dialog('open').dialog('setTitle','批量审批');
		$('#dlgApprove').dialog('open').dialog({
			title : '审核',
			left : 420,
			top : 100,
			closed : false,
			modal : true
		});
		jQuery('#dlgApprove').form('clear');
	}
</script>
</head>

<body>
	<div style="margin: 0;"></div>

	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',split:true,border:true"
			style="height: 40px">
			<div class="toolbar-backgroud">
				<span class="x_content_title" id="titleSpan">出差申请审核</span> <a
					id="reviewDiv" href="#" class="easyui-linkbutton"
					data-options="plain:true, iconCls:'icon-submit'"
					onclick="javascript:approveWin()">审核</a> <a href="#"
					class="easyui-linkbutton"
					data-options="plain:true, iconCls:'icon-cancel'"
					onclick="javascript:art.dialog.close();">关闭</a>
			</div>
		</div>

		<div data-options="region:'center',border:false,cache:true">
			<form id="iForm" name="iForm" method="post">
				<input type="hidden" id="travelid" name="travelid"
					value="${ltravel.travelid}" /> <input type="hidden"
					id="processInstanceId" name="processInstanceId"
					value="${ltravel.processinstanceid}" />
				<table class="easyui-form" style="width: 700px;" align="center">
					<tbody>
						<tr>
							<td width="20%" align="left">申请单位：</td>
							<td align="left"><input id="company" class="easyui-combobox"
								name="company" value="${ltravel.company}" disabled="disabled"
								data-options="required:true,valueField:'code',textField:'name',url:'${contextPath}/rs/dictory/jsonArray/${ltravel.area}'" />
							</td>
							<td width="20%" align="left">地区：</td>
							<td align="left"><input id="area" class="easyui-combobox"
								name="area" value="${ltravel.area}" disabled="disabled"
								data-options="required:true,valueField:'code',textField:'name',url:'${contextPath}/rs/dictory/jsonArray/eara'" />
							</td>
							<td width="20%" align="left">部门：</td>
							<td align="left"><input id="dept" class="easyui-combobox"
								name="dept" value="${ltravel.dept}" disabled="disabled"
								data-options="required:true,	valueField:'code',textField:'name',url:'${contextPath}/rs/dictory/deptJsonArray/012'" />
							</td>
						</tr>
						<tr>
							<td width="20%" align="left">申请人：</td>
							<td align="left"><input id="appuser" class="easyui-combobox"
								name="appuser" value="${ltravel.appuser}" disabled="disabled"
								data-options="required:true,valueField:'code',textField:'name',url:'${contextPath}/mx/oa/baseData/getUserJson'" />
							</td>
							<td width="20%" align="left">职位：</td>
							<td align="left"><input id="post" name="post" type="text"
								disabled="disabled" class="easyui-validatebox"
								value="${ltravel.post}" /></td>
							<td width="20%" align="left">申请日期：</td>
							<td align="left"><input id="appdate" name="appdate"
								type="text" disabled="disabled" class="easyui-datebox"
								data-options="required:true" value="${appdate}"
								pattern="yyyy-MM-dd" /></td>
						</tr>
						<tr>
							<td width="20%" align="left">出差地点：</td>
							<td align="left" colspan="5"><textarea id="traveladdress"
									name="traveladdress" style="width: 99%"
									data-options="required:true" disabled="disabled"
									class="easyui-validatebox">${ltravel.traveladdress}</textarea>
							</td>
						</tr>
						<tr>
							<td width="20%" align="left">起讫时间：</td>
							<td align="left" colspan="5"><input id="startdate"
								name="startdate" type="text" class="easyui-datebox"
								style="width: 130px" data-options="required:true"
								missingMessage="日期必须填写" editable="false" disabled="disabled"
								value="${startdate}" pattern="yyyy-MM-dd" />至 <input
								id="enddate" name="enddate" type="text" class="easyui-datebox"
								style="width: 130px" data-options="required:true"
								missingMessage="日期必须填写" editable="false" disabled="disabled"
								value="${enddate}" pattern="yyyy-MM-dd" />共 <input
								id="travelnum" name="travelnum" type="text"
								data-options="required:true" disabled="disabled"
								class="easyui-numberbox" precision="2" style="width: 25px"
								value="${ltravel.travelnum}" />天</td>
						</tr>
						<tr>
							<td width="20%" align="left">事由：</td>
							<td align="left" colspan="5"><textarea id="content"
									name="content" style="width: 99%" disabled="disabled"
									class="easyui-validatebox">${ltravel.content}</textarea></td>
						</tr>
						<tr>
							<td width="20%" align="left">附件：</td>
							<td align="left" colspan="5"><a
								href="javascript:uploadFile(8, ${ltravel.travelid}, 0)">附件上传</a>
								<jsp:include page="/others/attachment/showCount">
									<jsp:param name="referType" value="8" />
									<jsp:param name="referId" value="${ltravel.travelid}" />
								</jsp:include></td>
						</tr>
						<tr>
							<td align="center" colspan="6"><%@ include
									file="/WEB-INF/views/oa/common/task.jsp"%>
							</td>

						</tr>

					</tbody>
				</table>
				<%@ include file="/WEB-INF/views/oa/common/approve_foot.jsp"%>
			</form>

		</div>
	</div>
</body>
</html>
<%@ include file="/WEB-INF/views/inc/init_end.jsp"%>