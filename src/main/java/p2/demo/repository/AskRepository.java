package p2.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import p2.demo.entity.AskEntity;

import java.util.List;


@Repository
public interface AskRepository extends JpaRepository<AskEntity, Long> {
    void deleteByMemberId(Long memberId);
    List<AskEntity> findByMemberId(Long memberId);
    Page<AskEntity> findByMemberId(Long memberId, Pageable pageable);
}
