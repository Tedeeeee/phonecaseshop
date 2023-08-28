package com.project.phonecaseshop.service;

import com.project.phonecaseshop.entity.*;
import com.project.phonecaseshop.entity.dto.productDto.ProductRequestDto;
import com.project.phonecaseshop.entity.dto.productDto.ProductResponseDto;
import com.project.phonecaseshop.repository.*;
import com.project.phonecaseshop.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

        Product product = Product.builder()
                .productName(productRequestDto.getProductName())
                .member(member)
                .productDiscount(productRequestDto.getProductDiscount())
                .productDeliveryPrice(productRequestDto.getProductDeliveryPrice())
                .productPrice(productRequestDto.getProductPrice())
                .build();

        System.out.println("product = " + product.toString());
        productRepository.save(product);

        for (Model modelEn : productRequestDto.getProductModel()) {
            Model model = Model.builder()
                    .productId(product)
                    .modelName(modelEn.getModelName())
                    .build();
            modelRepository.save(model);
        }

        for (Design designEn : productRequestDto.getProductDesign()) {
            Design design = Design.builder()
                    .productId(product)
                    .designName(designEn.getDesignName())
                    .build();
            designRepository.save(design);
        }

        for (Photo photoEn : productRequestDto.getProductPhoto()) {
            Photo photo = Photo.builder()
                    .productId(product)
                    .photoName(photoEn.getPhotoName())
                    .build();
            photoRepository.save(photo);
        }

        return "제품이 생성되었습니다";
    }

    public List<ProductResponseDto> findProducts() {
        List<Product> all = productRepository.findAll();

        return all.stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    public List<ProductResponseDto> getMyProducts() {
        String memberEmail = SecurityUtil.getCurrentMemberId();

        List<Product> myProduct = productRepository.findByMember_MemberEmail(memberEmail);

        return myProduct.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto findProduct(int id) {
        Optional<Product> product = productRepository.findById(id);

        Product findProduct = product.orElse(null);

        if (findProduct != null) {
            return convertToResponseDto(findProduct);
        } else {
            return null;
        }
    }

    @Transactional
    public String removeProduct(int id) {
        Optional<Product> productOptional = productRepository.findById(id);

        String currentMemberId = SecurityUtil.getCurrentMemberId();

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (product.getMember().getMemberEmail().equals(currentMemberId)) {
                modelRepository.deleteByProductId_ProductId(product.getProductId());
                designRepository.deleteByProductId_ProductId(product.getProductId());
                photoRepository.deleteByProductId_ProductId(product.getProductId());
                productRepository.delete(product);
                return "제품이 제거되었습니다";
            }
        }
        // 예외 처리 생성 예정
        return "실패했습니다";
    }


    private ProductResponseDto convertToResponseDto(Product product) {
        int productId = product.getProductId();
        List<Design> productDesigns = designRepository.findByProductId_ProductId(productId);
        List<Model> productModels = modelRepository.findByProductId_ProductId(productId);
        List<Photo> productPhotos = photoRepository.findByProductId_ProductId(productId);

        return ProductResponseDto.builder()
                .productId(productId)
                .memberEmail(product.getMember().getMemberEmail())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productDiscount(product.getProductDiscount())
                .productDeliveryPrice(product.getProductDeliveryPrice())
                .productDesign(productDesigns)
                .productModel(productModels)
                .productPhoto(productPhotos)
                .build();
    }

    //=========================================================

}
