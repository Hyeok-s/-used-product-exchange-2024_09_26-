package p2.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pId;

    @Column
    @NonNull
    private String pName;

    @Column
    @NonNull
    private String pContent;

    @Column
    @NonNull
    private Integer pPrice;

    @Column
    @NonNull
    private String pType;

    @Column
    private String pic;

    @Column
    private Character pState;

    {
        this.pState = 'N';
    }


    // MemberEntity와 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")// 외래 키 설정
    private MemberEntity member;

}
