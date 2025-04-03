package com.replied.followapi.dto;

import java.util.UUID;

public class FollowRequestDTO {
    private UUID followerId;
    private UUID followeeId;

    public UUID getFollowerId() {
        return followerId;
    }

    public void setFollowerId(UUID followerId) {
        this.followerId = followerId;
    }

    public UUID getFolloweeId() {
        return followeeId;
    }

    public void setFolloweeId(UUID followeeId) {
        this.followeeId = followeeId;
    }
}
