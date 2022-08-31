package rooftopgreenlight.urbanisland.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rooftopgreenlight.urbanisland.domain.chat.entity.ChatRoom;
import rooftopgreenlight.urbanisland.domain.chat.entity.QChatRoom;
import rooftopgreenlight.urbanisland.domain.chat.repository.ChatRoomRepository;
import rooftopgreenlight.urbanisland.domain.chat.service.dto.ChatRoomDto;
import rooftopgreenlight.urbanisland.domain.common.exception.NoMatchMemberIdException;
import rooftopgreenlight.urbanisland.domain.common.exception.NotFoundChatRoomException;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.service.RooftopService;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final RooftopService rooftopService;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public Long joinChatRoom(Long rooftopId, Long ownerId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoom(rooftopId, ownerId, memberId).orElse(null);
        if(chatRoom == null) {
            ChatRoom newChatRoom = ChatRoom.createChatRoom()
                    .rooftopId(rooftopId)
                    .memberId(memberId)
                    .ownerId(ownerId)
                    .build();
            chatRoomRepository.save(newChatRoom);
            return newChatRoom.getId();
        }
        return chatRoom.getId();
    }

    public List<ChatRoomDto> getRoomList(Long memberId) {
        List<ChatRoom> chatRooms = StreamSupport.stream(
                        chatRoomRepository.findAll(QChatRoom.chatRoom.memberId.eq(memberId)).spliterator(), false)
                .collect(Collectors.toList());
        return chatRooms.stream().map(chatRoom -> ChatRoomDto.of(chatRoom.getId(), chatRoom.getRooftopId())).collect(Collectors.toList());
    }

    public ChatRoom getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> {
            throw new NotFoundChatRoomException("문의 내역을 찾을 수 없습니다.");
        });
    }

    public void deleteByRoomId(Long roomId) {
        chatRoomRepository.deleteById(roomId);
    }

    public List<ChatRoomDto> getRoomListByOwner(Long rooftopId, Long ownerId) {
        Rooftop rooftop = rooftopService.findByRooftopId(rooftopId);
        if(!rooftop.getMember().getId().equals(ownerId)) {
            throw new NoMatchMemberIdException("권한이 없습니다.");
        }
        List<ChatRoom> chatRoomList = chatRoomRepository.findChatRoomByOwnerId(rooftopId, ownerId);
        return chatRoomList.stream().map(chatRoom -> ChatRoomDto.of(chatRoom.getId(), chatRoom.getRooftopId())).collect(Collectors.toList());
    }

    public RooftopDto getRooftopAddressByRoomId(Long roomId) {
        ChatRoom chatRoom = getChatRoomById(roomId);
        Rooftop rooftop = rooftopService.findByRooftopId(chatRoom.getRooftopId());
        return RooftopDto.getRooftopAddress(rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail());
    }
}
