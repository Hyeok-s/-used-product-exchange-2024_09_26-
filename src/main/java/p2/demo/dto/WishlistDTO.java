package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;
import p2.demo.entity.WishlistEntity;

@Getter
@Setter
public class WishlistDTO {

    private Long id;
    private Long memberId;    // 사용자 ID
    private Long productId; // 제품 ID
    private boolean liked;  // 찜 여부
}
