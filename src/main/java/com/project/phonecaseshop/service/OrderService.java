package com.project.phonecaseshop.service;

import com.project.phonecaseshop.config.exception.BusinessExceptionHandler;
import com.project.phonecaseshop.config.exception.ErrorCode;
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
    public int createOrder(OrderRequestDto orderRequestDto) {

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
            return 1;
        }
        throw new BusinessExceptionHandler("주문에 실패하였습니다.", ErrorCode.BUSINESS_EXCEPTION_ERROR);
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
