package com.project.questapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.questapp.entities.Like;
import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.PostRepository;
import com.project.questapp.requests.PostCreateRequest;
import com.project.questapp.requests.PostUpdateRequest;
import com.project.questapp.responses.LikeResponse;
import com.project.questapp.responses.PostResponse;


@Service

public class PostService {
	
	private PostRepository postRepository;
	private LikeService likeService;
	private UserService userService;

	public PostService(PostRepository postRepository, UserService userService) {
		this.postRepository = postRepository;
		this.userService = userService;
	
	}
	@Autowired
	public void setLikeService(LikeService likeService) {
		this.likeService = likeService;
	}

	public List<PostResponse> getAllPosts(Optional<Long> userId) {
		List<Post> list;
		if(userId.isPresent()) {
			 list = postRepository.findByUserId(userId.get());
		}else
			list = postRepository.findAll();
		return list.stream().map(p -> { 
			List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));
			return new PostResponse(p, likes);}).collect(Collectors.toList());
	}
	public Post getOnePostById(Long postId) {
		return postRepository.findById(postId).orElse(null);
	}

	public Post createOnePost(PostCreateRequest newPostRequest) {
		
		User user=userService.getOneUserById(newPostRequest.getUserId());
		if(user==null)
			return null;
		
		//PostCreateRequestteki degerleri post objesinde set edıyoruz.
	
		Post toSave=new Post(); //yeni post objesi oluşturuyoruz create ettıgımız ıcın
		toSave.setId(newPostRequest.getId());
		toSave.setText(newPostRequest.getText());
		toSave.setTitle(newPostRequest.getTitle());
		toSave.setUser(user);
		
		// return postRepository.save(newPostRequest);
		return postRepository.save(toSave);
		
	}

	public Post updateOnePostById(Long postId, PostUpdateRequest updatePost) { //update için tek update edilmesi gereken alan text ve title.
	
		Optional<Post> post=postRepository.findById(postId);
		if(post.isPresent())
		{
			Post toUpdate=post.get(); //update ettiğimiz için update edilecek postu getiriyoruz.
			toUpdate.setText(updatePost.getText());
			toUpdate.setTitle(updatePost.getTitle());
			postRepository.save(toUpdate);
			return toUpdate;
		}
		return null;
		
	}

	public void deleteOnePostById(Long postId) {
		
		
		 postRepository.deleteById(postId);
		
	}
	

}
