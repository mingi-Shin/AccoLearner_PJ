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
<title>회원가입 페이지</title>
<style>
  body {
    background-color: #f8f9fa;
  }
  .card {
    border-radius: 1rem;
    box-shadow: 0 0 10px rgba(0,0,0,0.1);
  }
  .input-group-text {
    min-width: 130px;
    justify-content: center;
  }
  .form-section {
    margin-bottom: 1.2rem;
  }
  
  button {
  	min-width: 120px;
  }
  @media (max-width: 576px) {
    .input-group-text {
      min-width: 80px;
      font-size: 0.9rem;
    }
  }
</style>
  
<script type="text/javascript">

document.addEventListener("DOMContentLoaded", function(){
	
	joinUser(); //회원가입
	
	duplicateUserBy(); //중복검사 
	
	
	
});
	
//회원가입 api 
function joinUser(){
	const joinBtn = document.getElementById("join-btn");
	
	joinBtn.addEventListener("click", async () => {

		const loginId = document.getElementById("user-id").value;
		const password = document.getElementById("user-password").value;
		const nickname = document.getElementById("nickname").value;
		const email = document.getElementById("user-email").value;
		const emailSubscribed = document.getElementById("email-sub").checked;
		const kakaoSubscribed = document.getElementById("kakao-sub").checked;
		
		try {
			const response = await fetch("/api/join", {
				method : "POST",
				headers : {"Content-Type": "application/json"},
				body : JSON.stringify({loginId, password, nickname, email, emailSubscribed, kakaoSubscribed})
			});
			
/* 	서버에 요청을 보내면 HTTP응답 전체 정보(헤더, 상태코드, 바디 등)을 담고있는 Response 객체로 반환돼요.
 * 	즉,response.json() = 응답 본문 JSON 문자열 → JS 객체로 변환
		await을 붙였으니까 JSON파싱이 끝날 때까지 기다렸다가 그 결과(객체)를 result에 저장해요.
		...
		내부에서 await 덕분에 비동기 작업을 동기처럼 처리.. 
		async :	함수 선언에 붙임,	함수가 Promise를 반환하고, 내부에서 await 사용 가능, 이 함수 안에는 기다림이 있을 수 있어!
		await	: Promise 앞에 붙임,	 Promise가 완료될 때까지 기다리고 결과를 반환, 이 부분은 결과 나올 때까지 잠깐 기다릴게!
*/
			const result = await response.json();
			
			if(result.success){
				location.href = "/login";
			} else {
				//가입실패 
			}
		} catch (err){
			console.log(err);
			//서버오류
		}
	});
}

/**
 *	아이디, 이메일, 닉네임 중복체크
 */
function duplicateUserBy(){
	
	document.getElementById('id-dupl-btn').addEventListener('click', function(){
		const loginId = document.getElementById('user-id').value;
		duplicateUser('loginId', loginId);
	});
	
	document.getElementById('nickname-dupl-btn').addEventListener('click', function(){
		const loginId = document.getElementById('nickname').value;
		duplicateUser('nickname', loginId);
	});
	
	//-------이메일 중복검사는 input이벤트로 처리 + debounce ------
	document.getElementById('user-email').addEventListener('input', function(){
		let userEmail = document.getElementById('user-email').value;
		duplicateUser('nickname', userEmail);
		
	});
	
	
}
async function duplicateUser(field, value){ //async는 함수선언 앞에 
	
	if(value == null || value.trim() === ""){
		alert(" 빈칸입니다.");
		return;
	}
	
	try {
		const response = await fetch('/api/user/duplicate', {
			method : "POST",
			headers : {"Content-Type" : "application/json"},
			body : JSON.stringify({ "checkField": field,  "checkValue" : value}) //JS객체 -> JSON문자열로 변환 
		});	
		
		const result = await response.json();
		
		if(result.isDuplicated){
			alert(value + "는 사용하실 수 없습니다.");
		}
		if(!result.isDuplicated){
			alert(value + "는 사용가능합니다.");
		}
		
	} catch (error) {
			console.error("중복 체크 에러:", error);
      alert("서버와 통신 중 오류가 발생했습니다.");
	}
	
}



/******************************************************************
 
// 1. debounce 함수 정의
function debounce(func, delay) {
  let timer;
  return function(...args) {
    clearTimeout(timer);               // 기존 타이머 제거
    timer = setTimeout(() => {
      func.apply(this, args);          // delay 후 실행
    }, delay);
  };
}

// 2. 콘솔 찍는 함수
function sayHello(text) {
  console.log("Hello!", text);
}

// 3. 디바운스 적용
const debouncedHello = debounce(sayHello, 1500);

// 4. 이벤트 시뮬레이션
document.addEventListener('input', () => {
  debouncedHello("민기님");
});

위처럼 재사용 하지 않을거면, 함수하나에 그냥 박아버려
=> 

let timer;
input.addEventListener('input', () => {
  clearTimeout(timer);
  timer = setTimeout(() => {
    duplicateUser('email', emailValue);
  }, 1500);
});
 ******************************************************************/
	

</script>

</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
	<div class="card p-4" style="max-width: 600px; width: 100%;">
	  <h4 class="text-center mb-4">회원가입</h4>
	
	  <form action="${contextPath}/join" method="post">
	
	    <!-- 아이디 -->
	    <div class="input-group form-section">
	      <span class="input-group-text">아이디</span>
	      <input type="text" class="form-control" name="username" id="user-id" placeholder="아이디 입력">
	      <button type="button" class="btn btn-outline-secondary" id="id-dupl-btn">중복확인</button>
	    </div>
	
	    <!-- 비밀번호 -->
	    <div class="input-group form-section">
	      <span class="input-group-text">비밀번호</span>
	      <input type="password" class="form-control" name="password" id="user-password" placeholder="비밀번호 입력">
	    </div>
	    <!-- 비밀번호 확인 -->
	    <div class="input-group form-section">
	      <span class="input-group-text">비밀번호 확인</span>
	      <input type="password" class="form-control" id="user-password-confirm" placeholder="비밀번호 입력">
	    </div>
	
	    <!-- 이메일 -->
	    <div class="input-group form-section">
	      <span class="input-group-text">이메일</span>
	      <input type="email" class="form-control" name="email" id="user-email" placeholder="example@domain.com">
	      <button type="button" class="btn btn-outline-secondary" id="email-send-btn">인증코드 전송</button>
	    </div>
	
	    <!-- 인증번호 -->
	    <div class="input-group form-section">
	      <span class="input-group-text">인증번호</span>
	      <input type="text" class="form-control" id="email-code" placeholder="인증번호 입력">
	      <button type="button" class="btn btn-outline-success" id="email-confirm-btn">확인</button>
	    </div>
	
	    <!-- 닉네임 -->
	    <div class="input-group form-section">
	      <span class="input-group-text">닉네임</span>
	      <input type="text" class="form-control" name="nickname" id="nickname" placeholder="닉네임 입력">
	      <button type="button" class="btn btn-outline-secondary" id="nickname-dupl-btn">중복확인</button>
	    </div>
	
	    <!-- 구독 옵션 -->
	    <div class="form-section text-center">
	      <div class="form-check form-check-inline">
	        <input class="form-check-input" type="checkbox" name="emailSubscribed" id="email-sub">
	        <label class="form-check-label" for="email_sub">이메일 수신 동의</label>
	      </div>
	      <div class="form-check form-check-inline">
	        <input class="form-check-input" type="checkbox" name="kakaoSubscribed" id="kakao-sub">
	        <label class="form-check-label" for="kakao_sub">카카오 수신 동의</label>
	      </div>
	    </div>
	
	    <!-- 버튼 -->
	      <button type="button" class="btn btn-primary w-100" id="join-btn">회원가입</button>
	
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
justify-content-around -> 좌우 여백이 절반, 사이 여백 균등
justify-content-evenly -> 좌우 + 사이 여백 모두 균등
gap-2 -> 요소 사이에 약간의 여백 추가 
align-items-center -> 일단 Flexbox 레이아웃 안에서만 의미있음, 자식 요소를 수직 중앙 정렬 
min-height: 100vh -> 해당 영역의 높이를 화면크기의 100%에 맞춘다는 의미. 200을 주면 해당 영역의 높이가 모니터 화면의 두배길이로 설정됨 

...

수평 정렬은 justify-content
수직 정렬은 align-items
둘다 쓰면 정중앙에 배치 
-->