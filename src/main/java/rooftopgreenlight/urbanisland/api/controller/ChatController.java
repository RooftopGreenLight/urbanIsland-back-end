package rooftopgreenlight.urbanisland.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.api.controller.dto.ChatRequest;
import rooftopgreenlight.urbanisland.api.controller.dto.ChatRoomResponse;
import rooftopgreenlight.urbanisland.domain.chat.entity.ChatRoom;
import rooftopgreenlight.urbanisland.domain.chat.entity.Message;
import rooftopgreenlight.urbanisland.domain.chat.service.ChatRoomService;
import rooftopgreenlight.urbanisland.domain.chat.service.MessageService;

import java.util.ArrayList;
import java.util.List;

/**
 * @MessageMapping : 발행 경로
 * @SendToUser : 1:1로 메시지로 보낼 때 사용, 경로는 /queue로 시작
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    /**
     * 문의 응답 확인하기
     * = 사용자 입장에서 본 옥상지기-사용자 채팅방 목록
     *
     * 요청 데이터) 사용자 ID
     * 응답 데이터) room_id에 따른 <가장 마지막으로 대답한> 사용자의 시간, 응답 내용 리스트
     * 의문인 점
     * - 기획서에서는 문의 응답 (옥상지기가 마지막으로 대답했을 때)과 문의 내용 (사용자가 마지막으로 대답했을 때)를 굳이 나눴는데
     * 그냥 그럴 필요 없이 어떤 것이든 대답 온 거 누르면 채팅 내역이 뜨는 게 더 낫지 않나 싶다...
     */
    @GetMapping("/inquiry/response")
    public APIResponse getInquiryResponse(@RequestParam Long memberId) {
        List<ChatRoom> roomList = chatRoomService.getRoomList(memberId);
        List<ChatRoomResponse> roomResList = new ArrayList<>();

        roomList.stream().forEach(chatRoom -> {
            Message findMessage = messageService.getFirstMsgJoinFetchMember(memberId, chatRoom.getId());
            if(findMessage!=null) {
                // 생각해봤는데 이러면 n+1 나오지 않나...? ㅎㅎ
                roomResList.add(ChatRoomResponse.of(findMessage.getMember().getId(),
                        findMessage.getContent(), findMessage.getSendTime()));
            }
        });
        return APIResponse.of(roomResList);
    }

    /**
     * 문의 응답 확인하기 -> 세부 정보 클릭
     * = 즉, 1:1 채팅방 내용
     *
     * 요청 데이터) roomId
     * 응답 데이터) List<ChatRoomResponse>
     * 내부 -> memberId, content, date
     *
     */
    @GetMapping("/inquiry/room/{roomId}")
    public APIResponse getInquiryRoom(@PathVariable(value = "roomId") Long roomId,
                                      @RequestParam Long memberId) {
        List<ChatRoomResponse> roomResList = new ArrayList<>();
        List<Message> messages = messageService.getMessageByRoomId(memberId, roomId);
        messages.stream().forEach(message -> {
            roomResList.add(ChatRoomResponse.of(message.getMember().getId(),
                    message.getContent(), message.getSendTime()));
        });
        return APIResponse.of(roomResList);
    }


    /**
     * 옥상지기에게 문의하기
     * - '전송하기' 버튼을 사용자가 눌렀을 때, 옥상과 사용자 사이 이미 채팅방이 존재하는지 여부 확인
     * 1) 존재하지 않는다면 채팅방 생성 + 해당 채팅방에 메시지 보내기
     * 2) 존재한다면 해당 채팅방에 메시지 보내기
     *
     * 없으면 채팅방 id 새로 생성, 있으면 기존에 존재하는 거 리턴해주기
     *
     * 생각해보니까 옥상지기 정보가 아니라, 옥상 id가 필요할 것 같네...? (옥상별로 생기는 거니까...)
     * 요청 데이터) 옥상 id, 사용자 id
     * 응답 데이터) roomId
     */
    @GetMapping("/inquiry/room/{rooftopId}/{memberId}")
    public APIResponse joinChatRoom(@PathVariable(value = "rooftopId") Long rooftopId,
                                    @PathVariable(value = "memberId") Long memberId) {
        return APIResponse.of(chatRoomService.joinChatRoom(rooftopId, memberId));
    }

    /**
     * 실제로 채팅방에 메시지 보내기
     * - 클라이언트가 소켓 연결 후, /queue/roomId를 구독하면 될 듯...?
     *
     *  client 소켓에서 sendMessage 함수로 메시지를 보내서 @MessageMapping으로 받는 느낌?
     */
    @MessageMapping("/inquiry/room")
    public APIResponse chat(@RequestBody ChatRequest chatRequest) {
        messageService.sendMessage(chatRequest.getRoomId(), chatRequest.getMemberId(), chatRequest.getContent());
        messagingTemplate.convertAndSend("/queue/" + chatRequest.getRoomId(), chatRequest.getContent());
        return APIResponse.of("채팅 전송 성공!");
    }

}
