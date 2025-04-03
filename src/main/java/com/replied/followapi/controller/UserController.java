package com.replied.followapi.controller;

import com.replied.followapi.dto.FollowResponseDTO;
import com.replied.followapi.model.FollowRequest;
import com.replied.followapi.model.User;
import com.replied.followapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestParam String username,
            @RequestParam String birthdate) {
        User user = userService.registerUser(username, birthdate);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(
            @RequestParam UUID followerId,
            @RequestParam UUID followeeId) {
        userService.followUser(followerId, followeeId);
        return ResponseEntity.ok("Follow request sent.");
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FollowRequest>> getFollowRequests(
            @RequestParam UUID userId) {
        return ResponseEntity.ok(userService.getFollowRequests(userId));
    }

    @PostMapping("/block")
    public ResponseEntity<String> blockUser(
            @RequestParam UUID userId,
            @RequestParam UUID blockedUserId) {
        userService.blockUser(userId, blockedUserId);
        return ResponseEntity.ok("User blocked.");
    }

    @PostMapping("/requests/accept")
    public ResponseEntity<String> acceptFollowRequest(@RequestParam UUID requestId) {
        userService.acceptFollowRequest(requestId);
        return ResponseEntity.ok("Follow request accepted.");
    }

    @PostMapping("/requests/deny")
    public ResponseEntity<String> denyFollowRequest(@RequestParam UUID requestId) {
        userService.denyFollowRequest(requestId);
        return ResponseEntity.ok("Follow request denied.");
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowResponseDTO>> getFollowers(@PathVariable UUID userId) {
        List<FollowResponseDTO> followers = userService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowResponseDTO>> getFollowing(@PathVariable UUID userId) {
        List<FollowResponseDTO> following = userService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

}
