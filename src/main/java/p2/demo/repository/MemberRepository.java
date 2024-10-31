package p2.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import p2.demo.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberEmail(String email);
    boolean existsByMemberEmail(String memberEmail);

    @Query("SELECT DATE_FORMAT(m.memberTime, '%Y-%m-%d') AS day, COUNT(m) " +
            "FROM MemberEntity m GROUP BY day ORDER BY day")
    List<Object[]> countByRegistrationDate();

    // 월별 가입자 수 집계
    @Query("SELECT DATE_FORMAT(m.memberTime, '%Y-%m') AS month, COUNT(m) " +
            "FROM MemberEntity m GROUP BY month ORDER BY month")
    List<Object[]> countByRegistrationMonth();
}
