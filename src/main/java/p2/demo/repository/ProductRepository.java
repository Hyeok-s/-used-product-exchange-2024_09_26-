package p2.demo.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import p2.demo.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAll();

    @Query("SELECT p FROM ProductEntity p WHERE p.pType1 = ?1 AND p.pState = 'O'")
    List<ProductEntity> findBypType1(String pType1);

    @Query("SELECT p FROM ProductEntity p WHERE p.pType2 = ?1 AND p.pState = 'O'")
    List<ProductEntity> findBypType2(String pType2);


    List<ProductEntity> findBypState(String pState);

    //로그인한 id와 상태의 조회
    List<ProductEntity> findBypStateAndMemberId(String state, Long memberId);

    List<ProductEntity> findByMemberId(Long memberId);

    /*List<ProductEntity> findAllOrderByCountsDesc();
    List<ProductEntity> findAllOrderBypTimeDesc();
    List<ProductEntity> findAllOrderBypTimeAsc();
    List<ProductEntity> findAllOrderBypPriceAsc();
    List<ProductEntity> findAllOrderBypPriceDesc();*/
}

