package com.project.questapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.questapp.entities.Comment;
import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.CommentRepository;
import com.project.questapp.requests.CommentCreateRequest;
import com.project.questapp.requests.CommentUpdateRequest;

@Service
public class CommentService {
	
	private CommentRepository commentRepository;
	private UserService userService;
	private PostService postService;

	public CommentService(CommentRepository commentRepository,UserService userService,PostService postService) {
		this.userService = userService;
		this.postService = postService;
	}

	public List<Comment> getAllCommentsWithParam(Optional<Long> userId, Optional<Long> postId) {
		
		//user ıd gelip bu userin tüm commentlerini getir diyebilir.
		//post id gelip bu postun tüm commentlerini getir diyebilir
		//user ve post id verip bunun commentlerini getir diyebilir.
		//bu ihtimalleri tek tek if ile kontrol edeceğiz.
		
		if(userId.isPresent() && postId.isPresent()) {
			return commentRepository.findByUserIdAndPostId(userId.get(), postId.get()); //get diyince içerisideki value yı alıyoruz	
		}
		else if(userId.isPresent()) {
			return commentRepository.findByUserId(userId.get());		
		}
		else if(postId.isPresent()) {
			return commentRepository.findByPostId(postId.get());
		}
		else{
			return commentRepository.findAll();
		}
		
	}
	


	public Comment getOneComment(Long commentId) {
		
		return commentRepository.findById(commentId).orElse(null);
		
	}

	public Comment createOneComment(CommentCreateRequest request) {
		
		User user=userService.getOneUserById(request.getUserId());
		Post post=postService.getOnePostById(request.getPostId());
		
		if(user!=null || post!=null)
		{
			Comment toSave=new Comment();
			toSave.setId(request.getId());
			toSave.setPost(post);
			toSave.setUser(user);
			toSave.setText(request.getText());
			return commentRepository.save(toSave);
		}
		else {
			return null;
		}
		
		
	}

	public Comment updateOneComment(Long commentId,CommentUpdateRequest request) {
		
		Optional<Comment> comment=commentRepository.findById(commentId);
		
		if(comment.isPresent())
		{
			Comment toUpdateComment=comment.get();
			toUpdateComment.setText(request.getText());
			return commentRepository.save(toUpdateComment);
		}
		else
		{
			return null;
		}

	}

	public void deleteOneComment(Long commentId) {
		commentRepository.deleteById(commentId);
	}


	

}
