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
	<div class="d-flex justify-content-center align-items-center flex-column mt-5">
		<div >
			<h1>모두를 위한 기초 회계 공부서비스</h1>
		</div>
		<div>
			<img alt="회계바탕화면" src="${contextPath }/resources/images/accounting_backImg.png" style="max-width: 400xp; max-height: 300px;">
		</div>
	</div>

	
	<jsp:include page="/WEB-INF/views/common/bottom.jsp" />
</div>

</body>
</html>