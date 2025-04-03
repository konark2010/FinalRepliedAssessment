package com.replied.followapi.repository;

import com.replied.followapi.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BlockRepository extends JpaRepository<Block, UUID> {

    boolean existsByBlockerIdAndBlockedUserId(UUID blockerId, UUID blockedUserId);

    List<Block> findByBlockerId(UUID blockerId);
}