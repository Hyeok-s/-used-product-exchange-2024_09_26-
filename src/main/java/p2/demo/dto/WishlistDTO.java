package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;
import p2.demo.entity.WishlistEntity;

import java.time.LocalDateTime;

@Getter
@Setter
public class WishlistDTO {

    private Long id;
    private Long userId;    // 사용자 ID
    private Long productId; // 제품 ID
    private LocalDateTime likedAt;

    // DTO 변환을 위한 메소드
    public static WishlistDTO toWishlistDTO(WishlistEntity wishlistEntity) {
        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setId(wishlistEntity.getId());
        wishlistDTO.setUserId(wishlistEntity.getUser().getId());
        wishlistDTO.setProductId(wishlistEntity.getProduct().getPId());
        wishlistDTO.setLikedAt(wishlistEntity.getLikedAt());
        return wishlistDTO;
    }
}
