package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;
import p2.demo.entity.ProductEntity;


@Getter
@Setter
public class ProductDTO {
    private Long pId;
    private String pName;
    private String pContent;
    private Integer pPrice;
    private String pType;
    private Long member_id;
    private Character pState;


    public static ProductDTO toProductDTO(ProductEntity productEntity) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPId(productEntity.getPId());
        productDTO.setPName(productEntity.getPName());
        productDTO.setPContent(productEntity.getPContent());
        productDTO.setPPrice(productEntity.getPPrice());
        productDTO.setPType(productEntity.getPType());
        productDTO.setMember_id(productEntity.getMember().getId());  // 외래키 값 설정
        productDTO.setPState(productEntity.getPState());
        return productDTO;
    }


}
