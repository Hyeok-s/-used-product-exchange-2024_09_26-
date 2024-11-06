package p2.demo.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import p2.demo.dto.AddressDTO;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.AskEntity;
import p2.demo.entity.OrderEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.repository.AddressRepository;
import p2.demo.repository.OrderRepository;
import p2.demo.service.*;
import p2.demo.repository.MemberRepository;


import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final AddressService addressService;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderService orderService;
    private final WishlistService wishlistService;
    private final AskService askService;

    //회원가입폼
    @GetMapping("/demo/signup")
    public String signupForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());  // 빈 MemberDTO 객체를 추가
        return "signup";
    }


    //ResponseEntity를 활용해 c.s통신
    @GetMapping("/demo/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = memberService.isEmailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/demo/signup")
    public String signup(@ModelAttribute("memberDTO") MemberDTO memberDTO) {
        // 가입 시 이메일 중복 여부 최종 확인
        if (!memberService.isEmailExists(memberDTO.getMemberEmail())) {
            // 비밀번호 일치 및 규칙 확인
            if (isValidPassword(memberDTO.getMemberPassword())) {
                MemberEntity savedMember = memberService.save(memberDTO);
                AddressDTO addressDTO = memberDTO.getAddress();
                addressService.save(addressDTO, savedMember);
                return "redirect:/";
            } else {
                return "회원가입 실패: 비밀번호 규칙을 확인해주세요.";
            }
        } else {
            return "회원가입 실패: 이메일 중복을 확인해주세요.";
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{9,15}$";
        return password.matches(passwordPattern);
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

    //로그아웃
    @GetMapping("/demo/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/";
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

    // 비밀번호 수정 처리
    @PostMapping("/demo/edit-password")
    public String updatePassword(@RequestParam("memberPassword") String memberPassword, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        MemberEntity member = memberService.findById(loggedInUserDTO.getId());
        if (!isValidPassword(memberPassword)) {
            return "redirect:/demo/errorMessage/4";
        }
        member.setMemberPassword(memberPassword);
        memberService.save(member);
        session.setAttribute("loggedInUser", loggedInUserDTO);

        return "redirect:/demo/mypage";
    }

    //회원탈퇴 폼
    @GetMapping("/demo/withdraw")
    public String withDrawForm(HttpSession session, Model model){
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUserDTO != null){
            model.addAttribute("memberDTO", loggedInUserDTO);
        }
        return "memberWithDraw";
    }

    //회원탈퇴
    @Transactional
    @PostMapping("/demo/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long memberId) {

        if (productService.hasProductsWithStateC(memberId)) {
            return "redirect:/demo/errorMessage/2";  // 탈퇴 실패 시 리다이렉트할 경로
        }
        if (orderService.hasOrdersWithRestrictedStatus(memberId)) {
            return "redirect:/demo/errorMessage/3";  // 탈퇴 실패 시 리다이렉트할 경로
        }
        wishlistService.deleteByMemberId(memberId);
        addressService.deleteByMemberId(memberId);
        // 1. ask에서 memberId로 모든 질문 조회
        List<AskEntity> asks = askService.findByMemberId(memberId);

        // 2. askId 리스트 추출
        List<Long> askIds = asks.stream()
                .map(AskEntity::getId) // askId 가져오기
                .collect(Collectors.toList());

        // 3. answers에서 해당 askId로 답변 삭제
        if (!askIds.isEmpty()) {
            askService.deleteByAskIds(askIds);
        }
        askService.deleteByAskMemberId(memberId);
        MemberEntity member = memberService.findById(memberId);
        productService.updateMemberIdToNull(memberId);
        orderService.updateMemberIdToNull(memberId);
        memberRepository.delete(member);

        return "redirect:/demo/mypage";
    }


}
