<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="html"%>
<%@ page import="java.util.*"%>
<%@ page import="com.glaf.base.modules.*"%>
<%@ page import="com.glaf.base.modules.sys.*"%>
<%@ page import="com.glaf.base.modules.sys.model.*"%>
<%@ page import="com.glaf.base.modules.workspace.model.*"%>
<%@ page import="com.glaf.base.utils.*"%>
<%
  Message bean = (Message) request.getAttribute("bean");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/<%=com.glaf.core.util.RequestUtils.getTheme(request)%>/site.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/<%=com.glaf.core.util.RequestUtils.getTheme(request)%>/site.css">
<script type='text/javascript' src="<%= request.getContextPath() %>/scripts/css.js"></script>
<script type='text/javascript' src='<%= request.getContextPath() %>/scripts/main.js'></script>
<script type='text/javascript' src="<%= request.getContextPath() %>/scripts/verify.js"></script>
<title>消息内容</title>
</head>

<body>
<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td class="nav-title"><span class="Title">工作台</span>&gt;&gt; 消息内容</td>
  </tr>
</table>
<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" class="box">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr class="box">
        <td class="box-lt">&nbsp;</td>
        <td class="box-mt">&nbsp;</td>
        <td class="box-rt">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="box-mm">
      <table width="95%" border="0" align="center" cellpadding="6" cellspacing="0">
        <%
					int sended = ParamUtil.getIntParameter(request, "sended", 0);
					if (sended == 1) {
				%>
				<tr>
          <td width="50" class="input-box">收件人</td>
          <td><%= bean.getRecverList() %></td>
        </tr>
				<%
					} else {
						String senderName = bean.getSender() == null ? "" : bean.getSender().getName();
						String colorClass = "";
						if (bean.getType() == 0) {
							String sysType = bean.getSysType()==0?"Alarm":"News";
							senderName = "系统自动("+sysType+")";
							colorClass = "redcolor";
						}
				%>
				<tr>
          <td width="50" class="input-box">发件人</td>
          <td class="<%= colorClass %>"><%= senderName %></td>
        </tr>
				<%
					}
				%>
        <tr>
          <td class="input-box">日&nbsp;&nbsp;期</td>
          <td><%= WebUtil.dateToString(bean.getCreateDate(), "yyyy-MM-dd HH:mm:ss") %></td>
        </tr>
        <tr>
          <td class="input-box">主&nbsp;&nbsp;题</td>
          <td><%= bean.getTitle() %></td>
          </tr>
        <tr>
          <td valign="top" class="input-box2">内&nbsp;&nbsp;容</td>
          <td><%= StringUtil.replace(bean.getContent(), "\n", "<br>") %></td>
        </tr>
        <tr>
          <td height="30" colspan="2" align="center"><input name="btn_close" type="button" class="button" value="关闭" onClick="javascript:self.close()"></td>
        </tr>
      </table>
        </form>
    </td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr class="box">
        <td class="box-lb">&nbsp;</td>
        <td class="box-mb">&nbsp;</td>
        <td class="box-rb">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
