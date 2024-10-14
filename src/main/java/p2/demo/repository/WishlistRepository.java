package p2.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import p2.demo.entity.WishlistEntity;
import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {
    // 특정 사용자의 찜 목록 조회
    List<WishlistEntity> findByUserId(Long userId);

    // 특정 사용자와 제품이 존재하는지 확인
    WishlistEntity findByUserIdAndProductId(Long userId, Long productId);

    // 특정 사용자와 제품의 찜 삭제
    void deleteByUserIdAndProductId(Long userId, Long productId);

    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}
