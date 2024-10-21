package p2.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import p2.demo.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAll();

    @Query("SELECT p FROM ProductEntity p WHERE p.pType1 = ?1 AND p.pState = 'O'")
    List<ProductEntity> findByPtype1(String pType1);

    @Query("SELECT p FROM ProductEntity p WHERE p.pType2 = ?1 AND p.pState = 'O'")
    List<ProductEntity> findByPtype2(String pType2);

    @Query("SELECT p FROM ProductEntity p WHERE p.pState = ?1")
    List<ProductEntity> findByPstate(String pState);

    @Query("select p from ProductEntity p Where p.pState = 'O'")
    List<ProductEntity> findByPstateO();

    //로그인한 id와 상태의 조회
    List<ProductEntity> findBypStateAndMemberId(String state, Long memberId);

}

