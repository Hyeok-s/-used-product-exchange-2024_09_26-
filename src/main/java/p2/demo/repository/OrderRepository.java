package p2.demo.repository;

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
}