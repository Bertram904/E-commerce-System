package com.fpt.ecommerce.repository;

import com.fpt.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.orderDetails WHERE o.member.id = :memberId")
    List<Order> findHistoryByMemberId(Long memberId);
}
