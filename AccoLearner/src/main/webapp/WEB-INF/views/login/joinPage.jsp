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
	
	agreeTerms(); //약관동의 	
	
});
	
//회원가입 api 
function joinUser(){
	const joinBtn = document.getElementById("join-btn");
	
	joinBtn.addEventListener("click", async () => {

		const loginId = document.getElementById("user-id").value;
		const password = document.getElementById("user-password").value;
		const nickname = document.getElementById("nickname").value;
		const email = document.getElementById("user-email").value;
		const emailSubscribed = document.getElementById("switchCheckDefault").checked;
		
		try {
			const response = await fetch("/api/join", {
				method : "POST",
				headers : {"Content-Type": "application/json"},
				body : JSON.stringify({loginId, password, nickname, email, emailSubscribed})
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
	let timer;
	document.getElementById('user-email').addEventListener('input', function(){
		clearTimeout(timer);
		timer = setTimeout(() => {
			let userEmail = document.getElementById('user-email').value;
			duplicateUser('email', userEmail);
		}, 1500);
	});
	
	
}
async function duplicateUser(field, value){ //async는 함수선언 앞에 
	
	if(value == null || value.trim() === ""){
		alert("값을 입려해주세요 ");
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


/**
 *  약관동의 체크 함수 
 */
function agreeTerms(){
	
	let agreeAllChkbox = document.getElementById('agreeAll')
	let agreeServiceChkbox = document.getElementById('agreeTotalService')
	let agreePrivacyChkbox = document.getElementById('privacPolicy')
	
	//전체동의 체크박스 
	document.getElementById('agreeAll').addEventListener('change', function(){
		const checkBoxes = document.querySelectorAll('.checkbox-input:not(#agreeAll)');
		checkBoxes.forEach(checkbox => {
			checkbox.checked = this.checked;
		});
	});
	
	//개별 체크박스 
	document.querySelectorAll('.checkbox-input:not(#agreeAll)').forEach( checkbox => { //각 요소를 checkbox 라고 설정 
		checkbox.addEventListener('change', function(){
			const allCheckboxes = document.querySelectorAll('.checkbox-input:not(#agreeAll)');
			const checkedBoxes = document.querySelectorAll('.checkbox-input:not(#agreeAll):checked');
			//전체박스 === 체크된 박스 개수비교 -> 전체동의 체크박스 체크여부 결정 
			document.getElementById('agreeAll').checked = 
				allCheckboxes.length === checkedBoxes.length;
		});
	});
	
	
	
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
	<div class="mx-auto" style="width: 80%;">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
	</div>
	<div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
		<div class="card p-4" style="max-width: 500px; width: 100%;">
		  <h4 class="text-center mb-4">회원가입</h4>
		
		  <form action="${contextPath}/join" method="post">
		
		    <!-- 아이디 -->
		    <div class="input-group flex-column form-section">
		   		<label for="user-id " class="form-label">아이디</label>
		   		<div class="d-flex">
			      <input type="text" class="form-control" name="username" id="user-id" placeholder="4~15이내로 입력해주세요">
			      <button type="button" class="btn btn-outline-secondary" id="id-dupl-btn">중복확인</button>
		   		</div>
		    </div>
		
		    <!-- 비밀번호 -->
		    <div class="input-group flex-column form-section">
		    	<label for="user-password " class="form-label">비밀번호 </label>
		    	<div class="d-flex">
			      <input type="password" class="form-control" name="password" id="user-password" placeholder="최소 6자 이(알파벳, 숫자 필수)">
		    	</div>
		    </div>
		
		    <!-- 이메일 -->
		    <div class="input-group flex-column form-section">
		    	<label for="user-email " class="form-label">이메일 </label>
		    	<div class="d-flex">
			      <input type="email" class="form-control" name="email" id="user-email" placeholder="example@domain.com">
			      <button type="button" class="btn btn-outline-secondary" id="email-send-btn">인증코드 전송</button>
		    	</div>
		    </div>
		
		    <!-- 인증번호 -->
		    <div class="input-group flex-column form-section">
		    	<label for="email-code " class="form-label">인증번호</label>
		      <div class="d-flex">
			      <input type="text" class="form-control" id="email-code" placeholder="인증번호 입력">
			      <button type="button" class="btn btn-outline-success" id="email-confirm-btn">확인</button>
		      </div>
		    </div>
		
		    <!-- 닉네임 -->
		    <div class="input-group flex-column form-section">
		    	<label for="nickname " class="form-label">닉네임</label>
		    	<div class="d-flex">
			      <input type="text" class="form-control" name="nickname" id="nickname" placeholder="별명을 알파벳, 한글, 숫자를 20자 이하로 입력해주세요.">
			      <button type="button" class="btn btn-outline-secondary" id="nickname-dupl-btn">중복확인</button>
		    	</div>
		    </div>
		
		    <!-- 이메일 구독 옵션 -->
		    <div class="mt-5">
		    	<div class="d-flex justify-content-between mt-1 w-100">	
		    		<div>
		    			이메일 수신 동의 
		    		</div>
				    <div class="form-check form-switch">
						  <input class="form-check-input" type="checkbox" role="switch" id="switchCheckDefault" name="emailSubscribed">
						</div>
		    	</div>
		    	<small class="text-muted">AccoLearner에서 주최하는 다양한 이벤트, 정보성 뉴스레터 및 광고 수신여부를 설정할 수 있습니다.</small>
		    </div>
		    
		    <!-- 약관동의 -->
		    <div class="mt-5">
		    	<div>약관동의</div>
		    	<div class="d-flex flex-column mt-3"> <!-- 전체  -->
		    	
		    		<div class="form-check mt-2 mb-2"> <!-- 전체 -->
		    			<input class="form-check-input checkbox-input" type="checkbox" id="agreeAll">
		    			<label class="form-check-label" for="agreeAll">
		    				<span class="fw-bold">전체동의</span>
		    				<small class="text-muted">전체동의를 선택하시면 아래의 모든 약관에 동의하게 됩니다.</small>
		    			</label>
		    		</div>
		    		
		    		<hr class="flex-grow-1 border-top border-secondary opacity-40">
		    		
		    		<div class="my-3">
		    			<div>
		    				<input class="form-check-input me-1 checkbox-input" type="checkbox" id="agreeTotalService">
		    					<span>통합 서비스 이용약관</span>
		    					<a target="_blank" href="/legal/terms" class="">보기</a>
		    			</div>
		    			<div class="mt-1">
		    				<input class="form-check-input me-1 checkbox-input" type="checkbox" id="privacPolicy">
		    					<span>개인정보 처리방침</span>
		    					<a target="_blank" href="/legal/privacy" class="">보기</a>
		    			</div>
		    		
		    			<div></div>
		    		</div>
		    	</div>
		    </div>
		    
		
		    <!-- 버튼 -->
		    <div class="mt-3">
		      <button type="button" class="btn btn-primary w-100" id="join-btn">회원가입</button>
		    </div>
		
	    </form>
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
my	margin-top + margin-bottom (세로)
-->