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

    //테스트
    @GetMapping("/demo/test")
    public String lForm() {
        return "mypageEdit";
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

    // 주소 수정 폼으로 이동
    @GetMapping("/demo/edit-address")
    public String showEditAddressForm(Model model, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUserDTO != null) {
            model.addAttribute("loggedInUser", loggedInUserDTO);
        }
        return "editAddress";
    }

    // 비밀번호 수정 폼으로 이동
    @GetMapping("/demo/edit-password")
    public String showEditPasswordForm(Model model, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUserDTO != null) {
            model.addAttribute("loggedInUser", loggedInUserDTO);
        }
        return "editPassword";
    }

    // 주소 수정 처리
    @PostMapping("/demo/edit-address")
    public String updateAddress(@RequestParam("memberAddress") String newAddress, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUserDTO != null) {
            memberService.updateAddress(loggedInUserDTO.getId(), newAddress);
            loggedInUserDTO.setMemberAddress(newAddress);
            session.setAttribute("loggedInUser", loggedInUserDTO);  // 세션 갱신
        }
        return "redirect:/demo/mypage";
    }

    // 비밀번호 수정 처리
    @PostMapping("/demo/edit-password")
    public String updatePassword(@RequestParam("memberPassword") String newPassword, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUserDTO != null) {
            memberService.updatePassword(loggedInUserDTO.getId(), newPassword);
            session.setAttribute("loggedInUser", loggedInUserDTO);  // 세션 갱신
        }
        return "redirect:/demo/mypage";
    }

}
