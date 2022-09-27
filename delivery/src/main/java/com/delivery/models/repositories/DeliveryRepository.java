package com.delivery.models.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delivery.models.entities.DeliveryEntity;

public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
  Optional<DeliveryEntity> findByOrderId(long orderId);

  Optional<DeliveryEntity> findByInvoice(String invoice);

}