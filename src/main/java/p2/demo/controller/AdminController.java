package p2.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import p2.demo.entity.AskEntity;
import p2.demo.entity.OrderEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.service.OrderService;
import p2.demo.service.ProductService;
import p2.demo.service.AskService;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final ProductService productService;
    private final AskService askService;
    private final OrderService orderService;

    //관리자 홈
    @GetMapping("/Admin/admin")
    public String adminHome(){
        return "admin";
    }

    //상태에 따른 목록 표시
    @GetMapping("/Admin/product/{state}")
    public String getAdminForm(@PathVariable("state") String state, Model model){
        model.addAttribute("products", productService.getProductsByState(state));
        return "adminProduct";
    }


    //주문상태에 따른 목록 표시
    @GetMapping("/Admin/order/{status}")
    public String getAdminOrderForm(@PathVariable("status") String status, Model model){
        if(status.equals("a")){
            model.addAttribute("orders", orderService.getOrdersByStatus(status));
        }
        else{
            model.addAttribute("dorers", orderService.getCompletePurchaseHistory(status));
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
        return "askDetail";  // askDetail.html로 이동
    }

    //답변
    @PostMapping("/Admin/askDetail/{id}")
    public String submitAnswer(@PathVariable Long id, @RequestParam("aReturn") String aReturn) {
        askService.submitAnswer(id, aReturn);
        askService.setAskState(id);
        return "redirect:/Admin/askList";
    }


}
