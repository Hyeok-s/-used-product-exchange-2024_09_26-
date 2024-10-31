package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductHistoryDTO {
    private String pName;
    private String pContent;
    private Integer pPrice;
    private Long productId;
    private String pic;
    private String deliveryStatus;
    private boolean isOrder;

    public ProductHistoryDTO(String pName, int pPrice, String pContent, Long productId, String pic, String deliveryStatus, boolean isOrder) {
        this.pName = pName;
        this.pContent = pContent;
        this.pPrice = pPrice;
        this.productId = productId;
        this.pic = pic;
        this.deliveryStatus = deliveryStatus;
        this.isOrder = isOrder;
    }
}
