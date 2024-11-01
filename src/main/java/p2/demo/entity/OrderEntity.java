package p2.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.ProductEntity;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String deliveryStatus;
    {
        this.deliveryStatus="w";
    }

    @Column(nullable = true)
    private String shoppingMemo;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String shoppingAddress;

    @Column(nullable = false)
    private boolean trash;
    {
        this.trash = false;
    }

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private MemberEntity buyer;  // users 테이블과의 관계

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductEntity product;  // product 테이블과의 관계
}
