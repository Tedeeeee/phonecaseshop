package com.project.phonecaseshop.service;

import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.Order;
import com.project.phonecaseshop.entity.dto.orderDto.OrderRequestDto;
import com.project.phonecaseshop.repository.MemberRepository;
import com.project.phonecaseshop.repository.OrderRepository;
import com.project.phonecaseshop.repository.ProductRepository;
import com.project.phonecaseshop.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        productRepository.findById(orderRequestDto.)

        if (member != null) {
            Order.builder()
                    .orderCount(orderRequestDto.getOrderCount())
                    .productDesign(orderRequestDto.getProductDesign())
                    .productModel(orderRequestDto.getProductModel())
                    .memberId(member)
                    .productId()
                    .build();
        }

        return "주문이 실패되었습니다";
    }
}
