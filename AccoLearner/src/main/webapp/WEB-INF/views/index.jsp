<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 부모 jsp 공유 안함. 재선언 필요 -->
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fm" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Main 페이지입니다.</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container">
	
	<jsp:include page="/WEB-INF/views/common/bottom.jsp" />
</div>

</body>
</html>