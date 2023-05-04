package rooftopgreenlight.urbanisland.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rooftopgreenlight.urbanisland.domain.chat.ChatRoom;
import rooftopgreenlight.urbanisland.domain.chat.ChatRoomType;
import rooftopgreenlight.urbanisland.domain.chat.QChatRoom;
import rooftopgreenlight.urbanisland.repository.chat.ChatRoomRepository;
import rooftopgreenlight.urbanisland.dto.chat.ChatRoomDto;
import rooftopgreenlight.urbanisland.exception.NoMatchMemberIdException;
import rooftopgreenlight.urbanisland.exception.NotFoundChatRoomException;
import rooftopgreenlight.urbanisland.domain.rooftop.Rooftop;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final RooftopService rooftopService;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 채팅방 생성 여부 확인
     */
    @Transactional
    public Long joinChatRoom(Long id, Long ownerId, Long memberId, String type) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoom(id, ownerId, memberId).orElse(null);
        ChatRoomType roomType = type.equals("INQUIRY") ? ChatRoomType.INQUIRY : ChatRoomType.RESERVATION;
        if(chatRoom == null) {
            ChatRoom newChatRoom = ChatRoom.createChatRoom()
                    .rid(id)
                    .memberId(memberId)
                    .ownerId(ownerId)
                    .roomType(roomType)
                    .build();
            chatRoomRepository.save(newChatRoom);
            return newChatRoom.getId();
        }
        return chatRoom.getId();
    }

    /**
     * 사용자 - 문의 응답 확인하기 (목록형)
     */
    public List<ChatRoomDto> getRoomList(Long memberId) {
        List<ChatRoom> chatRooms = StreamSupport.stream(
                        chatRoomRepository.findAll(QChatRoom.chatRoom.memberId.eq(memberId)).spliterator(), false)
                .collect(Collectors.toList());
        return chatRooms.stream().map(chatRoom -> ChatRoomDto.of(chatRoom.getId(), chatRoom.getRid())).collect(Collectors.toList());
    }

    /**
     * 채팅방 조회
     */
    public ChatRoom getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> {
            throw new NotFoundChatRoomException("문의 내역을 찾을 수 없습니다.");
        });
    }

    public void deleteByRoomId(Long roomId) {
        chatRoomRepository.deleteById(roomId);
    }

    /**
     * 옥상지기 - 문의 내역 확인하기
     */
    public List<ChatRoomDto> getRoomListByOwner(Long rooftopId, Long ownerId) {
        Rooftop rooftop = rooftopService.findByRooftopId(rooftopId);
        if(!rooftop.getMember().getId().equals(ownerId)) {
            throw new NoMatchMemberIdException("권한이 없습니다.");
        }
        List<ChatRoom> chatRoomList = chatRoomRepository.findChatRoomByOwnerId(rooftopId, ownerId);
        return chatRoomList.stream().map(chatRoom -> ChatRoomDto.of(chatRoom.getId(), chatRoom.getRid())).collect(Collectors.toList());
    }

    /**
     * 채팅방 번호를 통해 옥상 주소 조회
     */
    public RooftopDto getRooftopAddressByRoomId(Long roomId) {
        ChatRoom chatRoom = getChatRoomById(roomId);
        Rooftop rooftop = rooftopService.findByRooftopId(chatRoom.getRid());
        return RooftopDto.getRooftopAddress(rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail());
    }
}
