package p2.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import p2.demo.dto.ProductDTO;
import p2.demo.entity.ProductEntity;
import p2.demo.service.ProductService;
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

    //관리자 대기 게시물
    @GetMapping("/Admin/nproduct")
    public String getNAdminForm(Model model){
        model.addAttribute("products", productService.getProductsByState("N"));
        return "adminProduct";
    }

   //관리자 승인 게시물
    @GetMapping("/Admin/oproduct")
    public String getOAdminForm(Model model){
        model.addAttribute("products", productService.getProductsByState("O"));
        return "adminProduct";
    }

    //관리자 거부 게시물
    @GetMapping("/Admin/fproduct")
    public String getFAdminForm(Model model){
        model.addAttribute("products", productService.getProductsByState("F"));
        return "adminProduct";
    }

    //거래 완료 게시물
    @GetMapping("/Admin/pproduct")
    public String getPAdminForm(Model model){
        model.addAttribute("products", productService.getProductsByState("P"));
        return "adminProduct";
    }

    @GetMapping("/Admin/admin")
    public String adminHome(){
        return "admin";
    }


    @GetMapping("/Admin/productDetail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        // 제품 ID로 해당 제품의 상세 정보를 조회
        ProductEntity product = productService.findById(id);
        model.addAttribute("product", product);
        return "adminProductDetail";  // adminProductDetail.html 페이지로 이동
    }

    @PutMapping("/Admin/updateProductState/{id}/{newState}")
    @ResponseBody
    public Map<String, Object> updateProductState(@PathVariable Long id, @PathVariable String newState) {
        Map<String, Object> response = new HashMap<>();
        try {
            productService.updateProductState(id, newState);  // 상태 업데이트
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }



}
