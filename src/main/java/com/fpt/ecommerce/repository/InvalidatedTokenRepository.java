package com.fpt.ecommerce.repository;

import com.fpt.ecommerce.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

}
