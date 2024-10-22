package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductHistoryDTO {
    private Long id;
    private String pName;
    private String pContent;
    private Integer pPrice;
    private Long memberId;

}
