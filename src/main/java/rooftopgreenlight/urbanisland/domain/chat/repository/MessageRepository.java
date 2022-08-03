package rooftopgreenlight.urbanisland.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import rooftopgreenlight.urbanisland.domain.chat.entity.Message;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Long>,
        QuerydslPredicateExecutor<Message> {
    @Query("select m from Message m join fetch m.member where m.member.id =:memberId and m.chatRoom.id =:roomId")
    List<Message> findByJoinFetchMember(@Param(value = "memberId") Long memberId,
                                        @Param(value = "roomId") Long roomId);
}
