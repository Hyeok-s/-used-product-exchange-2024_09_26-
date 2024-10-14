package p2.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "asks")
public class AskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String aTitle;

    @Column(nullable = false)
    private String aContent;

    @Column
    private String aReturn;  // 답변 내용, nullable

    @Column(nullable = false)
    private LocalDateTime aTime;  // 문의 시간

    @Column
    private LocalDateTime rTime;  // 답변 시간, nullable

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;  // 외래키로 members 테이블 참조

}
