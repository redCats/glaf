<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/inc/init_import.jsp"%>
<%@ include file="/WEB-INF/views/inc/init_config.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%="考核指标列表"%></title>
<meta name="Keywords" content="<%=appKeywords%>" />
<meta name="Description" content="<%=appDescription%>" />
<%@ include file="/WEB-INF/views/inc/init_style.jsp"%>
<%@ include file="/WEB-INF/views/inc/init_script.jsp"%>

<script type="text/javascript">
	var contextPath = "${contextPath}";

	jQuery(function() {
		jQuery('#mydatagrid').datagrid({
			width : 1000,
			height : 480,
			fit : true,
			fitColumns : true,
			nowrap : false,
			striped : true,
			collapsible : true,
			url : '${contextPath}/mx/oa/assessquestion/json',
			sortName : 'id',
			sortOrder : 'desc',
			remoteSort : false,
			singleSelect : true,
			idField : 'qustionid',
			columns : [ [ {
				title : '序号',
				field : 'startIndex',
				width : 20,
				sortable : false
			}, {
				title : '标题',
				field : 'title',
				width : 80,
				sortable : false
			}, {
				title : '有效期',
				field : 'validdate',
				width : 50,
				sortable : false
			}, {
				title : '频率',
				field : 'rate',
				width : 20,
				sortable : false,
				formatter : forRate
			}, {
				title : '是否有效',
				field : 'iseffective',
				width : 20,
				formatter : isEnabled
			} ] ],
			rownumbers : false,
			pagination : true,
			pageSize : 15,
			pageList : [ 10, 15, 20, 25, 30, 40, 50, 100 ]
		});

		var p = jQuery('#mydatagrid').datagrid('getPager');
		jQuery(p).pagination({
			onBeforeRefresh : function() {
				//alert('before refresh');
			}
		});
	});

	function isEnabled(value, row, index) {
		if (value == 1) {
			return "是";
		} else {
			return "否";
		}
	}
	function forRate(value, row, index) {
		if (value == 1) {
			return "季度";
		} else if (value == 2) {
			return "月度";
		} else if (value == 3) {
			return "年度";
		} else {
			return "";
		}
	}
	function addNew() {
		//location.href="${contextPath}/mx/oa/assessquestion/edit";
		var link = '${contextPath}/mx/oa/assessquestion/queryAssessIndex';
		art.dialog.open(link, {
			height : 450,
			width : 980,
			title : "添加记录",
			lock : true,
			scrollbars : "no"
		}, false);
		// window.location.href=link;
	}

	function searchWin() {
		jQuery('#dlg').dialog('open').dialog('setTitle', '考核指标查询');
		//jQuery('#searchForm').form('clear');
	}

	function resize() {
		jQuery('#mydatagrid').datagrid('resize', {
			width : 800,
			height : 400
		});
	}

	function editSelected() {
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if (rows == null || rows.length != 1) {
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected) {
			var link = '${contextPath}/mx/oa/assessquestion/queryAssessIndex?questionId='
					+ selected.qustionid;
			art.dialog.open(link, {
				height : 450,
				width : 980,
				title : "修改记录",
				lock : true
			}, false);
		}
	}

	function viewSelected() {
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if (rows == null || rows.length != 1) {
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected) {
			location.href = "${contextPath}/mx/oa/assessquestion/edit?readonly=true&qustionid="
					+ selected.id;
		}
	}

	function deleteSelections() {
		var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for ( var i = 0; i < rows.length; i++) {
			ids.push(rows[i].id);
		}
		if (ids.length > 0 && confirm("数据删除后不能恢复，确定删除吗？")) {
			var qustionids = ids.join(',');
			jQuery.ajax({
				type : "POST",
				url : '${contextPath}/mx/oa/assessquestion/delete?qustionids='
						+ qustionids,
				dataType : 'json',
				error : function(data) {
					alert('服务器处理错误！');
				},
				success : function(data) {
					if (data != null && data.message != null) {
						alert(data.message);
					} else {
						alert('操作成功完成！');
					}
					jQuery('#mydatagrid').datagrid('reload');
				}
			});
		} else {
			alert("请选择至少一条记录。");
		}
	}

	function reloadGrid() {
		jQuery('#mydatagrid').datagrid('reload');
	}

	function getSelected() {
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected) {
			alert(selected.code + ":" + selected.name + ":" + selected.addr
					+ ":" + selected.col4);
		}
	}

	function getSelections() {
		var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for ( var i = 0; i < rows.length; i++) {
			ids.push(rows[i].code);
		}
		alert(ids.join(':'));
	}

	function clearSelections() {
		jQuery('#mydatagrid').datagrid('clearSelections');
	}

	function loadGridData(url) {
		jQuery.post(url, {
			qq : 'xx'
		}, function(data) {
			//var text = JSON.stringify(data); 
			//alert(text);
			jQuery('#mydatagrid').datagrid('loadData', data);
		}, 'json');
	}

	function searchData() {
		var params = jQuery("#searchForm").formSerialize();
		jQuery.ajax({
			type : "POST",
			url : '${contextPath}/mx/oa/assessquestion/json',
			dataType : 'json',
			data : params,
			error : function(data) {
				alert('服务器处理错误！');
			},
			success : function(data) {
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});

		jQuery('#dlg').dialog('close');
	}
</script>
</head>
<body style="margin: 1px;">
	<div style="margin: 0;"></div>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',split:true,border:true"
			style="height: 40px">
			<div class="toolbar-backgroud">
				<img src="${contextPath}/images/window.png"> &nbsp;<span
					class="x_content_title">岗位考核指标列表：</span> <a href="#"
					class="easyui-linkbutton"
					data-options="plain:true, iconCls:'icon-add'"
					onclick="javascript:addNew();">新增</a> <a href="#"
					class="easyui-linkbutton"
					data-options="plain:true, iconCls:'icon-edit'"
					onclick="javascript:editSelected();">修改</a> <a href="#"
					class="easyui-linkbutton"
					data-options="plain:true, iconCls:'icon-search'"
					onclick="javascript:searchWin();">查找</a>
			</div>
		</div>
		<div data-options="region:'center',border:true">
			<table id="mydatagrid"></table>
		</div>
	</div>
	<div id="edit_dlg" class="easyui-dialog"
		style="width: 400px; height: 280px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons">
		<form id="editForm" name="editForm" method="post"></form>
	</div>
	<div id="dlg" class="easyui-dialog"
		style="width: 400px; height: 280px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons">
		<form id="searchForm" name="searchForm" method="post">
			<table class="easyui-form" align="left">
				<tbody>
					<tr>
						<td>标题</td>
						<td><input id="titleLike" name="titleLike"
							class="easyui-validatebox" type="text"></input></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok"
			onclick="javascript:searchData()">查询</a> <a href="#"
			class="easyui-linkbutton" iconCls="icon-cancel"
			onclick="javascript:jQuery('#dlg').dialog('close')">取消</a>
	</div>
</body>
</html>
<%@ include file="/WEB-INF/views/inc/init_end.jsp"%>