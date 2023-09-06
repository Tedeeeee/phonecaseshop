package com.project.phonecaseshop.repository;

import com.project.phonecaseshop.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    void deleteByProductId_ProductId(int productId);

    List<Favorite> findByMemberId_MemberId(int memberId);
}
