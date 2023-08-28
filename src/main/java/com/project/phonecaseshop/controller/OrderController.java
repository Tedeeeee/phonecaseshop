package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.dto.orderDto.OrderRequestDto;
import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
import com.project.phonecaseshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ApiResponse apiResponse;
    private final OrderService orderService;

    @GetMapping("")
    public CommonResult createOrder(@RequestBody OrderRequestDto orderRequestDto) {

        String result = orderService.createOrder(orderRequestDto);

        if (result.equals("주문이 완료되었습니다")) {
            return apiResponse.getSuccessResult(1);
        }
        return apiResponse.getFailResult("500", result);
    }
}
