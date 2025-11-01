package kr.co.accoLearner.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *  공지사항, 데일리 퀴즈, 자유게시판 
 */
@Controller
public class BoardController {

  private final Logger logger = LoggerFactory.getLogger(BoardController.class);
  
  
  /**
   * 게시판 이동 (권한 필요 x)
   */
  @GetMapping("/board/notice")
  public String getNoticeList(Model model) {
    
    return "/board/boardList";
  }
  
  @GetMapping("/board/dailyQuize")
  public String getDailyQuizeList(Model model) {
    
    return "/board/boardList";
  }
  
  @GetMapping("/board/community")
  public String getCommunityList(Model model) {
    
    return "/board/boardList";
  }
  
  /**
   * 게시물 작성
   */
  //@PostMapping("/board/notice")
  //public ResponseEntity<T>
  
  
  /**
   * 게시물 상세 조회
   */
  @GetMapping("/board/notice/{noticeSeq}")
  public String getNoticeDetail(@PathVariable Long NoticeSeq, Model model) {
    
    return "/board/boardDetail";
  }
  
  /**
   * 게시물 수정 
   */
  
  
  /**
   * 게시물 삭제 
   */
  
  
}
