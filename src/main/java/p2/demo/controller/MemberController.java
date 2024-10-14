package p2.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.MemberEntity;
import p2.demo.service.MemberService;
import p2.demo.repository.MemberRepository;
import org.springframework.http.HttpStatus;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/demo/signup")
    public String signupForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());  // 빈 MemberDTO 객체를 추가
        return "signup";
    }

    @PostMapping("/demo/signup")
    public String signup(@ModelAttribute("memberDTO") MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "redirect:/";
    }

    //로그인
    @GetMapping("/demo/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/demo/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password, HttpSession session) {
        MemberDTO memberDTO = memberService.login(email, password);
        if (memberDTO != null) {
            // 로그인 성공 시 홈으로 리다이렉트
            session.setAttribute("loggedInUser", memberDTO);
            return "redirect:/"; // 로그인 성공 페이지
        } else {
            // 로그인 실패 시 로그인 페이지로 리다이렉트
            return "redirect:/member/login?error=true";
        }
    }

    //로그인
    @GetMapping("/demo/mypage")
    public String mypage() {
        return "mypage";
    }


    @GetMapping("/demo/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/";
    }




}
