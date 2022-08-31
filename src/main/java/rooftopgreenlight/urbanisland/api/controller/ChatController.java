package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.ChatRequest;
import rooftopgreenlight.urbanisland.api.controller.dto.ChatRoomResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.MessageResponse;
import rooftopgreenlight.urbanisland.domain.chat.service.ChatRoomService;
import rooftopgreenlight.urbanisland.domain.chat.service.MessageService;
import rooftopgreenlight.urbanisland.domain.chat.service.dto.ChatRoomDto;
import rooftopgreenlight.urbanisland.domain.chat.service.dto.MessageDto;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    /**
     * 문의 응답 확인하기
     * @return List<ChatRoomResponse>
     */
    @GetMapping("/inquiry/response")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "문의 응답 확인하기 (목록)",
            notes = "정상 동작 시 채팅방 목록 전달, 내부 요소: roomId, rooftopId, memberId, content, sendTime")
    public APIResponse getInquiryResponse(@PK Long memberId) {
        List<ChatRoomDto> roomList = chatRoomService.getRoomList(memberId);
        return APIResponse.of(getResponseWithMessage(roomList));
    }

    /**
     * 문의 응답 확인하기 (세부 정보)
     */
    @GetMapping("/inquiry/room/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "문의 응답 확인하기 (세부 정보) + 옥상지기의 문의 알림 역시 동일",
            notes = "정상 동작 시 각 채팅방의 세부 정보 전달, 요소: roomId, city, district, detail, memberId, content, sendTime")
    public APIResponse getInquiryRoom(@PathVariable(value = "roomId") Long roomId,
                                      @PK Long memberId) {
        List<MessageDto> messages = messageService.getMessageByRoomId(memberId, roomId);
        RooftopDto rooftopDto = chatRoomService.getRooftopAddressByRoomId(roomId);
        List<MessageResponse> messageResponses = messages.stream().map(message -> MessageResponse.of(message.getMemberId(), message.getContent(),
                message.getSendTime())).collect(Collectors.toList());

        return APIResponse.of(ChatRoomResponse.of(rooftopDto.getCity(), rooftopDto.getDistrict(),
                rooftopDto.getDetail(), messageResponses));
    }

    /**
     * 문의 응답 확인하기 (세부정보) - 문의 삭제
     */
    @DeleteMapping("/inquery/room/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "문의 응답 확인하기 - 문의 삭제")
    public APIResponse deleteInquiryRoom(@PathVariable(value = "roomId") Long roomId,
                                         @PK Long memberId) {
        messageService.deleteMessageByRoomId(memberId, roomId);
        return APIResponse.empty();
    }


    /**
     * 문의하기
     */
    @GetMapping("/inquiry/join/room/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "문의하기 버튼 클릭 시 동작",
            notes = "요청 데이터(path) - key : rooftopid" +
                    "(param) - key : ownerId" +
                    "정상 동작 시 roomId 리턴")
    public APIResponse joinChatRoom(@PathVariable(value = "rooftopId") Long rooftopId,
                                    @RequestParam(value = "ownerId") Long ownerId,
                                    @PK Long memberId) {
        return APIResponse.of(chatRoomService.joinChatRoom(rooftopId, ownerId, memberId));
    }

    /**
     * 문의 알림란 (옥상지기)
     */
    @GetMapping("/inquiry/owner/response/{rooftopId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "(11) 옥상에 대한 문의알림란 (옥상지기 전용)",
                notes = "요청 데이터(path) - key : rooftopId")
    public APIResponse getInquiryOwnerResponse(@PathVariable(value = "rooftopId") Long rooftopId,
                                               @PK Long memberId) {
        List<ChatRoomDto> roomList = chatRoomService.getRoomListByOwner(rooftopId, memberId);
        return APIResponse.of(getResponseWithMessage(roomList));
    }


    private List<ChatRoomResponse> getResponseWithMessage(List<ChatRoomDto> roomList) {
        return roomList.stream().map(chatRoom -> {
            MessageDto findMessage = messageService.getLastMessage(chatRoom.getId());
            if (findMessage != null) {
                return ChatRoomResponse.of(chatRoom.getId(), chatRoom.getRooftopId(),
                        findMessage.getMemberId(), findMessage.getContent(), findMessage.getSendTime());
            }
            return null;
        }).collect(Collectors.toList());
    }

    /**
     * 메시지 전송
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
