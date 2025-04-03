package com.replied.followapi.repository;

import com.replied.followapi.model.FollowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<FollowRequest, UUID> {
    List<FollowRequest> findByFolloweeIdAndStatus(UUID followeeId, FollowRequest.RequestStatus status);
    List<FollowRequest> findByFollowerIdAndStatus(UUID followerId, FollowRequest.RequestStatus status);

    boolean existsByFollowerIdAndFolloweeIdAndStatus(UUID followerId, UUID followeeId, FollowRequest.RequestStatus status);
    void deleteByFollowerIdAndFolloweeId(UUID followerId, UUID followeeId);
}
