package com.project.phonecaseshop.repository;

import com.project.phonecaseshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByMember_MemberEmail(String memberEmail);
    Slice<Product> findSliceBy(Pageable pageable);
    Page<Product> findPageBy(Pageable pageable);
}
