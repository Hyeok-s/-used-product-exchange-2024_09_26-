package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private String shippingAddress;
    private String shippingMemo;
    private String paymentMethod;
    private String deliveryStatus;
    private Long buyerId;
    private Long productId;
}
