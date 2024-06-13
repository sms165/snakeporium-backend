package com.snakeporium_backend.repository;

import com.snakeporium_backend.entity.FAQ;
import com.snakeporium_backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository  extends JpaRepository<Review, Long> {

    List<Review> findAllByProductId(Long productId);
}
