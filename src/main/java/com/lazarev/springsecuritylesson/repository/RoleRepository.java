package com.lazarev.springsecuritylesson.repository;

import com.lazarev.springsecuritylesson.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
