package com.project.phonecaseshop.service;

import com.project.phonecaseshop.config.exception.BusinessExceptionHandler;
import com.project.phonecaseshop.config.exception.ErrorCode;
import com.project.phonecaseshop.entity.*;
import com.project.phonecaseshop.entity.dto.productDto.ProductRequestDto;
import com.project.phonecaseshop.entity.dto.productDto.ProductResponseDto;
import com.project.phonecaseshop.repository.*;
import com.project.phonecaseshop.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final AmazonS3Service amazonS3Service;

    public int createProduct(ProductRequestDto productRequestDto) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findByMemberEmail(currentMemberId);

        Product product = Product.builder()
                .productName(productRequestDto.getProductName())
                .member(member)
                .productDiscount(productRequestDto.getProductDiscount())
                .productDeliveryPrice(productRequestDto.getProductDeliveryPrice())
                .productPrice(productRequestDto.getProductPrice())
                .build();

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

        return 1;
    }

    public Slice<ProductResponseDto> findProducts(Pageable pageable) {
        Slice<Product> sliceBy = productRepository.findSliceBy(pageable);
        return sliceBy.map(this::convertToResponseDto);
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
            throw new BusinessExceptionHandler("존재하지 않는 상품입니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }
    }

    @Transactional
    public int removeProduct(int id) {
        Optional<Product> productOptional = productRepository.findById(id);

        String currentMemberId = SecurityUtil.getCurrentMemberId();

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (product.getMember().getMemberEmail().equals(currentMemberId)) {
                List<Photo> productSet = photoRepository.findByProductId_ProductId(product.getProductId());
                for (Photo photo : productSet) {
                    String fileName = photo.getPhotoName();
                    amazonS3Service.remove(fileName);
                }
                modelRepository.deleteByProductId_ProductId(product.getProductId());
                designRepository.deleteByProductId_ProductId(product.getProductId());
                photoRepository.deleteByProductId_ProductId(product.getProductId());
                productRepository.delete(product);
                return 1;
            }
        }
        throw new BusinessExceptionHandler("상품 제거를 실패하였습니다.", ErrorCode.BUSINESS_EXCEPTION_ERROR);
    }

    public int updateProduct(int productId, ProductRequestDto productRequestDto) {
        Optional<Product> productDto = productRepository.findById(productId);

        if (productDto.isPresent()) {
            Product product = Product.builder()
                    .productId(productId)
                    .productName(productRequestDto.getProductName())
                    .productPrice(productRequestDto.getProductPrice())
                    .productDiscount(productRequestDto.getProductDiscount())
                    .productDeliveryPrice(productRequestDto.getProductDeliveryPrice())
                    .member(productDto.get().getMember())
                    .build();

            productRepository.save(product);
            return 1;
        }
        throw new BusinessExceptionHandler("상품 수정에 실패했습니다", ErrorCode.BUSINESS_EXCEPTION_ERROR);
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
}
