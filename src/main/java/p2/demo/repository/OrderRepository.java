package p2.demo.repository;

import p2.demo.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    // 추가적으로 필요한 쿼리 메서드가 있으면 여기에 정의 가능
}