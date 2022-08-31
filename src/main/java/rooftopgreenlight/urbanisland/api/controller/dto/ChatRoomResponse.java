package rooftopgreenlight.urbanisland.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChatRoomResponse {
    private Long roomId;
    private Long memberId;
    private Long rooftopId;
    private String content;

    private String city;
    private String district;
    private String detail;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime sendTime;
    List<MessageResponse> messageResponses;

    protected ChatRoomResponse(String city, String district, String detail, List<MessageResponse> messageResponses) {
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.messageResponses = messageResponses;
    }

    protected ChatRoomResponse(Long roomId, Long rooftopId, Long memberId, String content, LocalDateTime sendTime) {
        this.roomId = roomId;
        this.rooftopId = rooftopId;
        this.memberId = memberId;
        this.content = content;
        this.sendTime = sendTime;
    }

    public static ChatRoomResponse of(String city, String district, String detail, List<MessageResponse> messageResponses) {
        return new ChatRoomResponse(city, district, detail, messageResponses);
    }

    public static ChatRoomResponse of(Long roomId, Long rooftopId, Long memberId, String content, LocalDateTime sendTime) {
        return new ChatRoomResponse(roomId, rooftopId, memberId, content, sendTime);
    }

}
