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
<title>게시판</title>
<style>
	/* 공지사항 배경색 */
	.notice-bg {
		background-color: rgb(240, 246, 250);
	}
	/* 게시물 항목 호버 효과 */
	.post-item:hover {
		background-color: #f8f9fa;
		cursor: pointer;
	}
	
	/* 공지사항, 데일리퀴즈 자유게시판 배경 및 버튼 커스텀  */
	.board-type-list-bg {
		background-color: rgb(244, 244, 245);
	}
	.board-type-list-bg > button {
		background-color: rgb(244, 244, 245);
    color: #000;                 /* 글자/아이콘 색 */
    border: none;                /* 테두리 제거 */
    border-radius: 0.5rem;       /* 둥근 모서리 */
    box-shadow: none;            /* 그림자 제거 */
		cursor: pointer;
		line-height: 1;
	}
	.board-type-list-bg > button:hover { <!-- 아니야, 호버시 말고 해당 게시판일시에 해주자. -->
		background-color: #ffffff;   /* 배경 하얀색 */
		box-shadow: 0 4px 8px rgba(0,0,0,0.15); /* 호버 시 그림자 */
	}
	
	/* 게시물 정렬 버튼 호버 효과 */
	.order-by:hover {
		background-color : lightgray;
		cursor: pointer;
	}
	/* 배지 스타일 */
	.badge-new {
		background-color: #ff6b6b;
		color: white;
		font-size: 0.7rem;
		padding: 2px 6px;
		border-radius: 3px;
		margin-left: 5px;
	}
	.badge-reply {
		color: #007bff;
		font-size: 0.9rem;
	}
</style>
</head>
<body>
	<div class="mx-auto" style="width: 80%;">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
	</div>

	<div class="container my-5 d-flex justify-content-center">
	  <!-- 가운데 카드 -->
	  <div class="card shadow-sm" style="width: 60%;">
	  	<div class="card-body p-4"> <!-- 카드안에 카드바디-->
	  		
	  		<!-- 0. 게시물 설정 비동기 버튼 -->
	  		<div class="mb-4">
	  			<div class="setting-bg py-3 mb-2">
	  				<div class="d-flex justify-content-between align-item-center">
	  					<div class="board-type-list-bg d-flex gap-2">
		  					<button class="board-type-btn" role="button">공지사항</button>
		  					<button class="board-type-btn" role="button">데일리 퀴즈</button>
		  					<button class="board-type-btn" role="button">자유게시판</button>
	  					</div>
	  					
	  					<div class="d-flex gap-3">
	  						<div>
		  						<div class="btn-group">
									  <button class="btn btn-light btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
									    <i class="bi bi-sort-down"></i>
									  </button>
									  <ul class="dropdown-menu">
									    <li class="order-by ms-4"><a>최신순</a></li>
									    <li class="order-by ms-4"><a>추천순</a></li>
									    <li class="order-by ms-4"><a>댓글순</a></li>
									    <li class="order-by ms-4"><a>스크랩순</a></li>
									    <li class="order-by ms-4"><a>조회순</a></li>
									  </ul>
									</div>
  							</div>
	  						<div><button class="btn btn-primary btn-sm"><i class="bi bi-pencil"></i> 작성하기</button> </div>
	  					</div>
	  					
	  				</div>
	  			</div>
	  		</div>
	  	
		  	<!-- 1. 상단 공지사항 영역 -->
		   	<div class="mb-4">
		   		<!-- 공지사항 1 -->
		   		<div class="notice-bg p-3 mb-2 rounded">
		   			<div class="d-flex justify-content-between align-items-center">
		   				<div>
		   					<span class="badge bg-primary me-2">공지사항</span>
		   					<!-- bg-primary: 파란색 배경 배지, me-2: 오른쪽 마진 -->
		   					<span class="fw-bold">새로운 실험: CODE BREW - 오늘의 개발자 이슈, 한 잔에 담다</span>
		   					<!-- fw-bold: 글씨 굵게 -->
		   				</div>
		   			</div>
		   		</div>
		   		
		   		<!-- 공지사항 2 -->
		   		<div class="notice-bg p-3 mb-2 rounded">
		   			<!-- p-3: 패딩 적용, mb-2: 아래 마진, rounded: 둥근 모서리 -->
		   			<div class="d-flex justify-content-between align-items-center">
		   				<!-- d-flex: flexbox 레이아웃, justify-content-between: 양쪽 끝 정렬, align-items-center: 세로 중앙 정렬 -->
		   				<div>
		   					<span class="badge bg-primary me-2">공지사항</span>
		   					<span class="fw-bold">개발자가 경영진·고객에게 듣는 황당한 말들을 찾습니다!</span>
		   				</div>
		   			</div>
		   		</div>
		   		
		   		<!-- 공지사항 3 -->
		   		<div class="notice-bg p-3 mb-2 rounded">
		   			<div class="d-flex justify-content-between align-items-center">
		   				<div>
		   					<span class="badge bg-warning text-dark me-2">취준생</span>
		   					<!-- bg-warning: 노란색 배경, text-dark: 어두운 글씨색 -->
		   					<span class="fw-bold">취준생 여러분을 위한 설문조사에 참여해 주세요.</span>
		   				</div>
		   			</div>
		   		</div>
		   	</div>
	
		   	<!-- 2. 게시물 리스트 -->
		   	<div class="mb-4">
		   		<!-- 게시물 1 -->
		   		<div class="post-item border-bottom py-3">
		   			<!-- border-bottom: 아래 테두리, py-3: 위아래 패딩 -->
		   			<div class="d-flex justify-content-between align-items-start mb-2">
		   				<!-- align-items-start: 세로 시작점 정렬 -->
		   				<div class="flex-grow-1">
		   					<!-- flex-grow-1: 남은 공간 차지 -->
		   					<div class="mb-1">
		   						<small class="text-muted">사는 얘기 · 동킹콩 · 약 2시간</small>
		   						<!-- text-muted: 회색 텍스트 -->
		   					</div>
		   					<div class="fw-bold">
		   						개발자 n년차 이제는 자바스크립트가 지칩니다. 
		   						<span class="badge-new">N</span>
		   						<span class="badge-reply">(2)</span>
		   					</div>
		   				</div>
		   				<div class="text-end text-muted small">
		   					<!-- text-end: 오른쪽 정렬, small: 작은 글씨 -->
		   					<div>👁 93 👍 0</div>
		   				</div>
		   			</div>
		   		</div>
		   		
		   		<!-- 게시물 2 -->
		   		<div class="post-item border-bottom py-3">
		   			<div class="d-flex justify-content-between align-items-start mb-2">
		   				<div class="flex-grow-1">
		   					<div class="mb-1">
		   						<small class="text-muted">사는 얘기 · TOTOLI · 약 2시간</small>
		   					</div>
		   					<div class="fw-bold">
		   						5개월 입금채널, 퇴사하고 실업급여 받으면서 이직 준비가 맞을까요? 
		   						<span class="badge-new">N</span>
		   					</div>
		   				</div>
		   				<div class="text-end text-muted small">
		   					<div>👁 52 👍 0</div>
		   				</div>
		   			</div>
		   		</div>
		   		
		   		<!-- 게시물 3 -->
		   		<div class="post-item border-bottom py-3">
		   			<div class="d-flex justify-content-between align-items-start mb-2">
		   				<div class="flex-grow-1">
		   					<div class="mb-1">
		   						<small class="text-muted">사는 얘기 · 캐티 · 약 2시간</small>
		   					</div>
		   					<div class="fw-bold">
		   						공용 전기에 고종이 있어요 
		   						<span class="badge-new">N</span>
		   					</div>
		   				</div>
		   				<div class="text-end text-muted small">
		   					<div>👁 21 👍 0</div>
		   				</div>
		   			</div>
		   		</div>
		   		
		   		<!-- 게시물 4 -->
		   		<div class="post-item border-bottom py-3">
		   			<div class="d-flex justify-content-between align-items-start mb-2">
		   				<div class="flex-grow-1">
		   					<div class="mb-1">
		   						<small class="text-muted">사는 얘기 · 원꿈돌 · 약 3시간</small>
		   					</div>
		   					<div class="fw-bold">
		   						급용건 장치칠 진짜 심하네요 
		   						<span class="badge-new">N</span>
		   						<span class="badge-reply">(1)</span>
		   					</div>
		   				</div>
		   				<div class="text-end text-muted small">
		   					<div>👁 110 👍 1</div>
		   				</div>
		   			</div>
		   		</div>
		   		
		   		<!-- 게시물 5 -->
		   		<div class="post-item border-bottom py-3">
		   			<div class="d-flex justify-content-between align-items-start mb-2">
		   				<div class="flex-grow-1">
		   					<div class="mb-1">
		   						<small class="text-muted">사는 얘기 · 바람솔바람 · 약 4시간</small>
		   					</div>
		   					<div class="fw-bold">
		   						젠슨황 한국인으로 착각 
		   						<span class="badge-new">N</span>
		   						<span class="badge-reply">(2)</span>
		   					</div>
		   				</div>
		   				<div class="text-end text-muted small">
		   					<div>👁 75 👍 1</div>
		   				</div>
		   			</div>
		   		</div>
		   		
		   		<!-- 게시물 6 -->
		   		<div class="post-item border-bottom py-3">
		   			<div class="d-flex justify-content-between align-items-start mb-2">
		   				<div class="flex-grow-1">
		   					<div class="mb-1">
		   						<small class="text-muted">사는 얘기 · 월급은나의빛 · 약 4시간</small>
		   					</div>
		   					<div class="fw-bold">
		   						파견 근무 2주차 후기 
		   						<span class="badge-new">N</span>
		   						<span class="badge-reply">(1)</span>
		   					</div>
		   				</div>
		   				<div class="text-end text-muted small">
		   					<div>👁 120 👍 4</div>
		   				</div>
		   			</div>
		   		</div>
		   		
		   		<!-- 게시물 7 (두 번째 이미지에서) -->
		   		<div class="post-item border-bottom py-3">
		   			<div class="d-flex justify-content-between align-items-start mb-2">
		   				<div class="flex-grow-1">
		   					<div class="mb-1">
		   						<small class="text-muted">사는 얘기 · 길가다주웠어 · 약 23시간</small>
		   					</div>
		   					<div class="fw-bold">
		   						샘 올트먼: GPT-6 이름 바꿀까임 
		   						<span class="badge-new">N</span>
		   						<span class="badge-reply">(4)</span>
		   					</div>
		   				</div>
		   				<div class="text-end text-muted small">
		   					<div>👁 488 👍 3</div>
		   				</div>
		   			</div>
		   		</div>
		   		
		   		<!-- 게시물 8 -->
		   		<div class="post-item border-bottom py-3">
		   			<div class="d-flex justify-content-between align-items-start mb-2">
		   				<div class="flex-grow-1">
		   					<div class="mb-1">
		   						<small class="text-muted">사는 얘기 · 길가다주웠어 · 약 24시간</small>
		   					</div>
		   					<div class="fw-bold">
		   						???: 지나갑니다~ 
		   						<span class="badge-new">N</span>
		   						<span class="badge-reply">(2)</span>
		   					</div>
		   				</div>
		   				<div class="text-end text-muted small">
		   					<div>👁 297 👍 3</div>
		   				</div>
		   			</div>
		   		</div>
		   		
		   		<!-- 게시물 9 -->
		   		<div class="post-item border-bottom py-3">
		   			<div class="d-flex justify-content-between align-items-start mb-2">
		   				<div class="flex-grow-1">
		   					<div class="mb-1">
		   						<small class="text-muted">사는 얘기 · 치킨파콜라 · 1일</small>
		   					</div>
		   					<div class="fw-bold">
		   						이제 개발이 귀찮아졌습니다. 
		   						<span class="badge-reply">(13)</span>
		   					</div>
		   				</div>
		   				<div class="text-end text-muted small">
		   					<div>👁 874 👍 5</div>
		   				</div>
		   			</div>
		   		</div>
		   	</div>
	
		   	<!-- 3. 페이지네이션 -->
		   	<div class="d-flex justify-content-center mb-4">
		   		<!-- justify-content-center: 가운데 정렬 -->
		   		<nav>
		   			<ul class="pagination">
		   				<!-- pagination: 부트스트랩 페이지네이션 클래스 -->
		   				<li class="page-item disabled">
		   					<!-- disabled: 비활성화 상태 -->
		   					<a class="page-link" href="#" tabindex="-1">이전</a>
		   					<!-- page-link: 페이지 링크 스타일 -->
		   				</li>
		   				<li class="page-item active">
		   					<!-- active: 현재 페이지 표시 -->
		   					<a class="page-link" href="#">1</a>
		   				</li>
		   				<li class="page-item"><a class="page-link" href="#">2</a></li>
		   				<li class="page-item"><a class="page-link" href="#">3</a></li>
		   				<li class="page-item"><a class="page-link" href="#">4</a></li>
		   				<li class="page-item"><a class="page-link" href="#">5</a></li>
		   				<li class="page-item"><a class="page-link" href="#">...</a></li>
		   				<li class="page-item"><a class="page-link" href="#">6443</a></li>
		   				<li class="page-item">
		   					<a class="page-link" href="#">다음</a>
		   				</li>
		   			</ul>
		   		</nav>
		   	</div>
	
		   	<!-- 4. 게시물 검색  -->
		   	<div class="d-flex justify-content-center">
		   		<div class="input-group" style="max-width: 500px;">
		   			<!-- input-group: 입력 요소들을 그룹화 -->
		   			<input type="text" class="form-control" placeholder="사는얘기 내에서 검색">
		   			<!-- form-control: 부트스트랩 입력 필드 스타일 -->
		   			<button class="btn btn-outline-secondary" type="button">
		   				<!-- btn: 버튼 기본 스타일, btn-outline-secondary: 회색 테두리 버튼 -->
		   				🔍
		   			</button>
		   		</div>
		   	</div>
		   	
	  	</div>
		</div>
		<!-- 카드 끝 -->

	</div>

	<div class="mx-auto" style="width: 80%;">
		<jsp:include page="/WEB-INF/views/common/bottom.jsp" />
	</div>
</body>
</html>

<!-- 
주요 부트스트랩 클래스 정리:

1. 레이아웃
- d-flex: flexbox 레이아웃 사용
- justify-content-between: 양쪽 끝 정렬
- justify-content-center: 가운데 정렬
- align-items-center: 세로 중앙 정렬
- align-items-start: 세로 시작점 정렬
- flex-grow-1: 남은 공간 모두 차지

2. 간격
- p-3: 패딩 (padding) 적용
- py-3: 위아래 패딩
- mb-2: 아래 마진 (margin-bottom)
- mb-4: 아래 마진 (더 큰 값)
- my-5: 위아래 마진

3. 테두리 및 모양
- border-bottom: 아래 테두리
- rounded: 둥근 모서리
- shadow-sm: 작은 그림자 효과

4. 텍스트
- fw-bold: 글씨 굵게
- text-muted: 회색 텍스트
- text-end: 오른쪽 정렬
- text-dark: 어두운 글씨색
- small: 작은 글씨

5. 색상
- bg-primary: 파란색 배경
- bg-warning: 노란색 배경
- notice-bg: 회색 배경 (커스텀)

6. 컴포넌트
- badge: 배지 스타일
- card: 카드 컨테이너
- card-body: 카드 내용
- pagination: 페이지네이션
- page-item: 페이지 항목
- page-link: 페이지 링크
- input-group: 입력 필드 그룹
- form-control: 입력 필드
- btn: 버튼
- btn-outline-secondary: 회색 테두리 버튼

7. 상태
- active: 활성화 상태
- disabled: 비활성화 상태
- hover: 마우스 오버 효과 (CSS)
-->