package com.coursenet.springbasic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coursenet.springbasic.entity.Orders;
import com.coursenet.springbasic.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByUserNameAndPassword(String userName, String password);
}
