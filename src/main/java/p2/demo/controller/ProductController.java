package p2.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import p2.demo.dto.ProductDTO;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.service.MemberService;
import p2.demo.service.ProductService;
import org.springframework.ui.Model;
import p2.demo.dto.MemberDTO;

import jakarta.servlet.http.HttpSession;
import p2.demo.service.WishlistService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final WishlistService wishlistService;
    private final MemberService memberService;
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
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");

        //MemberDTO를 MemberEntity로 변환
        List<ProductEntity> products = productService.getPstateO();

        if(loggedInUserDTO != null){
            MemberEntity loggedInUser = memberService.findById(loggedInUserDTO.getId());
            for(ProductEntity product : products){
                boolean isLiked = wishlistService.isProductLikedByMember(loggedInUser.getId(), product.getId());
                product.setLikedStatus(isLiked);
            }
        }
        model.addAttribute("products", products);
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

    // 제품 ID로 해당 제품의 상세 정보를 조회
    @GetMapping("/product/productDetail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        ProductEntity product = productService.findById(id);
        model.addAttribute("product", product);
        return "productDetail";
    }

    // 찜하기 버튼 클릭 시 처리
    @PostMapping("/product/wishlist/{productId}")
    public String toggleWishlist(@PathVariable("productId") Long productId, HttpSession session, Model model) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");

        if (loggedInUserDTO == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/demo/login";
        }

        //MemberDTO를 MemberEntity로 변환
        MemberEntity loggedInUser = memberService.findById(loggedInUserDTO.getId());
        // 로그인된 사용자와 선택한 상품에 대해 찜하기 상태 변경
        ProductEntity product = productService.findById(productId);

        // 로그인된 사용자와 선택한 상품에 대해 찜하기 상태 변경
        wishlistService.toggleWishlist(loggedInUser, product);

        return "redirect:/";
    }

}
