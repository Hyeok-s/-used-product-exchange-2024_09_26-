package p2.demo.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import p2.demo.dto.MemberDTO;
import p2.demo.dto.OrderDTO;
import p2.demo.dto.ProductDTO;
import p2.demo.entity.AddressEntity;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.OrderEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.repository.OrderRepository;
import p2.demo.repository.ProductRepository;
import p2.demo.service.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final WishlistService wishlistService;
    private final MemberService memberService;
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final AddressService addressService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    //글쓰기 페이지
    @GetMapping("/product/upload")
    public String uploadForm(Model model, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        if(loggedInUserDTO == null){
            return "redirect:/demo/errorMessage/1";
        }
        model.addAttribute("productDTO", new ProductDTO());
        return "upload";
    }

    //글쓰기
    @PostMapping("/product/upload")
    public String uploadProduct(@ModelAttribute("productDTO") ProductDTO productDTO,
                                @RequestParam("pic") MultipartFile pic,
                                @RequestParam(value = "pic1", required = false) MultipartFile pic1,
                                @RequestParam(value = "pic2", required = false) MultipartFile pic2,
                                HttpSession session, Model model) {
        try {
            // 로그인된 사용자 확인 및 서비스 호출
            productService.saveProduct(productDTO, pic, pic1, pic2, session);
            return "redirect:/";
        } catch (IllegalStateException | IOException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }


    // 게시물 목록
    @GetMapping("/product/product/{state}")
    public String productForm(@PathVariable("state") String state, Model model, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");

        List<ProductEntity> products = productService.getpState(state);

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

    //내찜 목록
    @GetMapping("/product/heart")
    public String productHeartForm(Model model, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");

        if(loggedInUserDTO == null){
            return "redirect:/demo/errorMessage/1";
        }
        List<ProductEntity> products = productService.getpState("O");
        List<ProductEntity> newProducts = new ArrayList<>();

        MemberEntity loggedInUser = memberService.findById(loggedInUserDTO.getId());
        for(ProductEntity product : products){
            boolean isLiked = wishlistService.isProductLikedByMember(loggedInUser.getId(), product.getId());
            product.setLikedStatus(isLiked);
            if(isLiked){
                newProducts.add(product);
            }
        }
        model.addAttribute("products", newProducts);
        return "wishProduct";
    }

    // 카테고리에 따른 상품 목록 로드
    @GetMapping("/product/category/{category}")
    public String getProductsByCategory1(@PathVariable("category") String category, Model model, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        category = category.replace("_", "/");
        List<ProductEntity> products = productService.getProductsByType1(category);
        if(loggedInUserDTO != null){
            MemberEntity loggedInUser = memberService.findById(loggedInUserDTO.getId());
            for(ProductEntity product : products){
                boolean isLiked = wishlistService.isProductLikedByMember(loggedInUser.getId(), product.getId());
                product.setLikedStatus(isLiked);
            }
        }

        model.addAttribute("products", products);
        return "product";  // 이 페이지는 카테고리별로 필터된 상품 리스트를 보여주는 HTML 조각
    }

    // 서브 카테고리에 따른 상품 목록 로드
    @GetMapping("/product/subcategory/{subcategory}")
    public String getProductsByCategory2(@PathVariable("subcategory") String subcategory, Model model, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        subcategory = subcategory.replace("_", "/");
        List<ProductEntity> products = productService.getProductsByType2(subcategory);
        if(loggedInUserDTO != null){
            MemberEntity loggedInUser = memberService.findById(loggedInUserDTO.getId());
            for(ProductEntity product : products){
                boolean isLiked = wishlistService.isProductLikedByMember(loggedInUser.getId(), product.getId());
                product.setLikedStatus(isLiked);
            }
        }
        model.addAttribute("products", products);
        return "product";  // 이 페이지는 카테고리별로 필터된 상품 리스트를 보여주는 HTML 조각
    }


    // 제품 ID로 해당 제품의 상세 정보를 조회
    @GetMapping("/product/productDetail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        ProductEntity product = productService.findById(id);
        product.setPContent(product.getPContent().replace("\n", "<br>"));
        model.addAttribute("product", product);
        product.getMember().getId();
        boolean mine = false;
        if(loggedInUserDTO == null){
            return "redirect:/demo/errorMessage/1";
        }
        else{
            if(loggedInUserDTO.getId() == product.getMember().getId()){
                mine = true;
            }
            if(!mine){
                // 쿠키 확인
                Cookie[] cookies = request.getCookies();
                boolean viewed = false;
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("viewedProductIds".equals(cookie.getName())) {
                            try {
                                // 기존 쿠키 값 디코딩 후 확인
                                String decodedValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                                if (decodedValue.contains("[" + id + "]")) {
                                    viewed = true;
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                // 조회수가 증가된 적이 없다면 증가시키고, 새로운 쿠키 저장
                if (!viewed) {
                    productService.incrementProductCount(id);

                    // 조회한 제품 ID를 쿠키에 추가
                    String newCookieValue = "[" + id + "]";
                    Cookie newCookie = new Cookie("viewedProductIds", URLEncoder.encode(newCookieValue, StandardCharsets.UTF_8));
                    newCookie.setMaxAge(60*60); // 1시간 동안 유효
                    response.addCookie(newCookie);
                }
            }
        }

        boolean isLiked = wishlistService.isProductLikedByMember(loggedInUserDTO.getId(), product.getId());
        product.setLikedStatus(isLiked);
        model.addAttribute("memberName", loggedInUserDTO.getMemberName());
        model.addAttribute("memberPhone", loggedInUserDTO.getMemberPhone());
        model.addAttribute("memberEmail", loggedInUserDTO.getMemberEmail());
        String formattedPhone = Integer.toString(loggedInUserDTO.getMemberPhone());
        formattedPhone = formatPhoneNumber("0"+formattedPhone);
        model.addAttribute("formattedPhone", formattedPhone);
        List<AddressEntity> addresses = addressService.getAddressesByMemberId(loggedInUserDTO.getId());
        model.addAttribute("addresses",addresses);
        model.addAttribute("order", new OrderDTO());
        model.addAttribute("mine", mine);
        return "productDetail";
    }

    //전화번호 형식 변경 함수
    private String formatPhoneNumber(String phone) {
        // 전화번호를 "010-XXXX-XXXX" 형식으로 변환
         if (phone.length() == 11) {
            return phone.replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1-$2-$3");
        }
        return phone; // 길이가 맞지 않으면 그대로 반환
    }

    // 찜하기 버튼 클릭 시 처리
    @PostMapping("/product/wishlist/{productId}/{num}")
    public String toggleWishlist(@PathVariable("productId") Long productId, @PathVariable("num") Integer num, HttpSession session, Model model) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");

        if (loggedInUserDTO == null) {
            return "redirect:/demo/errorMessage/1";
        }

        //MemberDTO를 MemberEntity로 변환
        MemberEntity loggedInUser = memberService.findById(loggedInUserDTO.getId());
        // 로그인된 사용자와 선택한 상품에 대해 찜하기 상태 변경
        ProductEntity product = productService.findById(productId);

        // 로그인된 사용자와 선택한 상품에 대해 찜하기 상태 변경
        wishlistService.toggleWishlist(loggedInUser, product);
        switch (num) {
            case 1:
                return "redirect:/";
            case 2:
                return "redirect:/product/heart";
            case 3:
                return "redirect:/product/productDetail/"+productId;
        }
        return "redirect:/";
    }

    //제품수정 폼
    @GetMapping("/product/update/{id}/{check}")
    public String editProduct(@PathVariable Long id, @PathVariable int check, Model model) {
        ProductEntity product = productService.findById(id);
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setPName(product.getPName());
        productDTO.setPContent(product.getPContent());
        productDTO.setPPrice(product.getPPrice());
        productDTO.setPType1(product.getPType1());
        productDTO.setPType2(product.getPType2());
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("check", check);
        return "editProduct"; // 수정 페이지의 HTML 파일 이름
    }

    //제품 수정 완료
    @PostMapping("/product/update/{check}")
    public String updateProduct(@ModelAttribute ProductDTO productDTO,@PathVariable int check, @RequestParam("pic") MultipartFile pic,
                                @RequestParam(value = "pic1", required = false) MultipartFile pic1,
                                @RequestParam(value = "pic2", required = false) MultipartFile pic2, Model model) {
        try {
            productService.updateProduct(productDTO, pic, pic1, pic2);
            if(check == 1){
                return "redirect:/";
            }
            else if(check == 2){
                return "redirect:/demo/mypage";
            }
            else{
                return "redirect:/";
            }
        } catch (IllegalStateException | IOException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }

    //제품 삭제
    @PostMapping("/product/delete/{id}/{num}")
    public String deleteProduct(@PathVariable("id") Long id, @PathVariable("num") int num){
        ProductEntity product = productService.findById(id);
        if(product.getPState().equals("P")){
            OrderEntity order = orderService.getOrderByProductId(id);
            if(order.isTrash()){
                orderRepository.delete(order);
                productRepository.delete(product);
                return "redirect:/demo/mypage";
            }
            else{
                product.setTrash(true);
                productRepository.save(product);
            }
        }
        else if(product.getPState().equals("C")){
            return "redirect:/demo/errorMessage/5";
        }
        else{
            wishlistService.deleteByProductId(product.getId());
            productRepository.delete(product);
        }
        if(num == 1){
            return "redirect:/";
        }
        return "redirect:/demo/mypage";
    }

}