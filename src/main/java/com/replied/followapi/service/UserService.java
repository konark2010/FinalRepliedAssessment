package com.replied.followapi.service;

import com.replied.followapi.dto.FollowResponseDTO;
import com.replied.followapi.model.Block;
import com.replied.followapi.model.FollowRequest;
import com.replied.followapi.model.User;
import com.replied.followapi.repository.BlockRepository;
import com.replied.followapi.repository.FollowRepository;
import com.replied.followapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRequestRepository;
    private final BlockRepository blockRepository;

    public UserService(UserRepository userRepository, FollowRepository followRequestRepository,
                       BlockRepository blockRepository) {
        this.userRepository = userRepository;
        this.followRequestRepository = followRequestRepository;
        this.blockRepository = blockRepository;
    }

    // Register a new user
    public User registerUser(String username, String birthdate) {
        User user = new User();
        user.setUsername(username);
        user.setBirthdate(birthdate);
        return userRepository.save(user);
    }

    // Retrieve all users
    public List<User> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers;
    }

    // Send a follow request (blocked users cannot send follow requests)
    @Transactional
    public void followUser(UUID followerId, UUID followeeId) {
        // Check if the follower is blocked by the followee
        if (blockRepository.existsByBlockerIdAndBlockedUserId(followeeId, followerId)) {
            throw new RuntimeException("You are blocked by this user and cannot follow them.");
        }

        // Check if the followee is blocked by the follower
        if (blockRepository.existsByBlockerIdAndBlockedUserId(followerId, followeeId)) {
            throw new RuntimeException("You have blocked this user and cannot send follow requests.");
        }

        // Save follow request
        FollowRequest request = new FollowRequest();
        request.setFollowerId(followerId);
        request.setFolloweeId(followeeId);
        request.setStatus(FollowRequest.RequestStatus.PENDING);
        followRequestRepository.save(request);
    }

    // Accept a follow request
    @Transactional
    public void acceptFollowRequest(UUID requestId) {
        FollowRequest request = followRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(FollowRequest.RequestStatus.ACCEPTED);
        followRequestRepository.save(request);
    }

    // Deny a follow request
    @Transactional
    public void denyFollowRequest(UUID requestId) {
        followRequestRepository.deleteById(requestId);
    }

    // Block a user (removes any follow requests between these users)
    @Transactional
    public void blockUser(UUID blockerId, UUID blockedUserId) {
        // Create and save block record
        Block block = new Block();
        block.setBlockerId(blockerId);
        block.setBlockedUserId(blockedUserId);
        blockRepository.save(block);

        // Remove any existing follow requests between these users
        followRequestRepository.deleteByFollowerIdAndFolloweeId(blockerId, blockedUserId);
        followRequestRepository.deleteByFollowerIdAndFolloweeId(blockedUserId, blockerId);
    }

    // Helper method to check if two users are "Close Friends" (mutual followers)
    private boolean areCloseFriends(UUID userId1, UUID userId2) {
        return followRequestRepository.existsByFollowerIdAndFolloweeIdAndStatus(userId1, userId2, FollowRequest.RequestStatus.ACCEPTED)
                && followRequestRepository.existsByFollowerIdAndFolloweeIdAndStatus(userId2, userId1, FollowRequest.RequestStatus.ACCEPTED);
    }

    // Get followers of a user, excluding blocked users, and show birthdate for close friends
    public List<FollowResponseDTO> getFollowers(UUID userId) {
        List<FollowRequest> acceptedRequests = followRequestRepository
                .findByFolloweeIdAndStatus(userId, FollowRequest.RequestStatus.ACCEPTED);

        return acceptedRequests.stream()
                .map(request -> {
                    UUID followerId = request.getFollowerId();
                    // Exclude blocked users
                    if (blockRepository.existsByBlockerIdAndBlockedUserId(userId, followerId)) {
                        return null;
                    }
                    User user = userRepository.findById(followerId).orElseThrow();
                    boolean isCloseFriend = areCloseFriends(userId, followerId);
                    return new FollowResponseDTO(user.getId(), user.getUsername(), isCloseFriend ? user.getBirthdate() : "Hidden");
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Get following list of a user, and show birthdate for close friends
    public List<FollowResponseDTO> getFollowing(UUID userId) {
        List<FollowRequest> acceptedRequests = followRequestRepository.findByFollowerIdAndStatus(userId, FollowRequest.RequestStatus.ACCEPTED);

        return acceptedRequests.stream()
                .map(request -> {
                    UUID followeeId = request.getFolloweeId();
                    User user = userRepository.findById(followeeId).orElseThrow();
                    boolean isCloseFriend = areCloseFriends(userId, followeeId);
                    return new FollowResponseDTO(user.getId(), user.getUsername(), isCloseFriend ? user.getBirthdate() : "Hidden");
                })
                .collect(Collectors.toList());
    }

    // Retrieve all pending follow requests for a user
    public List<FollowRequest> getFollowRequests(UUID userId) {
        return followRequestRepository.findByFolloweeIdAndStatus(userId, FollowRequest.RequestStatus.PENDING);
    }
}
