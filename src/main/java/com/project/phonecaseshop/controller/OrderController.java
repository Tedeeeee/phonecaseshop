package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.dto.orderDto.OrderRequestDto;
import com.project.phonecaseshop.entity.dto.orderDto.OrderResponseDto;
import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
import com.project.phonecaseshop.responseApi.ListResult;
import com.project.phonecaseshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ApiResponse apiResponse;
    private final OrderService orderService;

    @PostMapping("")
    public CommonResult createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return apiResponse.getSuccessResult(orderService.createOrder(orderRequestDto));
    }

    @GetMapping("/all")
    public ListResult<OrderResponseDto> getAllOrder() {
        return apiResponse.getListResult(orderService.getAllOrder());
    }
}
