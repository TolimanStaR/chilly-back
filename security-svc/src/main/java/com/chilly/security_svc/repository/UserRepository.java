package com.chilly.security_svc.repository;

import com.chilly.security_svc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
