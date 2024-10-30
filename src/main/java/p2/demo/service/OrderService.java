package p2.demo.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import p2.demo.dto.OrderDTO;
import p2.demo.dto.ProductHistoryDTO;
import p2.demo.entity.*;
import p2.demo.repository.AddressRepository;
import p2.demo.repository.MemberRepository;
import p2.demo.repository.OrderRepository;
import p2.demo.repository.ProductRepository;
import p2.demo.repository.ProductsHistoryRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductsHistoryRepository productsHistoryRepository;

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
    public List<OrderEntity> getOrdersByStatusAndMemberId(String state, Long memberId) {
        return orderRepository.findByDeliveryStatusAndBuyerId(state, memberId);
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
    public List<OrderEntity> getOrdersByStatus(String status) {
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

    // 구매 내역 (삭제되지 않은 제품 + 삭제된 제품) 조회
    public List<ProductHistoryDTO> getCompletePurchaseHistory(String status) {

        List<OrderEntity> activeOrders = orderRepository.findByDeliveryStatus(status);
        List<ProductsHistoryEntity> deletedOrdersHistory = new ArrayList<>();
        deletedOrdersHistory= productsHistoryRepository.findAll();

        //하나의 리스트로 반환
        List<ProductHistoryDTO> completeHistory = new ArrayList<>();

        // OrderEntity OrderHistoryDTO 변환
        for (OrderEntity order : activeOrders) {
            completeHistory.add(new ProductHistoryDTO(
                    order.getProduct().getPName(),
                    order.getProduct().getPPrice(),
                    order.getProduct().getPContent(),
                    order.getProduct().getId(),
                    order.getProduct().getPic(),
                    status
            ));
        }
        for (ProductsHistoryEntity history : deletedOrdersHistory) {
            completeHistory.add(new ProductHistoryDTO(
                    history.getPName(),
                    history.getPPrice(),
                    history.getPContent(),
                    history.getId(),
                    history.getPic(),
                    status
            ));
        }
        return completeHistory;
    }
}
