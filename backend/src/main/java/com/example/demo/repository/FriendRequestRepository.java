package com.example.demo.repository;

import com.example.demo.model.FriendRequest;
import com.example.demo.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {

    @Query("select a from FriendRequest a where a.receiver.id = ?1 and a.acepted = false ")
    List<FriendRequest> getAllFriendRequestByReceive(Long receiveId);

    @Modifying
    @Transactional
    @Query("UPDATE FriendRequest a SET a.acepted = true WHERE a.id = ?1")
    void confirmFriendRequest(Long friendRequestId);

    @Query("select a from FriendRequest a where (a.sender.id = ?1 and a.receiver.id = ?2) or (a.sender.id = ?2 and a.receiver.id = ?1)")
    Optional<FriendRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
