package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.dto.productDto.ProductRequestDto;
import com.project.phonecaseshop.entity.dto.productDto.ProductResponseDto;
import com.project.phonecaseshop.responseApi.*;
import com.project.phonecaseshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ApiResponse apiResponse;
    private final ProductService productService;

    // 전체 상품 가져오기
    @GetMapping("")
    public SliceResult<ProductResponseDto> getProductList(Pageable pageable) {
        return apiResponse.getSliceResult(productService.findProducts(pageable));
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
        return apiResponse.getSuccessResult(productService.createProduct(productRequestDto));
    }

     // 상품 수정하기
    @PutMapping("/{productId}")
    public CommonResult updateProduct(@PathVariable int productId, @RequestBody ProductRequestDto productRequestDto) {
        return apiResponse.getSuccessResult(productService.updateProduct(productId, productRequestDto));
    }

    @DeleteMapping("/removal/{id}")
    public CommonResult deleteProduct(@PathVariable int id) {
        return apiResponse.getSuccessResult(productService.removeProduct(id));
    }
}
