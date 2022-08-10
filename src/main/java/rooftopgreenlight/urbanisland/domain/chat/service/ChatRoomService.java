package rooftopgreenlight.urbanisland.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rooftopgreenlight.urbanisland.api.common.exception.AuthorizationException;
import rooftopgreenlight.urbanisland.api.common.exception.NotFoundChatRoomException;
import rooftopgreenlight.urbanisland.domain.chat.entity.ChatRoom;
import rooftopgreenlight.urbanisland.domain.chat.entity.QChatRoom;
import rooftopgreenlight.urbanisland.domain.chat.repository.ChatRoomRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public Long joinChatRoom(Long rooftopId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findByRooftopIdAndAndMemberId(rooftopId, memberId).orElse(null);
        if(chatRoom == null) {
            ChatRoom newChatRoom = ChatRoom.createChatRoom()
                    .rooftopId(rooftopId)
                    .memberId(memberId)
                    .build();
            chatRoomRepository.save(newChatRoom);
            return newChatRoom.getId();
        }
        return chatRoom.getId();
    }

    public List<ChatRoom> getRoomList(Long memberId) {
        return StreamSupport.stream(
                chatRoomRepository.findAll(QChatRoom.chatRoom.memberId.eq(memberId)).spliterator(), false)
                .collect(Collectors.toList());
    }

    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> {
            throw new NotFoundChatRoomException("문의 내역을 찾을 수 없습니다.");
        });
    }

    public void deleteByRoomId(Long roomId) {
        chatRoomRepository.deleteById(roomId);
    }
}
