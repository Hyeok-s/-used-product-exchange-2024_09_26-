package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private String postcode;
    private String address;
    private String detailAddress;
    private Long memberId;
}
