package p2.demo.service;

import lombok.RequiredArgsConstructor;
import p2.demo.dto.OrderDTO;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.OrderEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.repository.MemberRepository;
import p2.demo.repository.OrderRepository;
import p2.demo.repository.ProductRepository;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    private final ProductRepository productRepository;

    // 주문을 저장하는 메서드
    public void saveOrder(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();

        // 주문 정보를 DTO에서 엔티티로 매핑
        orderEntity.setShippingAddress(orderDTO.getShippingAddress());
        orderEntity.setShippingMemo(orderDTO.getShippingMemo());
        orderEntity.setPaymentMethod(orderDTO.getPaymentMethod());
        orderEntity.setDeliveryStatus("asdf");

        // 구매자 및 상품 정보 설정
        MemberEntity buyer = memberRepository.findById(orderDTO.getBuyerId()).orElseThrow(() -> new IllegalArgumentException("Invalid buyer ID"));
        ProductEntity product = productRepository.findById(orderDTO.getProductId()).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        orderEntity.setBuyer(buyer);
        orderEntity.setProduct(product);

        // 주문 저장
        orderRepository.save(orderEntity);
    }
}
