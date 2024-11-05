package p2.demo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import p2.demo.dto.OrderDTO;
import p2.demo.entity.*;
import p2.demo.repository.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 주문을 저장하는 메서드
    public void saveOrder(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();

        // 주문 정보를 DTO에서 엔티티로 매핑
        orderEntity.setShoppingAddress(orderDTO.getShoppingAddress());
        orderEntity.setShoppingMemo(orderDTO.getShoppingMemo());
        orderEntity.setPaymentMethod(orderDTO.getPaymentMethod());

        // 구매자 및 상품 정보 설정
        MemberEntity buyer = memberRepository.findById(orderDTO.getBuyerId()).orElseThrow(() -> new IllegalArgumentException("Invalid buyer ID"));
        ProductEntity product = productRepository.findById(orderDTO.getProductId()).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        orderEntity.setBuyer(buyer);
        orderEntity.setProduct(product);

        // 주문 저장
        orderRepository.save(orderEntity);
    }

    //로그인 id와 상태로 가져오기
    public List<OrderEntity> getOrdersByStatusAndMemberIdAndFalse(String state, Long memberId) {
        return orderRepository.findByDeliveryStatusAndBuyerIdAndTrash(state, memberId, false);
    }

    public void updateOrderDeliveryStatus(Long productId, String deliveryStatus){
        OrderEntity orderentity = orderRepository.findByProductId(productId);
        orderentity.setDeliveryStatus(deliveryStatus);
    }

    public void deleteOrder(Long id){
        orderRepository.deleteById(id);
    }

    // productId로 OrderEntity 가져오기
    public OrderEntity getOrderByProductId(Long productId) {
        return orderRepository.findByProductId(productId);
    }


    //조건부 만족하는 상태 찾기
    public List<OrderEntity> getOrdersByStatus() {
        return orderRepository.findByDeliveryStatusIn(List.of("w", "ing", "f"));
    }

    //아이디로 가져오기
    public OrderEntity findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + id));
    }

    // 상태 업데이트
    public void updateOrderStatus(Long id, String newStatus) {
        OrderEntity order = findById(id);
        order.setDeliveryStatus(newStatus);
        orderRepository.save(order);
    }

    //배송상태에 따른 orderEntity
    public List<OrderEntity> getOrderByStatus(String status){
        return orderRepository.findByDeliveryStatus(status);
    }

    public boolean hasOrdersWithRestrictedStatus(Long memberId) {
        List<OrderEntity> orders = orderRepository.findByBuyerIdAndDeliveryStatusIn(memberId, Arrays.asList("w", "ing", "f"));
        return !orders.isEmpty();
    }

    @Transactional
    public void updateMemberIdToNull(Long memberId) {
        // orders의 memberId를 null로 업데이트
        orderRepository.updateBuyerIdToNull(memberId);
    }
}
