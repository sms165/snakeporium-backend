package com.snakeporium_backend.repository;

import com.snakeporium_backend.entity.Category;
import com.snakeporium_backend.entity.Sex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SexRepository extends JpaRepository<Sex, Long> {
}
