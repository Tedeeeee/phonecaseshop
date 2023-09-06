package com.project.phonecaseshop.controller;

import com.project.phonecaseshop.responseApi.ApiResponse;
import com.project.phonecaseshop.responseApi.CommonResult;
import com.project.phonecaseshop.responseApi.SingleResult;
import com.project.phonecaseshop.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class ImageController {

    private final AmazonS3Service amazonS3Service;
    private final ApiResponse apiResponse;

    @PostMapping("")
    public SingleResult<String> CheckFile(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        return apiResponse.getSingleResult(amazonS3Service.upLoad(multipartFile, "upload"));
    }

}
