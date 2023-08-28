package com.project.phonecaseshop.service;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.Orders;
import com.project.phonecaseshop.entity.Product;
import com.project.phonecaseshop.entity.dto.orderDto.OrderRequestDto;
import com.project.phonecaseshop.entity.dto.orderDto.OrderResponseDto;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.repository.OrderRepository;
import com.project.phonecaseshop.repository.ProductRepository;
import com.project.phonecaseshop.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public String createOrder(OrderRequestDto orderRequestDto) {

        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberEmail(currentMemberId);
        Optional<Product> product = productRepository.findById(orderRequestDto.getProductId());

        if (member != null && product.isPresent()) {
            Orders orders = Orders.builder()
                    .orderCount(orderRequestDto.getOrderCount())
                    .productDesign(orderRequestDto.getProductDesign())
                    .productModel(orderRequestDto.getProductModel())
                    .memberId(member)
                    .productId(product.get())
                    .build();

            orderRepository.save(orders);
            return "주문에 성공하였습니다";
        }
        // 예외처리
        return "주문이 실패되었습니다";
    }

    public List<OrderResponseDto> getAllOrder() {
        List<Orders> all = orderRepository.findAll();

        return all.stream().map(order -> {
            return OrderResponseDto.builder()
                    .orderId(order.getOrderId())
                    .orderCount(order.getOrderCount())
                    .productDesign(order.getProductDesign())
                    .productModel(order.getProductModel())
                    .productId(order.getProductId())
                    .memberId(order.getMemberId())
                    .build();
        }).collect(Collectors.toList());
    }
}
