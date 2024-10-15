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
    private Long id;

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
    private String pState;

    {
        this.pState = "N";
    }

    // MemberEntity와 다대일 관계 설정
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")// 외래 키 설정
    private MemberEntity member;

    @Transient //이 필드는 데이터베이스에 저장x
    private boolean likedStatus;
    {
        this.likedStatus=false;
    }



}