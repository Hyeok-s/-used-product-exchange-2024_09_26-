package p2.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "answers")
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String aReturn;

    @Column(nullable = false)
    private LocalDateTime rTime;

    @ManyToOne
    @JoinColumn(name = "ask_id", referencedColumnName = "id")
    private AskEntity ask;  // 외래키로 members 테이블 참조
}
