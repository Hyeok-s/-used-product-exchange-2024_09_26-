package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private String shoppingMemo;
    private String paymentMethod;
    private String deliveryStatus;
    private String shoppingAddress;
    private Long buyerId;
    private Long productId;
    private boolean trash = false;
}
