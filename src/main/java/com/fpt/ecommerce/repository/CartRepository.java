package com.fpt.ecommerce.repository;

import com.fpt.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("select c from Cart c " +
    "left join fetch c.cartItems ci " +
    "left join fetch ci.product p " +
    "where c.member.id = :memberId")
    Optional<Cart> findByMemberIdWithItems(@Param("memberId") Long memberId);
    Optional<Cart> findByMemberId(Long memberId);
}
