package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;
import p2.demo.entity.AddressEntity;

@Getter
@Setter
public class AddressDTO {
    private Long id;
    private String postcode;
    private String address;
    private String detailAddress;
    private Long memberId;


}