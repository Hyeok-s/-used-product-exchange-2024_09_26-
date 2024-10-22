package p2.demo.repository;
import org.springframework.stereotype.Repository;
import p2.demo.entity.ProductsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


@Repository
public interface ProductsHistoryRepository extends JpaRepository<ProductsHistoryEntity, Long> {
    List<ProductsHistoryEntity> findByMemberId(Long MemberId);
}
