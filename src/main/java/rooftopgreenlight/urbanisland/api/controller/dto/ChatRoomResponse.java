package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(staticName = "of")
public class ChatRoomResponse {
    private Long memberId;
    private String content;
    private LocalDateTime sendTime;
}
