package p2.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p2.demo.dto.WishlistDTO;
import p2.demo.service.WishlistService;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // 찜 추가
    @PostMapping("/add")
    public ResponseEntity<String> addWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        wishlistService.addWishlist(userId, productId);
        return ResponseEntity.ok("상품이 찜 목록에 추가되었습니다.");
    }

    // 찜 삭제
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        wishlistService.removeWishlist(userId, productId);
        return ResponseEntity.ok("상품이 찜 목록에서 삭제되었습니다.");
    }

    // 사용자별 찜 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistDTO>> getWishlist(@PathVariable Long userId) {
        List<WishlistDTO> wishlist = wishlistService.getWishlistByUser(userId);
        return ResponseEntity.ok(wishlist);
    }
}
