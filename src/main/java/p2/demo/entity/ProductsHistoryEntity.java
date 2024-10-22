package p2.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="productsHistory")
public class ProductsHistoryEntity {
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
    private String pic;

    // MemberEntity와 다대일 관계 설정
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")// 외래 키 설정
    private MemberEntity member;

}
