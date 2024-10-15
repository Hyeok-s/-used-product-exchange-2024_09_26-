package p2.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import p2.demo.entity.WishlistEntity;

public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {


    boolean existsByMemberIdAndProductId(Long memberId, Long productId);


    WishlistEntity findByMemberIdAndProductId(Long memberId, Long productId);
}
