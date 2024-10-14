package p2.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import p2.demo.dto.WishlistDTO;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.entity.WishlistEntity;
import p2.demo.repository.MemberRepository;
import p2.demo.repository.ProductRepository;
import p2.demo.repository.WishlistRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 찜 추가
    public void addWishlist(Long userId, Long productId) {
        MemberEntity user = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다."));

        // 이미 찜한 제품인지 확인
        if (wishlistRepository.findByUserIdAndProductId(userId, productId) == null) {
            WishlistEntity wishlistEntity = new WishlistEntity();
            wishlistEntity.setUser(user);
            wishlistEntity.setProduct(product);
            wishlistRepository.save(wishlistEntity);
        }
    }

    // 찜 삭제
    public void removeWishlist(Long userId, Long productId) {
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);
    }

    // 사용자별 찜 목록 조회
    public List<WishlistDTO> getWishlistByUser(Long userId) {
        List<WishlistEntity> wishlistEntities = wishlistRepository.findByUserId(userId);
        return wishlistEntities.stream()
                .map(WishlistDTO::toWishlistDTO)
                .collect(Collectors.toList());
    }

    public boolean isProductLikedByUser(Long memberId, Long productId) {
        return wishlistRepository.existsByMemberIdAndProductId(memberId, productId);
    }

}
