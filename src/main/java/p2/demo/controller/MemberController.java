package p2.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import p2.demo.dto.AddressDTO;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.AddressEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.repository.AddressRepository;
import p2.demo.service.AddressService;
import p2.demo.service.MemberService;
import p2.demo.repository.MemberRepository;
import org.springframework.http.HttpStatus;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final AddressService addressService;

    //회원가입폼
    @GetMapping("/demo/signup")
    public String signupForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());  // 빈 MemberDTO 객체를 추가
        return "signup";
    }

    //회원가입
    @PostMapping("/demo/signup")
    public String signup(@ModelAttribute("memberDTO") MemberDTO memberDTO) {
        MemberEntity savedMember = memberService.save(memberDTO);
        AddressDTO addressDTO = memberDTO.getAddress();

        addressService.save(addressDTO, savedMember);
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
    public String mypage(HttpSession session, Model model) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUserDTO != null) {
            MemberEntity member = new MemberEntity();
            member.setMemberEmail(loggedInUserDTO.getMemberEmail());
            member.setMemberName(loggedInUserDTO.getMemberName());
            member.setMemberBir(loggedInUserDTO.getMemberBir());
            member.setMemberPhone(loggedInUserDTO.getMemberPhone());

            List<AddressEntity> addresses = addressService.getAddressesByMemberId(loggedInUserDTO.getId());
            //List<ProductEntity> myProduct = productService.get~~;
            //List<ProductEntity> buyProduct =

            model.addAttribute("member", member);
            model.addAttribute("addresses", addresses);
        }
        return "mypage";
    }

    // 주소 추가 페이지 (get 방식)
    @GetMapping("/demo/add-address")
    public String addAddressForm(Model model) {
        model.addAttribute("addressDTO", new AddressDTO());
        return "addAddress";  // addAddress.html 로 이동
    }

    // 주소 추가 처리 (post 방식)
    @PostMapping("/demo/add-address")
    public String addAddress(@ModelAttribute("addressDTO") AddressDTO addressDTO,
                             HttpSession session){
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        MemberEntity admemberEntity = new MemberEntity();
        admemberEntity.setId(loggedInUserDTO.getId());
        addressService.save(addressDTO, admemberEntity);
        return "redirect:/demo/mypage";
    }

    // 주소 수정 페이지 (get 방식)
    @GetMapping("/demo/edit-address/{id}")
    public String editAddressForm(@PathVariable("id") Long id, Model model) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(id);
        model.addAttribute("addressDTO", addressDTO);
        return "editAddress";
    }


    // 주소 수정 처리 (post 방식)
    @PostMapping("/demo/update-address")
    public String updateAddress(@ModelAttribute("addressDTO") AddressDTO addressDTO) {
        addressService.updateAddress(addressDTO);
        return "redirect:/demo/mypage";
    }




    // 주소 삭제 처리
    @PostMapping("/demo/delete-address")
    public String deleteAddress(@RequestParam("id") Long id) {
        addressService.deleteAddress(id);
        return "redirect:/demo/mypage";
    }




    //로그아웃
    @GetMapping("/demo/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/";
    }

    // 주소 수정 폼으로 이동 (GET 요청)
    @GetMapping("/demo/edit-address")
    public String editAddressForm(HttpSession session, Model model) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");

        if (loggedInUserDTO != null) {
            Long memberId = loggedInUserDTO.getId();
            // memberId를 이용하여 해당 사용자의 주소 목록 가져오기
            List<AddressEntity> addresses = addressService.getAddressesByMemberId(memberId);
            model.addAttribute("addresses", addresses);
        }

        return "edit-address"; // 주소 수정 폼 HTML로 이동
    }

    // 주소 추가, 수정, 삭제 처리 (POST 요청)
    /*@PostMapping("/demo/edit-address")
    public String updateAddress(
            @RequestParam(value = "action", required = true) String action,
            @ModelAttribute AddressEntity address,
            HttpSession session){
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");

        if (loggedInUserDTO != null) {
            Long memberId = loggedInUserDTO.getId();
            MemberEntity memberGetId = new MemberEntity();
            memberGetId.setId(memberId);
            if ("add".equals(action)) {
                // 주소 추가
                address.setMember(memberGetId);
                addressService.addAddress(address);
            } else if ("update".equals(action)) {
                // 주소 수정
                address.setMember(memberGetId);
                addressService.updateAddress(address);
            } else if ("delete".equals(action)) {
                // 주소 삭제
                addressService.deleteAddress(address.getId());
            }
        }

        return "redirect:/demo/mypage";
    }
*/


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
    public String updatePassword(@RequestParam("memberPassword") String newPassword, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUserDTO != null) {
            memberService.updatePassword(loggedInUserDTO.getId(), newPassword);
            session.setAttribute("loggedInUser", loggedInUserDTO);  // 세션 갱신
        }
        return "redirect:/demo/mypage";
    }

}
