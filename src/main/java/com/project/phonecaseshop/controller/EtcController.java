package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.entity.Design;
import com.project.phonecaseshop.entity.Model;
import com.project.phonecaseshop.entity.Photo;
import com.project.phonecaseshop.repository.DesignRepository;
import com.project.phonecaseshop.repository.ModelRepository;
import com.project.phonecaseshop.repository.PhotoRepository;
import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.ListResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/etc")
@RequiredArgsConstructor
public class EtcController {

    private final ModelRepository modelRepository;
    private final DesignRepository designRepository;
    private final PhotoRepository photoRepository;
    private final ApiResponse apiResponse;

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
}
