package p2.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import p2.demo.dto.AddressDTO;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.AddressEntity;
import p2.demo.entity.OrderEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.service.AddressService;
import p2.demo.service.OrderService;
import p2.demo.service.ProductService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MypageController {

    private final AddressService addressService;
    private final ProductService productService;
    private final OrderService orderService;

    //마이페이지
    @GetMapping("/demo/mypage")
    public String mypage(HttpSession session, Model model) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        if (loggedInUserDTO != null) {
            MemberEntity member = new MemberEntity();
            member.setMemberEmail(loggedInUserDTO.getMemberEmail());
            member.setMemberName(loggedInUserDTO.getMemberName());
            member.setMemberBir(loggedInUserDTO.getMemberBir());
            member.setMemberPhone(loggedInUserDTO.getMemberPhone());

            List<AddressEntity> addresses = addressService.getAddressesByMemberId(loggedInUserDTO.getId());

            model.addAttribute("member", member);
            model.addAttribute("addresses", addresses);
        }
        return "mypage";
    }

    // 주소 추가 페이지 (get 방식)
    @GetMapping("/demo/add-address")
    public String addAddressForm(Model model) {
        model.addAttribute("addressDTO", new AddressDTO());
        return "addAddress";  // addAddress.html 로 이동
    }

    // 주소 추가 처리 (post 방식)
    @PostMapping("/demo/add-address")
    public String addAddress(@ModelAttribute("addressDTO") AddressDTO addressDTO,
                             HttpSession session){
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        MemberEntity admemberEntity = new MemberEntity();
        admemberEntity.setId(loggedInUserDTO.getId());
        addressService.save(addressDTO, admemberEntity);
        return "redirect:/demo/mypage";
    }

    // 주소 수정 페이지 (get 방식)
    @GetMapping("/demo/edit-address/{id}")
    public String editAddressForm(@PathVariable("id") Long id, Model model) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(id);
        model.addAttribute("addressDTO", addressDTO);
        return "editAddress";
    }


    // 주소 수정 처리 (post 방식)
    @PostMapping("/demo/update-address")
    public String updateAddress(@ModelAttribute("addressDTO") AddressDTO addressDTO) {
        addressService.updateAddress(addressDTO);
        return "redirect:/demo/mypage";
    }


    // 주소 삭제 처리
    @PostMapping("/demo/delete-address")
    public String deleteAddress(@RequestParam("id") Long id) {
        addressService.deleteAddress(id);
        return "redirect:/demo/mypage";
    }

    // 상태별 제품 리스트 페이지
    @GetMapping("mypage/product/{status}")
    public String getProductsByStatus(@PathVariable("status") String status, Model model, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        Long id = loggedInUserDTO.getId();
        List<ProductEntity> products = productService.getProductsByStatusAndMemberIdAndFalse(status, id);
        model.addAttribute("products", products);
        return "myProduct";
    }

    // 구매 게시물(배송 상태) 리스트 페이지
    @GetMapping("mypage/order/{status}")
    public String getOrdersByStatus(@PathVariable("status") String status, Model model, HttpSession session) {
        MemberDTO loggedInUserDTO = (MemberDTO) session.getAttribute("loggedInUser");
        Long id = loggedInUserDTO.getId();
        List<OrderEntity> orders = orderService.getOrdersByStatusAndMemberIdAndFalse(status, id);
        model.addAttribute("orders", orders);
        return "myPurchase";
    }

}
