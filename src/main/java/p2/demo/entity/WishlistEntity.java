package p2.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "wishlist")
public class WishlistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private MemberEntity user;  // 회원 엔티티와의 관계

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;  // 제품 엔티티와의 관계

    private LocalDateTime likedAt = LocalDateTime.now();  // 찜한 시간
}
