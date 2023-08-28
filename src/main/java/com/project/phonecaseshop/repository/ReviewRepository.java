package com.project.phonecaseshop.repository;

import com.project.phonecaseshop.entity.Product;
import com.project.phonecaseshop.entity.Review;
import com.project.phonecaseshop.entity.dto.reviewDto.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Page<Review> findByProductId(Product productId, PageRequest pageRequest);
}
