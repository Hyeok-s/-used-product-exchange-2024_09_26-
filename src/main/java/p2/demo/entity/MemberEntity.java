package p2.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "members")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NonNull
    private String memberEmail;

    @Column
    @NonNull
    private String memberPassword;

    @Column
    @NonNull
    private String memberName;

    @Column
    @NonNull
    private Integer memberBir;

    @Column
    @NonNull
    private Integer memberPhone;

    @Column
    @NonNull
    private String memberRole;

    {
        this.memberRole="user";
    }

}
