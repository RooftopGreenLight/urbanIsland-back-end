package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.ChatRequest;
import rooftopgreenlight.urbanisland.api.controller.dto.ChatRoomResponse;
import rooftopgreenlight.urbanisland.domain.chat.entity.ChatRoom;
import rooftopgreenlight.urbanisland.domain.chat.entity.Message;
import rooftopgreenlight.urbanisland.domain.chat.service.ChatRoomService;
import rooftopgreenlight.urbanisland.domain.chat.service.MessageService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    /**
     * 문의 응답 확인하기
     * @param memberId
     * @return List<ChatRoomResponse>
     */
    @GetMapping("/inquiry/response")
    @ApiOperation(value = "문의 응답 확인하기 (목록)",
            notes = "정상 동작 시 채팅방 목록 전달, 내부 요소: roomId, memberId, content, sendTime")
    public APIResponse getInquiryResponse(@PK Long memberId) {
        List<ChatRoom> roomList = chatRoomService.getRoomList(memberId);
        List<ChatRoomResponse> roomResList = new ArrayList<>();

        roomList.stream().forEach(chatRoom -> {
            Message findMessage = messageService.getFirstMsgJoinFetchMember(memberId, chatRoom.getId());
            if(findMessage!=null) {
                roomResList.add(ChatRoomResponse.of(chatRoom.getId(), findMessage.getMember().getId(),
                        findMessage.getContent(), findMessage.getSendTime()));
            }
        });
        return APIResponse.of(roomResList);
    }

    /**
     * 문의 응답 확인하기 (세부 정보)
     * @param roomId
     * @param memberId
     * @return List<ChatRoomResponse>
     */
    @GetMapping("/inquiry/room/{roomId}")
    @ApiOperation(value = "문의 응답 확인하기 (세부 정보)",
            notes = "정상 동작 시 각 채팅방의 세부 정보 전달, 요소: roomId, memberId, content")
    public APIResponse getInquiryRoom(@PathVariable(value = "roomId") Long roomId,
                                      @PK Long memberId) {
        List<ChatRoomResponse> roomResList = new ArrayList<>();
        List<Message> messages = messageService.getMessageByRoomId(memberId, roomId);
        messages.stream().forEach(message -> {
            roomResList.add(ChatRoomResponse.of(message.getMember().getId(),
                    message.getContent(), message.getSendTime()));
        });
        return APIResponse.of(roomResList);
    }

    /**
     * 문의하기
     * @param rooftopId
     * @param memberId
     * @return roomId
     */
    @GetMapping("/inquiry/join/room/{rooftopId}")
    @ApiOperation(value = "문의하기 버튼 클릭 시 동작",
            notes = "정상 동작 시 roomId 리턴")
    public APIResponse joinChatRoom(@PathVariable(value = "rooftopId") Long rooftopId,
                                    @PK Long memberId) {
        return APIResponse.of(chatRoomService.joinChatRoom(rooftopId, memberId));
    }

    /**
     * 메시지 전송
     * @param chatRequest
     * @return null
     */
    @MessageMapping("/inquiry/room")
    @ApiOperation(value = "메시지 전송", notes = "정상 동작 시 memberId, roomId, content, sendTime 리턴")
    public APIResponse chat(ChatRequest chatRequest) {
        LocalDateTime sendTime = messageService.sendMessage(chatRequest.getRoomId(), chatRequest.getMemberId(), chatRequest.getContent());
        messagingTemplate.convertAndSend("/queue/" + chatRequest.getRoomId(),
                ChatRequest.of(chatRequest.getMemberId(), chatRequest.getRoomId(), chatRequest.getContent(), sendTime));
        return APIResponse.empty();
    }
}
