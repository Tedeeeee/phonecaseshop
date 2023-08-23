package com.project.phonecaseshop.repository;

import com.project.phonecaseshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByMember_MemberEmail(String memberEmail);
}
