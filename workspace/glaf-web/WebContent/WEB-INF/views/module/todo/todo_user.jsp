<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*"%>
<%@ page import="com.glaf.base.modules.todo.*"%>
<%@ page import="com.glaf.base.modules.todo.model.*"%>
<%@ page import="com.glaf.base.modules.todo.service.*"%>
<%@ page import="com.glaf.base.utils.*"%>
<%@ page import="com.glaf.base.modules.sys.*"%>
<%@ page import="com.glaf.base.modules.sys.model.*"%>
<%@ page import="com.glaf.base.modules.sys.service.*"%>
<%@ page import="org.jpage.util.*" %>
<%@ page import="org.jpage.core.query.paging.*" %>
<%@ page import="com.glaf.base.modules.*" %>
<%@ page import="com.glaf.base.modules.todo.service.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%
    String context = request.getContextPath();
    SysUser user = com.glaf.base.utils.RequestUtil.getLoginUser(request);
	Map params = new HashMap();
	params.put("actorIdx", user.getAccount());
	TodoJobBean bean = (TodoJobBean)BaseDataManager.getInstance().getBean("todoJobBean");
	Collection rows = bean.getToDoInstanceList(params);
	Map dataMap = new LinkedHashMap();
	Map todoMap = bean.getToDoMap();
	Map userMap = bean.getUserMap();

	if(rows != null && rows.size()> 0){
			  Iterator iterator008 = rows.iterator();
			  while(iterator008.hasNext()){
                   ToDoInstance tdi = (ToDoInstance)iterator008.next();
				   int status = TodoConstants.getTodoStatus(tdi);
				   tdi.setStatus(status);
				   ToDoInstance xx = (ToDoInstance)dataMap.get(tdi.getActorId());
				   if(xx == null){
					   xx = new ToDoInstance();
					   xx.setActorId(tdi.getActorId());
					   SysUser u = (SysUser)userMap.get(tdi.getActorId());
					   if(u != null){
						   xx.setActorName(u.getName());
					   }
				   }

                   switch(status){
					    case TodoConstants.OK_STATUS:
							xx.setQty01(xx.getQty01()+1);
							break;
						case TodoConstants.CAUTION_STATUS:
							xx.setQty02(xx.getQty02()+1);
							break;
						case TodoConstants.PAST_DUE_STATUS:
							xx.setQty03(xx.getQty03()+1);
							break;
						default:
							break;
				   }

				   dataMap.put(tdi.getActorId(), xx);

			  }
	}

	rows = dataMap.values();

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>基础平台系统</title>
<link href="../css/site.css" rel="stylesheet" type="text/css">
<link href="<%=context%>/css/site.css" rel="stylesheet" type="text/css">
<script src="<%=context%>/scripts/main.js" language="javascript"></script>
<style type="text/css"> 
@import url("<%=context%>/scripts/hmenu/skin-yp.css");
.STYLE1 {color: #FF0000}
</style>
<script type="text/javascript">
_dynarch_menu_url = "<%=context%>/scripts/hmenu";
</script>
<script type="text/javascript" src="<%=context%>/scripts/hmenu/hmenu.js"></script>
<script type="text/javascript" src="<%=context%>/scripts/main.js"></script>
<script type="text/javascript" src="<%=context%>/scripts/site.js"></script>
<script type="text/javascript">

</script>
</head>

<body id="document">
<br><br>
<table align="center" width="90%" border="0" cellspacing="1" cellpadding="0" class="list-box">
          <tr class="list-title">
            <td align="center">用户</td>
            <td width="95" align="center">PastDue</td>
            <td width="95" align="center">Caution</td>
            <td width="95" align="center">OK</td>
          </tr>
		  <%if(rows != null && rows.size()> 0){
			  Iterator iterator008 = rows.iterator();
			  while(iterator008.hasNext()){
                   ToDoInstance tdi = (ToDoInstance)iterator008.next();
				   pageContext.setAttribute("tdi",tdi);
			  %>
          <tr class="list-a">
            <td height="20" align="left">
			<a href="todo_list.jsp?query_actorId=<c:out value="${tdi.actorId}"/>" >
			<c:out value="${tdi.actorName}"/>
			</a>
			</td>
            <td align="center" class="red">
			     <c:out value="${tdi.qty03}"/>
			</td>
            <td align="center" class="yellow">
			     <c:out value="${tdi.qty02}"/>
			</td>
            <td align="center">
			     <c:out value="${tdi.qty01}"/>
			</td>
          </tr>
		  <%     }
				  }
		  %>
        </table>