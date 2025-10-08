package kr.co.langStudy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@GetMapping("/")
	public String main() {
		logger.info("메인 페이지 요청 도착 ");
		logger.debug("디버그용 상세 로그입니다. ");
		return "index";
	}

}
