package p2.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import p2.demo.entity.AnswerEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {
    AnswerEntity findByAskId(Long id);
    void deleteByMemberId(Long memberId);
    void deleteByAskIdIn(List<Long> askIds);
}
