package com.coursenet.springbasic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coursenet.springbasic.entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long>{
//	@Query("SELECT o FROM orders o WHERE o.goods_name = :goodsName")
//	Optional<Orders> findOrderByGoodsName(@Param("goodsName") String goodsName);
	
// @Query(value = "SELECT * FROM orders WHERE goods_name = :goodsName", nativeQuery = true);
// Optional<Orders> findByGoodsName(@Param("goodsName") String goodsName);
	
	Optional<Orders> findByGoodsName(String goodsName);
}
