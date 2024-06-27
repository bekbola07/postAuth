package org.example.postauth.repo;

import org.example.postauth.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LikeRepo extends JpaRepository<Like, UUID> {
    List<Like> findAllByUserId(Integer user_id);
    List<Like> findAllByPostIdAndUserId(UUID post_id,Integer user_id);
}
