package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.Design;
import com.project.phonecaseshop.entity.Model;
import com.project.phonecaseshop.entity.Photo;
import com.project.phonecaseshop.repository.DesignRepository;
import com.project.phonecaseshop.repository.ModelRepository;
import com.project.phonecaseshop.repository.PhotoRepository;
import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.ListResult;
import com.project.phonecaseshop.responseApi.SingleResult;
import com.project.phonecaseshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/etc")
@RequiredArgsConstructor
public class TestController {

    private final ModelRepository modelRepository;
    private final DesignRepository designRepository;
    private final PhotoRepository photoRepository;
    private final ApiResponse apiResponse;
    private final ProductService productService;

    // Model 테이블 확인
    @GetMapping("/model")
    public ListResult<Model> getModel() {
        return apiResponse.getListResult(modelRepository.findAll());
    }

    // Photo 테이블 확인
    @GetMapping("/photo")
    public ListResult<Photo> getPhoto() {
        return apiResponse.getListResult(photoRepository.findAll());
    }

    // Design 테이블 확인
    @GetMapping("/design")
    public ListResult<Design> getDesign() {
        return apiResponse.getListResult(designRepository.findAll());
    }

    // AWS S3 사진 업로드 확인
//    @PostMapping("/upload")
//    public SingleResult<Photo> uploadFile() {
//        return apiResponse.getSingleResult(productService)
//    }
}
