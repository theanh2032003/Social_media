package com.example.demo.controller;

import com.example.demo.file.FileUpLoad;
import com.example.demo.mapper.impl.PostMapper;
import com.example.demo.model.User;
import com.example.demo.model.dto.PostDto;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.service.PostAnalyticService;
import com.example.demo.service.impl.PostAnalyticServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Post;
import com.example.demo.service.impl.PostServiceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FileUpLoad fileUpLoad;

    @Autowired
    private PostAnalyticServiceImpl postAnalyticService;

    private final String PATH_IMAGE = "D:/Tai_lieu_PTIT/ttcs/Frontend/mxh/public/post/";

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/post_newfeed")
    public ResponseEntity<?> getPostOnNewFeed(HttpServletRequest request){
        Long userId = jwtProvider.getIdFromHttpRequest(request);
        List<Post> posts = postService.getAllPostByFriend(userId);
        Collections.sort(posts, Comparator.comparing(Post::getCreateAt));
        Collections.reverse(posts);
        List<PostDto> postDtos = posts.stream().map(post -> postMapper.mapTo(post)).collect(Collectors.toList());
        return new ResponseEntity<>(postDtos,HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId){
        Optional<Post> post = postService.findById(postId);
        if(post.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(postMapper.mapTo(post.get()), HttpStatus.OK);
    }

    @GetMapping("/post_profile")
    public ResponseEntity<?> getPostOnProfile(@RequestParam Long userId){

        List<Post> posts = postService.getAllPostByUserId(userId);
        Collections.reverse(posts);
        List<PostDto> postDtos = posts.stream().map(post -> postMapper.mapTo(post)).collect(Collectors.toList());
        return new ResponseEntity<>(postDtos,HttpStatus.OK);
    }

    @GetMapping("/post_like")
    public ResponseEntity<?> getLikePost(@RequestParam Long userId){

        List<Post> posts = postService.getAllPostByUserLike(userId);
        List<PostDto> postDtos = posts.stream().map(post -> postMapper.mapTo(post)).collect(Collectors.toList());
        return new ResponseEntity<>(postDtos,HttpStatus.OK);
    }

    @PostMapping(value = "/save",produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> savePost(@RequestParam("content") String content,@RequestParam(name = "images", required = false) MultipartFile[] multipartFiles , HttpServletRequest request) throws IOException {
        String jwtToken = jwtProvider.parseJwtToken(request);
        Long userId = jwtProvider.getIdByJwtToken(jwtToken);
        System.out.println(content);


        User user = userService.findById(userId).get();
        Post savedPost = postService.saveNewPost(new Post(content, LocalDateTime.now(),user,null,null));

        if(multipartFiles != null){
            List<String> images = fileUpLoad.saveFile(PATH_IMAGE,savedPost.getId().toString(),multipartFiles);
            System.out.println(images);
            List<String> subImages = images.stream().map(image -> image.substring(image.indexOf("\\post"))).collect(Collectors.toList());
            System.out.println(subImages);

            savedPost.setImages(subImages);
            postService.updatePost(savedPost);
        }

        return new ResponseEntity<>(savedPost,HttpStatus.CREATED);
    }

    @PatchMapping("/change_post")
    public ResponseEntity<?> changePost(@RequestParam("id") Long postId ,@RequestParam("content") String content,@RequestParam("images") MultipartFile[] multipartFiles) throws IOException {
        Optional<Post> post = postService.findById(postId);
        if(post.isEmpty()){
            throw new IllegalArgumentException("post is not exist");
        }

        post.get().setContent(content);

        post.get().getComments().clear();
        List<String> images = fileUpLoad.saveFile(PATH_IMAGE,postId.toString(),multipartFiles);
        post.get().setImages(images);
        postService.updatePost(post.get());
        return new ResponseEntity<>("post has been changed",HttpStatus.OK);
    }

    @PostMapping("/like_post/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId, HttpServletRequest request){
        String jwtToken = jwtProvider.parseJwtToken(request);
        Long userId = jwtProvider.getIdByJwtToken(jwtToken);

        boolean isLiked = postService.checkIfUserIsLikePost(userId,postId);
        if(!isLiked){
            postService.likePost(userId,postId);
            return new ResponseEntity<>(true,HttpStatus.CREATED);
        }else{
            postService.unlikePost(userId,postId);
            return new ResponseEntity<>(false,HttpStatus.OK);

        }




    }

    @GetMapping("/like_count/{postId}")
    public ResponseEntity<?> getLikesOfPost(@PathVariable Long postId){
        Long likes = postAnalyticService.getLikesOfPost(postId);

        return new ResponseEntity<>(likes,HttpStatus.OK);
    }

    @GetMapping("/check_like/{postId}")
    public ResponseEntity<?> checkLikedPost(@PathVariable Long postId, HttpServletRequest request){
        String jwtToken = jwtProvider.parseJwtToken(request);
        Long userId = jwtProvider.getIdByJwtToken(jwtToken);

        boolean isLiked = postService.checkIfUserIsLikePost(userId,postId);
        return new ResponseEntity<>(isLiked,HttpStatus.OK);
    }


    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId){
        postService.deletePostById(postId);
        messagingTemplate.convertAndSend("/topic/post/delete",postId);
        return new ResponseEntity<>("post has been deleted",HttpStatus.OK);
    }


}
