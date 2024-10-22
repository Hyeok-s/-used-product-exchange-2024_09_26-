package p2.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import p2.demo.entity.WishlistEntity;

public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {



    WishlistEntity findByMemberIdAndProductId(Long memberId, Long productId);
    void deleteByProductId(Long productId);
    boolean existsByProductId(Long productId);
}
