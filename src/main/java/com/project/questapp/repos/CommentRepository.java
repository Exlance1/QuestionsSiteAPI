package com.project.questapp.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.questapp.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByUserIdAndPostId(Long userId, Long postId); //jpa repo getircek

	List<Comment> findByUserId(Long userId);

	List<Comment> findByPostId(Long postId);
	
	
	

}
