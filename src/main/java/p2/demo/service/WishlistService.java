package p2.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.entity.WishlistEntity;
import p2.demo.repository.WishlistRepository;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    // 찜 여부 확인
    public boolean isProductLikedByMember(Long memberId, Long productId) {
        WishlistEntity wishlist = wishlistRepository.findByMemberIdAndProductId(memberId, productId);
        return wishlist != null && wishlist.isLiked();
    }

    // 찜하기 토글 (찜하기 추가 또는 해제)
    public void toggleWishlist(MemberEntity member, ProductEntity product) {
        WishlistEntity wishlist = wishlistRepository.findByMemberIdAndProductId(member.getId(), product.getId());
        if (wishlist != null) {
            // 이미 찜한 경우, 찜 해제
            wishlistRepository.delete(wishlist);
        } else {
            // 찜하지 않은 경우, 새로운 엔티티 생성하여 저장
            WishlistEntity newWishlist = new WishlistEntity();
            newWishlist.setMember(member);
            newWishlist.setProduct(product);
            newWishlist.setLiked(true);  // 처음에는 찜한 상태로 저장
            wishlistRepository.save(newWishlist);
        }
    }

    public void deleteByMemberId(Long memberId) {
        // memberId에 해당하는 모든 주소 데이터를 삭제
        wishlistRepository.deleteByMemberId(memberId);
    }
}
