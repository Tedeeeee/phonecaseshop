package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.dto.productDto.ProductRequestDto;
import com.project.phonecaseshop.entity.dto.productDto.ProductResponseDto;
import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
import com.project.phonecaseshop.responseApi.ListResult;
import com.project.phonecaseshop.responseApi.SingleResult;
import com.project.phonecaseshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ApiResponse apiResponse;
    private final ProductService productService;

    // 전체 상품 가져오기
    @GetMapping("")
    public ListResult<ProductResponseDto> getProductList() {
        System.out.println("durl");
        return apiResponse.getListResult(productService.findProducts());
    }

    // 하나의 상품 가져오기
    @GetMapping("/{id}")
    public SingleResult<ProductResponseDto> getProduct(@PathVariable int id) {
        return apiResponse.getSingleResult(productService.findProduct(id));
    }

    // 나의 상품 가져오기
    @GetMapping("/my-products")
    public ListResult<ProductResponseDto> getMyProducts() {
        return apiResponse.getListResult(productService.getMyProducts());
    }


    // 상품 생성
    @PostMapping("/new")
    public CommonResult createProduct(@RequestBody ProductRequestDto productRequestDto) {
        String result = productService.createProduct(productRequestDto);

        if (result.equals("제품이 생성되었습니다")) {
            return apiResponse.getSuccessResult(1);
        } else {
            return apiResponse.getFailResult("500", result);
        }
    }

     // 상품 수정하기
    @PutMapping("/{productId}")
    public CommonResult updateProduct(@PathVariable int productId, @RequestBody ProductRequestDto productRequestDto) {
        String result = productService.updateProduct(productId, productRequestDto);

        if (result.equals("수정을 성공했습니다")) {
            return apiResponse.getSuccessResult(1);
        } else {
            return apiResponse.getFailResult("500", result);
        }
    }

    @DeleteMapping("/removal/{id}")
    public CommonResult deleteProduct(@PathVariable int id) {

        String result = productService.removeProduct(id);

        if (result.equals("제품이 제거되었습니다")) {
            return apiResponse.getSuccessResult(1);
        } else {
            return apiResponse.getFailResult("500", result);
        }
    }
}
