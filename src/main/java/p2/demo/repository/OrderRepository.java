package p2.demo.repository;

import org.springframework.stereotype.Repository;
import p2.demo.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import p2.demo.entity.ProductEntity;
import p2.demo.entity.WishlistEntity;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    //로그인한 id와 상태의 조회
    List<OrderEntity> findByDeliveryStatusAndBuyerId(String state, Long memberId);
    OrderEntity findByProductId(Long productId);
    void deleteByProductId(Long productId);
}