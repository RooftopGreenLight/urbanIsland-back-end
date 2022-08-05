package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChatRoomResponse {
    private Long roomId;
    private Long memberId;
    private String content;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime sendTime;

    protected ChatRoomResponse(Long memberId, String content, LocalDateTime sendTime) {
        this.memberId = memberId;
        this.content = content;
        this.sendTime = sendTime;
    }

    protected ChatRoomResponse(Long roomId, Long memberId, String content, LocalDateTime sendTime) {
        this.roomId = roomId;
        this.memberId = memberId;
        this.content = content;
        this.sendTime = sendTime;
    }

    public static ChatRoomResponse of(Long memberId, String content, LocalDateTime sendTime) {
        return new ChatRoomResponse(memberId, content, sendTime);
    }

    public static ChatRoomResponse of(Long roomId, Long memberId, String content, LocalDateTime sendTime) {
        return new ChatRoomResponse(roomId, memberId, content, sendTime);
    }

}
