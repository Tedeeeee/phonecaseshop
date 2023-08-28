package com.project.phonecaseshop.repository;

import com.project.phonecaseshop.entity.Design;
import com.project.phonecaseshop.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {
    List<Model> findByProductId_ProductId(int productId);
    void deleteByProductId_ProductId(int productId);

}
