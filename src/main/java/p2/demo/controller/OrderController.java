package p2.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import p2.demo.dto.ProductDTO;
import p2.demo.dto.OrderDTO;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.ProductEntity;
import p2.demo.service.ProductService;
import p2.demo.service.OrderService;
import org.springframework.ui.Model;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    // 제품 ID를 받아서 주문 폼을 표시
    @GetMapping("/order/{pId}")
    public String orderForm(@PathVariable("pId") Long pId, Model model, HttpSession session) {
        // 현재 로그인된 사용자 정보 가져오기
        MemberDTO loggedInUser = (MemberDTO) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            model.addAttribute("userName", loggedInUser.getMemberName());
            //model.addAttribute("userPhone", loggedInUser.getPhoneNumber());
            model.addAttribute("userEmail", loggedInUser.getMemberEmail());
            model.addAttribute("userAddress", loggedInUser.getMemberAddress());
        }

        // 제품 정보 가져오기
        ProductEntity product = productService.findById(pId);
        model.addAttribute("product", product);

        return "order";  // 주문 폼 페이지
    }

    // 주문 제출 처리
    @PostMapping("/order/submit")
    public String submitOrder(@RequestParam("deliveryStatus") String address,
                              @RequestParam("shippingMemo") String shippingMemo,
                              @RequestParam("paymentMethod") String paymentMethod,
                              @RequestParam("productId") Long productId,
                              HttpSession session) {

        MemberDTO loggedInUser = (MemberDTO) session.getAttribute("loggedInUser");

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setShippingAddress(address);
        orderDTO.setShippingMemo(shippingMemo);
        orderDTO.setPaymentMethod(paymentMethod);
        orderDTO.setBuyerId(loggedInUser.getId());
        orderDTO.setProductId(productId);  // productId 저장

        orderService.saveOrder(orderDTO);  // 주문 정보 저장
        productService.updateProductState(productId, "P");

        return "redirect:/";  // 성공 페이지로 리다이렉트
    }
}
