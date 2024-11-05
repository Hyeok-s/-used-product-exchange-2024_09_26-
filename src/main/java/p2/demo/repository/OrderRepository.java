package p2.demo.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import p2.demo.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    //로그인한 id와 상태의 조회
    List<OrderEntity> findByDeliveryStatusAndBuyerIdAndTrash(String state, Long memberId, boolean trash);
    OrderEntity findByProductId(Long productId);
    void deleteByProductId(Long productId);


    List<OrderEntity> findByDeliveryStatus(String status);
    List<OrderEntity> findByDeliveryStatusIn(List<String> statuses);
    List<OrderEntity> findByBuyerIdAndDeliveryStatusIn(Long memberId, List<String> deliveryStatus);

    @Modifying
    @Transactional
    @Query("UPDATE OrderEntity o SET o.buyer = null WHERE o.buyer.id = :memberId")
    void updateBuyerIdToNull(@Param("memberId") Long memberId);
}