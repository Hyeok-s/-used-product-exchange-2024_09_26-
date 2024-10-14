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
import p2.demo.service.WishlistService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final WishlistService wishlistService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    //글쓰기 페이지
    @GetMapping("/product/upload")
    public String uploadForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        return "upload";
    }
    //글쓰기
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
    // 게시물 목록
    @GetMapping("/product/product")
    public String productForm(Model model, HttpSession session) {
        // 현재 로그인된 사용자 정보를 세션에서 가져오기
        Long memberId = (Long) session.getAttribute("memberId");

        // 상품 목록 가져오기
        List<ProductDTO> products = productService.getPstateO();

        // 사용자가 로그인했을 때에만 찜 상태 확인
        if (memberId != null) {
            for (ProductDTO product : products) {
                // 현재 사용자가 이 상품을 찜했는지 여부 확인
                boolean isLiked = wishlistService.isProductLikedByUser(memberId, product.getPId());
                product.setLikedByUser(isLiked);  // 상품 DTO에 찜 상태 설정
            }
        }

        // 모델에 상품 목록과 사용자 정보를 추가
        model.addAttribute("products", products);
        model.addAttribute("user", memberId != null ? memberId : null); // 로그인 여부 판단

        return "product"; // 해당 HTML 파일로 이동
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

    // 제품 ID로 해당 제품의 상세 정보를 조회
    @GetMapping("/product/productDetail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        ProductEntity product = productService.findById(id);
        model.addAttribute("product", product);
        return "productDetail";
    }

}
