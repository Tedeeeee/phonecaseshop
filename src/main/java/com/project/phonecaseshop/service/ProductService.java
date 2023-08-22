package com.project.phonecaseshop.service;

import com.project.phonecaseshop.entity.*;
import com.project.phonecaseshop.entity.dto.productDto.ProductRequestDto;
import com.project.phonecaseshop.entity.dto.productDto.ProductResponseDto;
import com.project.phonecaseshop.repository.*;
import com.project.phonecaseshop.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ModelRepository modelRepository;
    private final DesignRepository designRepository;
    private final PhotoRepository photoRepository;

    public String createProduct(ProductRequestDto productRequestDto) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findByMemberEmail(currentMemberId);

        Product product = new Product();
        product.setMember(member);
        product.setProductName(productRequestDto.getProductName());
        product.setProductDiscount(productRequestDto.getProductDiscount());
        product.setProductDeliveryPrice(productRequestDto.getProductDeliveryPrice());
        product.setProductPrice(productRequestDto.getProductPrice());

        productRepository.save(product);

        for (Model modelEn : productRequestDto.getProductModel()) {
            Model model = new Model();
            model.setProductId(product);
            model.setModelName(modelEn.getModelName());
            modelRepository.save(model);
        }

        for (Design designEn : productRequestDto.getProductDesign()) {
            Design design = new Design();
            design.setProductId(product);
            design.setDesignName(designEn.getDesignName());
            designRepository.save(design);
        }

        for (Photo photoEn : productRequestDto.getProductPhoto()) {
            Photo photo = new Photo();
            photo.setProductId(product);
            photo.setPhotoName(photoEn.getPhotoName());
            photoRepository.save(photo);
        }

        return "제품이 생성되었습니다";
    }

    //======================================================
    public List<ProductResponseDto> findProducts() {
        List<Product> all = productRepository.findAll();


        return all.stream()
                .map(product ->{
                    Long productId = product.getProductId();
                    List<Design> productDesigns = designRepository.findByProductId_ProductId(productId);
                    List<Model> productModels = modelRepository.findByProductId_ProductId(productId);
                    List<Photo> productPhotos = photoRepository.findByProductId_ProductId(productId);

                    return ProductResponseDto.builder()
                        .productId(product.getProductId())
                        .memberEmail(product.getMember() != null ? product.getMember().getMemberEmail() : null)
                        .productName(product.getProductName())
                        .productPrice(product.getProductPrice())
                        .productDiscount(product.getProductDiscount())
                        .productDeliveryPrice(product.getProductDeliveryPrice())
                        .productDesign(productDesigns)
                        .productModel(productModels)
                        .productPhoto(productPhotos)
                        .build();
                })
                .toList();
    }
}
