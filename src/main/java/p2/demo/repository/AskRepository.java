package p2.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import p2.demo.entity.AskEntity;

import java.util.Optional;

@Repository
public interface AskRepository extends JpaRepository<AskEntity, Long> {

}
