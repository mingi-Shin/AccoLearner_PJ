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
<title>로그인 페이지</title>
<script type="text/javascript">
	document.addEventListener("DOMContentLoaded", function(){
		
		//예외처리
	
		//로그인 post 비동기 처리 	
		loginProcess();
		
		//로컬스토리지 저장된 아이디 불러오기 
		getSavedId();
		
	}); //DOMContentLoaded
	
	function loginProcess() { 
		document.getElementById("login-process-btn").addEventListener("click", async () => {
		  const username = document.getElementById("login-id").value.trim();
		  const password = document.getElementById("login-pwd").value.trim();
		  const errorDiv = document.getElementById("login-error");
		  
		  //아이디 저장 여부 
		  let rememberId = document.querySelector('#save-id').checked;
		  if(rememberId){
			  localStorage.setItem('rememberId', username);
		  } else {
			  localStorage.removeItem('rememberId');
		  }
		  
		  // 로그인 요청 
		  if(!username||!password){
			  errorDiv.textContent = "아이디와 비밀번호를 입력해주세요.";
			  errorDiv.style.display='block';
			  return;
		  }
		  
		  try {
			  const response = await fetch("/api/auth/login", {
				  method : "POST",
				  headers : {"Content-Type" : "application/json"},
				  body : JSON.stringify({username, password})
			  });
			  
/* fetch()함수로 서버에 요청을 보내면 HTTP응답 전체 정보(헤더, 상태코드, 바디 등)을 담고있는 Response 객체로 반환돼요.
 * 	즉,response.json() = 응답 본문 JSON 문자열 → JS 객체로 변환
		await을 붙였으니까 JSON파싱이 끝날 때까지 기다렸다가 그 결과(객체)를 result에 저장해요.
*/			  
/**
// 이후 다른 API 호출 시
	const response = await fetch("/api/some-endpoint", {
	    method: "GET",
	    headers: {
	        "Authorization": `Bearer ${localStorage.getItem("accessToken")}`
	    }
	});
*/
			  const result = await response.json();
			  
			  if(result.success){
				  localStorage.setItem("accessToken", result.accessToken);
				  location.href = "/";
				  
			  } else {
				  alert(result.message);
				  errorDiv.textContet = "아이디 또는 비밀번호가 잘못되었습니다.";
				  errorDiv.style.display = 'block';
			  }
		  } catch(err) {
			  console.log(err);
			  errorDiv.textContent ="서버 오류가 발생했습니다.";
			  errorDiv.style.display = 'block';
		  }
		});
	}//loginProcess
	
	//저장된 아이디 불러오기 
	function getSavedId(){
		const userId = localStorage.getItem('rememberId');
		if(userId !== null){
			document.getElementById("login-id").value = userId;
			document.querySelector('#save-id').checked = true;
		} 
	}
	
</script>
</head>
<body>
	<div class="mx-auto" style="width: 80%;">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
	</div>
  <div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
    
    <!-- 로그인 카드 -->
    <div class="card p-4" style="max-width: 500px; width: 100%;">
    
    	<!-- Logo 와 사이트 소개란 -->
    	<div class="d-flex flex-column mb-4">
      	<div class="d-flex justify-content-center align-items-center mb-2 ">
      		<img alt="로고" src="${contextPath }/resources/images/AccoLearner_Logo.png" style="max-width: 300px; max-height: 200px;" >
      	</div>
    		<h2 class="mt-6 text-center font-bold text 2xl">즐거운 회계 공부를 시작해보세요! </h2>
   		</div>
    
      <!-- 소셜 로그인 -->
      <div class="text-center">
      
        <div class="mb-2">소셜 계정으로 간편 로그인</div>
        <div class="d-flex justify-content-center gap-5 mb-3">
          <button class="btn btn-outline-success w-25 me-1 " onclick="alert('네이버 로그인')">N</button>
<!--      <button class="btn btn-outline-warning w-100 mx-1" onclick="alert('카카오 로그인')">💬</button> -->
          <button class="btn btn-outline-danger w-25 mx-1" onclick="alert('구글 로그인')">G</button>
<!--      <button class="btn btn-outline-primary w-100 mx-1" onclick="alert('페이스북 로그인')">f</button> -->
<!--      <button class="btn btn-outline-dark w-100 ms-1" onclick="alert('애플 로그인')">🍎</button> -->
        </div>
      </div>
      
      <!-- 경계선 -->
			<div class="position-relative my-4">
			  <div class="d-flex align-items-center justify-content-center">
			    <hr class="flex-grow-1 border-top border-secondary opacity-40">
			    <span class="mx-3 text-muted bg-white px-2">
			      AccoLearner 아이디로 로그인
			    </span>
			    <hr class="flex-grow-1 border-top border-secondary opacity-40">
			  </div>
			</div>

      
      <!-- 로그인 폼 -->
      <form action="#" method="post" id="login-form">
        <div class="mb-3"> 
          <input type="text" class="form-control" id="login-id" name="username" placeholder="아이디" required>
        </div>
        <div class="mb-3">
          <input type="password" class="form-control" id="login-pwd" name="password" placeholder="비밀번호" required>
          <div id="login-error" class="text-danger mt-1" style="display: none;"></div>
        </div>
        
        <div class="d-flex justify-content-between mb-3">
          <div class="form-check">
            <input type="checkbox" class="form-check-input" id="keep-login">
            <label class="form-check-label" for="keep-login">로그인 유지</label>
          </div>
          <div class="form-check">
            <input type="checkbox" class="form-check-input" id="save-id">
            <label class="form-check-label" for="save-id">아이디 저장</label>
          </div>
        </div>
        
        <button type="button" id="login-process-btn" class="btn btn-primary w-100 mb-3">로그인</button>
      </form>
      
      <!-- 아이디/비번 찾기 & 회원가입 -->
      <div class="d-flex justify-content-center mb-3 gap-2">
        <a href="#" onclick="alert('아이디 찾기 기능')">계정찾기</a>
        <span>|</span>
        <a href="${contextPath }/join" id="join-page" onclick="joinPage()">회원가입</a>
      </div>
      

      
    </div>
    
  </div>
  
 	<div class="mx-auto" style="width: 80%;">
		<jsp:include page="/WEB-INF/views/common/bottom.jsp" />
	</div>
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

...
position-relative					기준 컨테이너 지정
my-4											위아래 여백
text-center								텍스트 가운데 정렬
<hr> + border-top					가로 실선
opacity-50								회색 선의 투명도 조절
position-absolute					텍스트를 선 위 중앙에 배치
top-50 start-50 translate-middle	정확히 중앙 정렬
bg-white									배경 흰색 (선과 겹치지 않게)
px-3											좌우 여백
text-muted								회색 텍스트

...

m	모든 방향 마진
mt	margin-top (위쪽)
mb	margin-bottom (아래쪽)
ms	margin-left (start, 왼쪽)
me	margin-right (end, 오른쪽)
mx	margin-left + margin-right (가로)
my	margin-top + margin-bottom (세로
-->