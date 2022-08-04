package rooftopgreenlight.urbanisland.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rooftopgreenlight.urbanisland.domain.chat.entity.ChatRoom;
import rooftopgreenlight.urbanisland.domain.chat.entity.Message;
import rooftopgreenlight.urbanisland.domain.chat.repository.MessageRepository;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final MessageRepository messageRepository;

    @Transactional
    public void sendMessage(Long roomId, Long memberId, String content) {
        Member findMember = memberService.findById(memberId);
        ChatRoom findRoom = chatRoomService.findById(roomId);

        LocalDateTime date = LocalDateTime.now();

        Message message = Message.createMessage()
                .content(content)
                .sendTime(date)
                .build();

        message.updateMember(findMember);
        message.updateChatRoom(findRoom);
        messageRepository.save(message);
    }


    public List<Message> getMessageByRoomId(Long memberId, Long roomId) {
        return messageRepository.findByJoinFetchMember(memberId, roomId);
    }

    public Message getFirstMsgJoinFetchMember(Long memberId, Long roomId) {
        return messageRepository.findByJoinFetchMember(memberId, roomId)
                .stream().findFirst().orElse(null);
    }
}
