package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.dto.productDto.ProductRequestDto;
import com.project.phonecaseshop.entity.dto.productDto.ProductResponseDto;
import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
import com.project.phonecaseshop.responseApi.ListResult;
import com.project.phonecaseshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ApiResponse apiResponse;
    private final ProductService productService;

    @GetMapping("")
    public ListResult<ProductResponseDto> getProductList() {
        return apiResponse.getListResult(productService.findProducts());
    }

    @PostMapping("")
    public CommonResult createProduct(@RequestBody ProductRequestDto productRequestDto) {
        String result = productService.createProduct(productRequestDto);

        if (result.equals("제품이 생성되었습니다")) {
            return apiResponse.getSuccessResult(1);
        } else {
            return apiResponse.getFailResult("500", result);
        }
    }

    // 전체 제품 가져오기
//    @GetMapping("")
//    public List<ProductResponseDto> getAllProduct() {
//        return apiResponse.getListResult(productService.)
//    }
}
