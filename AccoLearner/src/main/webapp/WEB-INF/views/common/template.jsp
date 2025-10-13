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
<title>This is a Template</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
	
	<div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;"> 
    
	  <!-- 카드 -->
	  <div class="card p-4" style="max-width: 800px; width: 100%;">
	      
	  	<!-- 폼 -->
	    <form action="#" method="post" id="login-form">
	    </form>
	    
		</div>
	
	</div>

<jsp:include page="/WEB-INF/views/common/bottom.jsp" />
</body>
</html>
<!--
mb-3 -> margin-bottom, 1rem; 폼에서 input, label, 버튼 등 요소 사이의 간격(여백)을 만들 때 사용
w-100 -> width : 100%, 버튼이 부모 컨테이너의 전체 너비를 채우도록 만듦 
d-felx -> display : flex;, flexbox를 쓰면 내부 요소를 수평, 수직 정렬하거나 간격 조정이 쉬움 
justify-content-center -> Flexbox 속성 justify-content : center;, 수평 방향 중앙 정렬.. 즉 div안의 자식요소들이 가로 중앙에 모여서 배치됨 
justify-content-between -> 첫번째 요소는 왼쪽 끝, 마지막 요소는 오른쪽 끝, 나머지는 균등하게 사이 간격 
gap-2 -> 요소 사이에 약간의 여백 추가 
align-items-center -> 일단 Flexbox 레이아웃 안에서만 의미있음, 자식 요소를 수직 중앙 정렬 
min-height: 100vh -> 해당 영역의 높이를 화면크기의 100%에 맞춘다는 의미. 200을 주면 해당 영역의 높이가 모니터 화면의 두배길이로 설정됨 

...

수평 정렬은 justify-content
수직 정렬은 align-items
둘다 쓰면 정중앙에 배치 
-->