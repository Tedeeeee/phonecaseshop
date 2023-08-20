package com.project.phonecaseshop.repository;

import com.project.phonecaseshop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberEmail(String username);
}
