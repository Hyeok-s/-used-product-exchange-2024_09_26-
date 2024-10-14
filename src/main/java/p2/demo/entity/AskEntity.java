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

    @Column(nullable = false)
    private LocalDateTime aTime;  // 문의 시간

    @Column
    private String aState;
    {
        this.aState = "N";
    }

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private MemberEntity member;  // 외래키로 members 테이블 참조

}
