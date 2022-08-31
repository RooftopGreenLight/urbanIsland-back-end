package rooftopgreenlight.urbanisland.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import rooftopgreenlight.urbanisland.domain.chat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>,
        QuerydslPredicateExecutor<ChatRoom> {

    @Query("select c from ChatRoom c where c.rooftopId=:rooftopId and c.memberId=:memberId " +
            "and c.ownerId =:ownerId")
    Optional<ChatRoom> findChatRoom(@Param("rooftopId") Long rooftopId, @Param("ownerId") Long ownerId, @Param("memberId") Long memberId);

    @Query("select c from ChatRoom c where c.rooftopId=:rooftopId and c.ownerId=:ownerId")
    List<ChatRoom> findChatRoomByOwnerId(@Param("rooftopId") Long rooftopId, @Param("ownerId") Long ownerId);
}
