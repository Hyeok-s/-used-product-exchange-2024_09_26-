package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String pName;
    private String pContent;
    private Integer pPrice;
    private String pType;
    private Long member_id;
    private String pState;

}
