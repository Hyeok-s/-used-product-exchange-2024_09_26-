package p2.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import p2.demo.dto.ProductDTO;
import p2.demo.entity.ProductEntity;
import p2.demo.service.ProductService;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/product/upload")
    public String uploadForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        return "upload";
    }

    @PostMapping("/product/upload")
    public String uploadProduct(@ModelAttribute("productDTO") ProductDTO productDTO,
                                @RequestParam("pic") MultipartFile pic,
                                HttpSession session, Model model) {
        try {//로그인 된사용자 인지 확인
            productService.saveProduct(productDTO, pic, session);
            return "redirect:/product/product";
        } catch (IllegalStateException | IOException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/product/product")
    public String productForm(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "product";
    }

    // 가전제품만 조회
    @GetMapping("/product/electronics")
    public String getElectronicsProducts(Model model) {
        model.addAttribute("products", productService.getProductsByType("가전제품"));
        return "product";
    }
/*
    // 전자기기만 조회
    @GetMapping("/products/devices")
    public String getDevicesProducts(Model model) {
        model.addAttribute("products", productService.getProductsByType("전자기기"));
        return "product";
    }*/

}
