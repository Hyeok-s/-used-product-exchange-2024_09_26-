package p2.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")  // 루트 URL 요청을 처리
    public String home() {
        return "home";  // home.html 템플릿을 반환
    }

}
