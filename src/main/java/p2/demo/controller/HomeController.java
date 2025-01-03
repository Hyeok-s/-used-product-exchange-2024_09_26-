package p2.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    @GetMapping("/")  // 루트 URL 요청을 처리
    public String home() {
        return "home";  // home.html 템플릿을 반환
    }

    @GetMapping("/demo/errorMessage/{num}")
    public String ErrorMessageForm(@PathVariable("num") Integer num, Model model) {
        String message;
        String redirectUrl;

        switch (num) {
            case 1:
                message = "로그인 먼저 진행해주세요.";
                redirectUrl = "/demo/login";
                break;
            case 2:
                message = "판매중인 상품이 있습니다.";
                redirectUrl = "/demo/mypage";
                break;
            case 3:
                message = "구매중인 상품이 있습니다.";
                redirectUrl = "/demo/mypage";
                break;
            case 4:
                message = "비밀번호는 9~15자, 숫자, 문자 및 특수문자를 포함해야 합니다.";
                redirectUrl = "/demo/edit-password";
                break;
            case 5:
                message = "구매가 진행중인 상품은 삭제할 수 없습니다.";
                redirectUrl = "/";
                break;
            default:
                message = "알 수 없는 오류가 발생했습니다.";
                redirectUrl = "/demo/home";
        }

        model.addAttribute("message", message);
        model.addAttribute("redirectUrl", redirectUrl);

        return "errorMessage";
    }

}
