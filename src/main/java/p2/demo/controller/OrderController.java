package p2.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import p2.demo.dto.AddressDTO;
import p2.demo.dto.ProductDTO;
import p2.demo.dto.OrderDTO;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.AddressEntity;
import p2.demo.entity.OrderEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.service.AddressService;
import p2.demo.service.ProductService;
import p2.demo.service.OrderService;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final AddressService addressService;

    // 제품 ID를 받아서 주문 폼을 표시
    @GetMapping("/order/{id}")
    public String orderForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        // 현재 로그인된 사용자 정보 가져오기
        MemberDTO loggedInUser = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("memberName", loggedInUser.getMemberName());
            model.addAttribute("memberPhone", loggedInUser.getMemberPhone());
            model.addAttribute("memberEmail", loggedInUser.getMemberEmail());
            List<AddressEntity> addresses = addressService.getAddressesByMemberId(loggedInUser.getId());
            model.addAttribute("addresses",addresses);
            // 제품 정보 가져오기
            ProductEntity product = productService.findById(id);
            model.addAttribute("product", product);
            model.addAttribute("order", new OrderDTO());
            return "order";  // 주문 폼 페이지
        }
        return "redirect:/";
    }

    // 주문 제출 처리
    @PostMapping("/order/submit")
    public String submitOrder(@ModelAttribute OrderDTO orderDTO, HttpSession session) {

        MemberDTO loggedInUser = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            Long productId = orderDTO.getProductId();

            orderDTO.setBuyerId(loggedInUser.getId());
            orderService.saveOrder(orderDTO);  // 주문 정보 저장
            productService.updateProductState(productId, "C");

        }
        return "redirect:/";  // 성공 페이지로 리다이렉트
    }

    //구매 확인 처리
    @GetMapping("/order/finish/{id}")
    public String finishOrder(@PathVariable("id") Long id){
        orderService.updateOrderDeliveryStatus(id, "d");
        productService.updateProductState(id, "P");
        return "redirect:/demo/mypage";
    }

    //주문 취소
    @GetMapping("/order/cancel/{productId}")
    public String cancelOrder(@PathVariable("productId") Long productId){
        OrderEntity orderEntity = orderService.getOrderByProductId(productId);
        orderService.deleteOrder(orderEntity.getId());
        productService.updateProductState(productId, "O");
        return "redirect:/demo/mypage";
    }


}
