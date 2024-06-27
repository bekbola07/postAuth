package org.example.postauth.repo;

import org.example.postauth.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepo extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByPostId(UUID post_id);
}
