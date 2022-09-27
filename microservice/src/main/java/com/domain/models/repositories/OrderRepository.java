package com.domain.models.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.domain.models.entities.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

  Optional<OrderEntity> findByGoodsName(String goodsName);
}
