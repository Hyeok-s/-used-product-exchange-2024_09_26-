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

    //회원가입폼
    @GetMapping("/demo/signup")
    public String signupForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());  // 빈 MemberDTO 객체를 추가
        return "signup";
    }
    //회원가입
    @PostMapping("/demo/signup")
    public String signup(@ModelAttribute("memberDTO") MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "redirect:/";
    }

    //로그인폼
    @GetMapping("/demo/login")
    public String loginForm() {
        return "login";
    }

    //로그인
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

    //마이페이지
    @GetMapping("/demo/mypage")
    public String mypage() {
        return "mypage";
    }

    //로그아웃
    @GetMapping("/demo/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/";
    }

    //마이페이지 수정폼
    @GetMapping("/demo/edit")
    public String showEditForm(HttpSession session, Model model) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUserDTO != null) {
            model.addAttribute("loggedInUser", loggedInUserDTO);
            return "mypageEdit";  // 수정 페이지로 이동
        }
        return "redirect:/login";  // 로그인하지 않았다면 로그인 페이지로 리다이렉트
    }

    //마이페이지 수정
    @PostMapping("/demo/edit")
    public String updateProfile(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUserDTO != null) {
            loggedInUserDTO.setMemberName(memberDTO.getMemberName());
            loggedInUserDTO.setMemberAge(memberDTO.getMemberAge());
            loggedInUserDTO.setMemberPhone(memberDTO.getMemberPhone());
            loggedInUserDTO.setMemberAddress(memberDTO.getMemberAddress());
            loggedInUserDTO.setMemberPassword(memberDTO.getMemberPassword());

            memberService.updateMember(loggedInUserDTO);  // 서비스에서 사용자 정보 업데이트 처리
            session.setAttribute("loggedInUser", loggedInUserDTO);  // 세션 정보 갱신
        }
        return "redirect:/demo/mypage";  // 수정 후 마이페이지로 이동
    }

}
