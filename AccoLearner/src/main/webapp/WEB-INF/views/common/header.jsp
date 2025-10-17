<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>

<!-- 부모 jsp와 공유 안함. 재선언 필요 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fm" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!-- 부모 jsp와 공유함 -->
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<!-- 이거는 세션로그인 전용 JSTL임, 세션안쓰면 쓸일 없음 -->
	<c:set var="user" value="${SPRING_SECURITY_CONTEXT.authentication.principal}" />
	<c:set var="auth" value="${SPRING_SECURITY_CONTEXT.authentication.authorities}" />

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1"> <!-- 반응형 웹 필수 코드 -->

  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

  <!-- Bootstrap JS (Popper 포함) -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>

<script type="text/javascript">
	document.addEventListener("DOMContentLoaded", function(){
		console.log("DOM이 준비되었습니다!");
		
		const loginBtn = document.getElementById("login-page-btn");
		loginBtn.addEventListener("click", () => {
			location.href="/login";
		});
		
		
		
	});
	

</script>

<body>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
      <a class="navbar-brand" href="/home">AccoLearner</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarScroll"
        aria-controls="navbarScroll" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarScroll">
        <ul class="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll" style="--bs-scroll-height: 100px;">
          <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="/home">Home</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">소개</a>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
              aria-expanded="false">
             	학습하기
            </a>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" href="#">Action</a></li>
              <li><a class="dropdown-item" href="#">Another action</a></li>
              <li><hr class="dropdown-divider"></li>
              <li><a class="dropdown-item" href="#">Something else here</a></li>
            </ul>
          </li>
          <li class="nav-item">
            <a class="nav-link disabled" aria-disabled="true">Link</a>
          </li>
        </ul>

        <form class="d-flex" role="search">
          <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" />
          <button class="btn btn-outline-success" type="submit">Search</button>
        </form>

        <div class="ms-2">
          <div class="container-fluid justify-content-start">
          	
          	<security:authorize access="!isAuthenticated()">
	            <button class="btn btn-outline-secondary" type="button" id="login-page-btn">로그인</button>
          	</security:authorize>
          	<security:authorize access="isAuthenticated()">
          		<li class="nav-item dropdown">
          			<a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"aria-expanded="false">
          				<security:authentication property="principal.nickname"/>
          			</a>
		            <ul class="dropdown-menu">
              		<li><a class="dropdown-item" href="#">마이페이지</li>
              		<li><hr class="dropdown-divider"></li>
              		<li><a class="dropdown-item" href="#">로그아웃</a></li>
            		</ul>
          		</li>
          	</security:authorize>
          </div>
        </div>
        
        
      </div>
    </div>
  </nav>
</body>
</html>
