package com.replied.followapi.service;

import com.replied.followapi.dto.FollowResponseDTO;
import com.replied.followapi.model.Block;
import com.replied.followapi.model.FollowRequest;
import com.replied.followapi.model.User;
import com.replied.followapi.repository.BlockRepository;
import com.replied.followapi.repository.FollowRepository;
import com.replied.followapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;
    private UUID user1Id;
    private UUID user2Id;

    @BeforeEach
    public void setUp() {
        user1Id = UUID.randomUUID();
        user2Id = UUID.randomUUID();

        user1 = new User();
        user1.setId(user1Id);
        user1.setUsername("User1");
        user1.setBirthdate("1990-01-01");

        user2 = new User();
        user2.setId(user2Id);
        user2.setUsername("User2");
        user2.setBirthdate("1992-02-02");
    }
    @Test
    public void testRegisterUser() {
        when(userRepository.save(any(User.class))).thenReturn(user1);

        User registeredUser = userService.registerUser("User1", "1990-01-01");

        assertNotNull(registeredUser);
        assertEquals("User1", registeredUser.getUsername());
        assertEquals("1990-01-01", registeredUser.getBirthdate());
    }
    @Test
    public void testFollowUser_whenNotBlocked() {
        when(blockRepository.existsByBlockerIdAndBlockedUserId(any(UUID.class), any(UUID.class))).thenReturn(false);
        when(followRepository.save(any(FollowRequest.class))).thenReturn(new FollowRequest());

        userService.followUser(user1Id, user2Id);

        verify(followRepository, times(1)).save(any(FollowRequest.class));
    }
    @Test
    public void testFollowUser_whenBlocked() {
        when(blockRepository.existsByBlockerIdAndBlockedUserId(user2Id, user1Id)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.followUser(user1Id, user2Id);
        });

        assertEquals("You are blocked by this user and cannot follow them.", exception.getMessage());
    }
    @Test
    public void testAcceptFollowRequest() {
        FollowRequest followRequest = new FollowRequest();
        followRequest.setId(UUID.randomUUID());
        followRequest.setFollowerId(user1Id);
        followRequest.setFolloweeId(user2Id);
        followRequest.setStatus(FollowRequest.RequestStatus.PENDING);

        when(followRepository.findById(any(UUID.class))).thenReturn(Optional.of(followRequest));
        when(followRepository.save(any(FollowRequest.class))).thenReturn(followRequest);

        userService.acceptFollowRequest(followRequest.getId());

        assertEquals(FollowRequest.RequestStatus.ACCEPTED, followRequest.getStatus());
        verify(followRepository, times(1)).save(followRequest);
    }
    @Test
    public void testBlockUser() {
        when(blockRepository.save(any(Block.class))).thenReturn(new Block());
        doNothing().when(followRepository).deleteByFollowerIdAndFolloweeId(any(UUID.class), any(UUID.class));

        userService.blockUser(user1Id, user2Id);

        verify(blockRepository, times(1)).save(any(Block.class));
        verify(followRepository, times(1)).deleteByFollowerIdAndFolloweeId(user1Id, user2Id);
        verify(followRepository, times(1)).deleteByFollowerIdAndFolloweeId(user2Id, user1Id);
    }
    @Test
    public void testGetFollowers_whenBlocked() {
        FollowRequest followRequest = new FollowRequest();
        followRequest.setFollowerId(user1Id);
        followRequest.setFolloweeId(user2Id);
        followRequest.setStatus(FollowRequest.RequestStatus.ACCEPTED);

        when(followRepository.findByFolloweeIdAndStatus(user2Id, FollowRequest.RequestStatus.ACCEPTED))
                .thenReturn(Collections.singletonList(followRequest));
        when(blockRepository.existsByBlockerIdAndBlockedUserId(user2Id, user1Id)).thenReturn(true);

        List<FollowResponseDTO> followers = userService.getFollowers(user2Id);

        assertTrue(followers.isEmpty(), "Blocked user should not be in the followers list.");
    }
    @Test
    public void testGetFollowers_whenNotBlocked() {
        FollowRequest followRequest = new FollowRequest();
        followRequest.setFollowerId(user1Id);
        followRequest.setFolloweeId(user2Id);
        followRequest.setStatus(FollowRequest.RequestStatus.ACCEPTED);

        when(followRepository.findByFolloweeIdAndStatus(user2Id, FollowRequest.RequestStatus.ACCEPTED))
                .thenReturn(Collections.singletonList(followRequest));
        when(blockRepository.existsByBlockerIdAndBlockedUserId(user2Id, user1Id)).thenReturn(false);
        when(userRepository.findById(user1Id)).thenReturn(Optional.of(user1));

        List<FollowResponseDTO> followers = userService.getFollowers(user2Id);

        assertEquals(1, followers.size(), "User should be in the followers list.");
        assertEquals(user1.getUsername(), followers.get(0).getUsername());
    }
    @Test
    public void testGetFollowing() {
        FollowRequest followRequest = new FollowRequest();
        followRequest.setFollowerId(user2Id);
        followRequest.setFolloweeId(user1Id);
        followRequest.setStatus(FollowRequest.RequestStatus.ACCEPTED);

        when(followRepository.findByFollowerIdAndStatus(user2Id, FollowRequest.RequestStatus.ACCEPTED))
                .thenReturn(Collections.singletonList(followRequest));
        when(userRepository.findById(user1Id)).thenReturn(Optional.of(user1));

        List<FollowResponseDTO> following = userService.getFollowing(user2Id);

        assertEquals(1, following.size(), "User should be in the following list.");
        assertEquals(user1.getUsername(), following.get(0).getUsername());
    }
}
