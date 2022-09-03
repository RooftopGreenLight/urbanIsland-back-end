package rooftopgreenlight.urbanisland.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rooftopgreenlight.urbanisland.api.common.exception.AuthorizationException;
import rooftopgreenlight.urbanisland.domain.chat.entity.ChatRoom;
import rooftopgreenlight.urbanisland.domain.chat.entity.Message;
import rooftopgreenlight.urbanisland.domain.chat.repository.MessageRepository;
import rooftopgreenlight.urbanisland.domain.chat.service.dto.MessageDto;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final MessageRepository messageRepository;

    /**
     * 메시지 생성
     */
    @Transactional
    public LocalDateTime sendMessage(Long roomId, Long memberId, String content) {
        Member findMember = memberService.findById(memberId);
        ChatRoom findRoom = chatRoomService.getChatRoomById(roomId);

        LocalDateTime date = LocalDateTime.now();

        Message message = Message.createMessage()
                .content(content)
                .sendTime(date)
                .build();

        message.updateMember(findMember);
        message.updateChatRoom(findRoom);
        messageRepository.save(message);
        return date;
    }

    /**
     * 특정 채팅방의 메시지 목록 조회
     */
    public List<MessageDto> getMessageByRoomId(Long memberId, Long roomId) {
        List<Message> messages = messageRepository.findByJoinFetchMember(memberId, roomId);
        return messages.stream().map(message -> MessageDto.of(message.getId(), message.getContent(),
                message.getSendTime(), message.getMember().getId())).collect(Collectors.toList());
    }

    /**
     * 가장 마지막에 보낸 메시지 조회
     */
    public MessageDto getLastMessage(Long roomId) {
        Message message = messageRepository.findLastMessageOrdered(roomId)
                .stream().findFirst().orElse(null);
        if(message != null) return MessageDto.of(message.getId(), message.getContent(), message.getSendTime(), message.getMember().getId());
        return null;
    }

    /**
     * 문의 삭제하기
     */
    public void deleteMessageByRoomId(Long memberId, Long roomId) {
        ChatRoom findChatRoom = chatRoomService.getChatRoomById(roomId);
        if(!findChatRoom.getMemberId().equals(memberId)) {
            throw new AuthorizationException("권한이 없습니다.");
        }
        messageRepository.deleteBulkMessages(roomId);
        chatRoomService.deleteByRoomId(roomId);
    }
}
