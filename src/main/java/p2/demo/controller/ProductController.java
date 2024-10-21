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
            return "redirect:/";
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


    // 카테고리에 따른 상품 목록 로드
    @GetMapping("/product/category/{category}")
    public String getProductsByCategory1(@PathVariable("category") String category, Model model) {
        category = category.replace("_", "/");
        List<ProductEntity> products = productService.getProductsByType1(category);
        model.addAttribute("products", products);
        return "product";  // 이 페이지는 카테고리별로 필터된 상품 리스트를 보여주는 HTML 조각
    }

    // 서브 카테고리에 따른 상품 목록 로드
    @GetMapping("/product/subcategory/{subcategory}")
    public String getProductsByCategory2(@PathVariable("subcategory") String subcategory, Model model) {
        subcategory = subcategory.replace("_", "/");
        List<ProductEntity> products = productService.getProductsByType2(subcategory);
        model.addAttribute("products", products);
        return "product";  // 이 페이지는 카테고리별로 필터된 상품 리스트를 보여주는 HTML 조각
    }


    // 제품 ID로 해당 제품의 상세 정보를 조회
    @GetMapping("/product/productDetail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        ProductEntity product = productService.findById(id);
        model.addAttribute("product", product);
        boolean mine = false;
        if(loggedInUserDTO.getId() == product.getMember().getId()){
            mine = true;
        }
        model.addAttribute("mine", mine);
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

    //제품수정 폼
    @GetMapping("/product/update/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        ProductEntity product = productService.findById(id);
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setPName(product.getPName());
        productDTO.setPContent(product.getPContent());
        productDTO.setPPrice(product.getPPrice());
        productDTO.setPType1(product.getPType1());
        productDTO.setPType2(product.getPType2());

        model.addAttribute("productDTO", productDTO);
        return "editProduct"; // 수정 페이지의 HTML 파일 이름
    }

    //제품 수정 완료
    @PostMapping("/product/update")
    public String updateProduct(@ModelAttribute ProductDTO productDTO, @RequestParam("pic") MultipartFile pic, Model model) {
        try {//로그인 된사용자 인지 확인
            productService.updateProduct(productDTO, pic);
            return "redirect:/";
        } catch (IllegalStateException | IOException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }

    //제품삭제
    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Model model) {
        try {
            productService.deleteProduct(id);
            return "redirect:/"; // 삭제 후 메인 페이지로 리다이렉트
        } catch (Exception e) {
            model.addAttribute("errorMessage", "제품 삭제 중 오류가 발생했습니다.");
            return "redirect:/product/" + id; // 오류 발생 시 제품 상세 페이지로 리다이렉트
        }
    }
}