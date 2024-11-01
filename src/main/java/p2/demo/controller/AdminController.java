package p2.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.*;
import p2.demo.service.MemberService;
import p2.demo.service.OrderService;
import p2.demo.service.ProductService;
import p2.demo.service.AskService;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final ProductService productService;
    private final AskService askService;
    private final OrderService orderService;
    private final MemberService memberService;

    //관리자 홈
    @GetMapping("/Admin/admin")
    public String adminHome(){
        return "admin";
    }

    //유저 현황
    @GetMapping("/Admin/member")
    public String adminMembewrForm(Model model){
        List<MemberEntity> memberEntity = memberService.findAll();
        model.addAttribute("member", memberEntity);
        return "adminMember";
    }

    //차트
    @GetMapping("/Admin/chart")
    public String showMemberStatistics(Model model) {
        Map<String, Long> dailyRegistrations = memberService.getDailyRegistrations();
        Map<String, Long> monthlyRegistrations = memberService.getMonthlyRegistrations();

        model.addAttribute("dailyRegistrations", dailyRegistrations);
        model.addAttribute("monthlyRegistrations", monthlyRegistrations);

        return "adminChart";
    }

    //유저상세
    @GetMapping("/Admin/memberDetail/{id}")
    public String adminMemberDetail(@PathVariable("id") Long id, Model model){
        MemberEntity memberEntity = memberService.findById(id);
        model.addAttribute("member", memberEntity);
        return "adminMemberDetail";
    }

    //유저수정
    @GetMapping("/Admin/memberEdit/{id}/{element}")
    public String adminMemberEdit(@PathVariable("id") Long id, @PathVariable("element") String element, Model model){
        MemberEntity memberEntity = memberService.findById(id);
        model.addAttribute("member", memberEntity);
        model.addAttribute("element", element);
        return "adminEdit";
    }

    // 비밀번호 유효성 검사
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{9,15}$";
        return password.matches(passwordPattern);
    }

    //유저수정 저장
    @PostMapping("/Admin/memberEdit/{element}")
    public String updateMemberInfo(@PathVariable("element") String element,
                                   @RequestParam("id") Long id,
                                   @RequestParam(required = false) String memberName,
                                   @RequestParam(required = false) String memberPassword,
                                   @RequestParam(required = false) Integer memberPhone,
                                   Model model) {
        MemberEntity member = memberService.findById(id);

        switch (element) {
            case "name":
                member.setMemberName(memberName);
                break;
            case "password":
                // 비밀번호 유효성 검사
                if (!isValidPassword(memberPassword)) {
                    model.addAttribute("error", "비밀번호는 9~15자, 숫자, 문자 및 특수문자를 포함해야 합니다.");
                    return String.format("redirect:/Admin/memberEdit/%d/password", id); // 비밀번호 변경 페이지로 리다이렉트
                }
                member.setMemberPassword(memberPassword);
                break;
            case "phone":
                member.setMemberPhone(memberPhone);
                break;
            default:
                // 잘못된 element 처리
                model.addAttribute("error", "잘못된 요청입니다.");
                return "errorPage";
        }
        memberService.save(member);  // 변경된 내용을 저장
        return String.format("redirect:/Admin/memberDetail/%d", id);
    }

    //상태에 따른 목록 표시
    @GetMapping("/Admin/product/{state}")
    public String getProductAdminForm(@PathVariable("state") String state, Model model){
        model.addAttribute("products", productService.getProductsByState(state));
        return "adminProduct";
    }


    //주문상태에 따른 목록 표시
    @GetMapping("/Admin/order/{status}")
    public String getOrderAdminForm(@PathVariable("status") String status, Model model){
        if(status.equals("a")){
            List<OrderEntity> orders = orderService.getOrdersByStatus();
            model.addAttribute("orders", orders);
        }
        else{
            List<OrderEntity> orders = orderService.getOrderByStatus(status);
            model.addAttribute("orders", orders);
        }
        return "adminOrderProduct";
    }

    //상태변경
    @PostMapping("/Admin/product/{productId}/{state}")
    public String productState(@PathVariable ("productId") Long productId, @PathVariable("state") String state){
        productService.updateProductState(productId, state);
        return "redirect:/Admin/admin";
    }

    //id에 따른 상세정보
    @GetMapping("/Admin/productDetail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        // 제품 ID로 해당 제품의 상세 정보를 조회
        ProductEntity product = productService.findById(id);
        model.addAttribute("product", product);
        return "adminProductDetail";  // adminProductDetail.html 페이지로 이동
    }

    //id에 따른 주문 상세정보
    @GetMapping("/Admin/productOrderDetail/{productId}")
    public String orderProductDetail(@PathVariable("productId") Long productId, Model model) {
        OrderEntity order = orderService.getOrderByProductId(productId);
        model.addAttribute("order", order);
        return "adminOrderProductDetail";
    }


    //상태변경
    @PostMapping("/Admin/order/{orderId}/{status}")
    public String orderState(@PathVariable ("orderId") Long orderId, @PathVariable("status") String status){
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/Admin/admin";
    }

    //질문 리스트
    @GetMapping("/Admin/askList")
    public String getAskList(Model model) {
        List<AskEntity> askList = askService.getAllAsks();
        model.addAttribute("askList", askList);
        return "ask-list";  // askList.html로 이동
    }

    //질문 상세 정보
    @GetMapping("/Admin/askDetail/{id}")
    public String getAskDetail(@PathVariable Long id, Model model) {
        AskEntity ask = askService.getAskById(id);
        model.addAttribute("ask", ask);
        AnswerEntity answer = askService.getAnswerByAskId(id);
        model.addAttribute("answer", answer);
        return "askDetail";
    }

    //답변
    @PostMapping("/Admin/askDetail/{id}")
    public String submitAnswer(@PathVariable Long id, @RequestParam("aReturn") String aReturn, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        askService.submitAnswer(id, aReturn, loggedInUserDTO.getId());
        return "redirect:/Admin/askList";
    }


}
