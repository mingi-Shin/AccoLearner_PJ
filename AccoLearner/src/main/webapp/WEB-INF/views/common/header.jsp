<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 부모 jsp 공유 안함. 재선언 필요 -->
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fm" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!-- 부모 jsp와 공유 -->
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${SPRING_SECURITY_CONTEXT.authentication.principal }" />
<c:set var="auth" value="${SPRING_SECURITY_CONTEXT.authentication.authorities }" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1"> <!-- 반응형 웹 필수 코드 -->

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Bootstrap JS (Popper 포함) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<title>header</title>
</head>
<body>

</body>
</html>