package com.haotianxu.twitterlike.rest;

import com.haotianxu.twitterlike.dao.PostRepository;
import com.haotianxu.twitterlike.dao.RoleRepository;
import com.haotianxu.twitterlike.dao.UserRepository;
import com.haotianxu.twitterlike.entity.Auth;
import com.haotianxu.twitterlike.entity.Post;
import com.haotianxu.twitterlike.entity.RoleId;
import com.haotianxu.twitterlike.entity.User;
import com.haotianxu.twitterlike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://haotianxuapp.s3-website.us-east-2.amazonaws.com", allowCredentials = "true")
@RequestMapping("/api")
public class UserRestController {
    private UserService userService;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private RoleRepository roleRepository;
    @Autowired
    public UserRestController(UserService theService, UserRepository theRepo, PostRepository thePost, RoleRepository theRole) {
        userService = theService;
        userRepository = theRepo;
        postRepository = thePost;
        roleRepository = theRole;
    }
    @PostMapping("/login")
    public boolean loginUser(@RequestBody User theUser) {
        String name = theUser.getUsername();
        String pwd = theUser.getPassword();
        Optional<User> temp = userRepository.findById(name);
        if (temp.isPresent()) {
            User user = temp.get();
            // Compare the password (ideally, passwords should be hashed and compared securely)
            return user.getPassword().equals(pwd);
        }

        return false;
    }
    @PostMapping("/signup")
    public boolean signup(@RequestBody User theUser) {
        String name = theUser.getUsername();
//        String pwd = theUser.getPassword();
        Optional<User> temp = userRepository.findById(name);
        if (temp.isPresent()) {
            return false;
        }
        else {
            userRepository.save(theUser);
            Auth theRole = new Auth(name, "ROLE_USER");
            roleRepository.save(theRole);
            return true;
        }
    }
    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    @PostMapping("/posts")
    public boolean createPost(@RequestBody Post thePost, @RequestHeader String username) {
        String name = thePost.getAuthorId();
        if (!name.equals(username)) { return false; }
        Optional<User> temp = userRepository.findById(name);
        if (temp.isPresent()) {
            postRepository.save(thePost);
            return true;
        }
        return false;
    }
    @PutMapping("/posts/{postId}")
    public boolean updatePost(@PathVariable String postId, @RequestBody Post postUpdate, @RequestHeader String username) {

        return postRepository.findById(postId).map(post -> {

            if (!post.getAuthorId().equals(username)) {
                return false;
            }

            post.setContent(postUpdate.getContent());
            post.setTitle(postUpdate.getTitle());
            // Update other fields as necessary
            post.setUpdatedAt(LocalDateTime.now());
            Post updatedPost = postRepository.save(post);
            return true;

        }).orElseGet(() -> false);
    }

    @DeleteMapping("/posts/{postId}")
    public boolean deletePost(@PathVariable String postId, @RequestHeader String username) {
        return postRepository.findById(postId).map(post -> {

            if (!post.getAuthorId().equals(username)) {
                // If the logged-in user is not the author of the post
                return false;
            }

            postRepository.delete(post);
            return true;

        }).orElseGet(() -> false);
    }

    @GetMapping("/posts/author/{authorId}")
    public List<Post> getPostsByAuthorId(@PathVariable String authorId) {
        return postRepository.findByAuthorId(authorId);
    }
    @GetMapping("/posts/sorted")
    public List<Post> getPostsSortedByTime() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    @PutMapping("/posts/{postId}/like")
    public boolean like(@PathVariable String postId, @RequestHeader String username) {
        return postRepository.findById(postId).map(post -> {
            // Update other fields as necessary
            Optional<User> temp = userRepository.findById(username);
            if (!temp.isPresent()) return false;
            List<String> users = post.getLikes();
            if (users.contains(username)) {
                users.remove(username);
            } else {
                users.add(username);
            }
            post.setLikes(users);
            Post updatedPost = postRepository.save(post);
            return true;

        }).orElseGet(() -> false);
    }
    @DeleteMapping("/manage/posts/{postId}")
    public boolean superDeletePost(@PathVariable String postId, @RequestHeader String username) {
        if (roleRepository.findById(new RoleId(username, "ROLE_ADMIN")).isEmpty() && roleRepository.findById(new RoleId(username, "ROLE_MANAGER")).isEmpty()) {
            return false;
        }
        return postRepository.findById(postId).map(post -> {

            postRepository.delete(post);
            return true;

        }).orElseGet(() -> false);
    }
    @GetMapping("/manage/roles")
    public List<Auth> getAllRoles(@RequestHeader String username) {
        if (userRepository.findById(username).isEmpty()) return new ArrayList<>();
        return roleRepository.findAll(Sort.by(Sort.Direction.ASC, "authority"));
    }
    @PutMapping("/manage/roles")
    public boolean setRole(@RequestBody Auth auth, @RequestHeader String username) {
        if (roleRepository.findById(new RoleId(username, "ROLE_ADMIN")).isEmpty()) {
            return false;
        }
        if (userRepository.findById(auth.getUsername()).isEmpty()) return false;
        roleRepository.save(auth);

        return true;
    }
    @GetMapping("/manage/user_role")
    public List<String> getUserRole(@RequestHeader String username) {
        if (userRepository.findById(username).isEmpty()) return new ArrayList<>();
        ArrayList<String> roles = new ArrayList<>();
        roles.add("USER");
        Optional<Auth> temp = roleRepository.findById(new RoleId(username, "ROLE_MANAGER"));
        if (temp.isPresent()) roles.add("MANAGER");
        temp = roleRepository.findById(new RoleId(username, "ROLE_ADMIN"));
        if (temp.isPresent()) roles.add("ADMIN");
        return roles;
    }
}
