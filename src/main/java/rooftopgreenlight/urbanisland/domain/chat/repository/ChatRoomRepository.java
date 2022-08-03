package rooftopgreenlight.urbanisland.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import rooftopgreenlight.urbanisland.domain.chat.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>,
        QuerydslPredicateExecutor<ChatRoom> {
    Optional<ChatRoom> findByRooftopIdAndAndMemberId(Long rooftopId, Long memberId);
}
