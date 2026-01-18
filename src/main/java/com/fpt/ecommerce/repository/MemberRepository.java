package com.fpt.ecommerce.repository;

import com.fpt.ecommerce.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByUsername(String username);

    //Kiem tra ton tai
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
