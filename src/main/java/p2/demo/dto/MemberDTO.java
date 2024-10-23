package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;
import p2.demo.entity.MemberEntity;



@Getter
@Setter
public class MemberDTO {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private Integer memberBir;
    private Integer memberPhone;
    private String memberRole;

    //address회원가입시 필요
    private AddressDTO address;

    // MemberEntity를 MemberDTO로 변환
    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberBir(memberEntity.getMemberBir());
        memberDTO.setMemberPhone(memberEntity.getMemberPhone());
        memberDTO.setMemberRole(memberEntity.getMemberRole());
        return memberDTO;
    }
}

