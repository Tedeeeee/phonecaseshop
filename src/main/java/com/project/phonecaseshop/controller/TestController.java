package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.Design;
import com.project.phonecaseshop.entity.Member;
import com.project.phonecaseshop.entity.Model;
import com.project.phonecaseshop.entity.Photo;
import com.project.phonecaseshop.repository.DesignRepository;
import com.project.phonecaseshop.repository.ModelRepository;
import com.project.phonecaseshop.repository.PhotoRepository;
import com.project.phonecaseshop.responseApi.*;
import com.project.phonecaseshop.service.AmazonS3Service;
import com.project.phonecaseshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/etc")
@RequiredArgsConstructor
public class TestController {

    private final ModelRepository modelRepository;
    private final DesignRepository designRepository;
    private final PhotoRepository photoRepository;
    private final ApiResponse apiResponse;
    private final AmazonS3Service amazonS3Service;

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

    @DeleteMapping("/s3")
    public CommonResult deleteFile(@RequestParam String name) {
        return apiResponse.getSuccessResult(amazonS3Service.remove(name));
    }

}
