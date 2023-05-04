package rooftopgreenlight.urbanisland.dto.chat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageResponse {
    private Long memberId;
    private String content;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime sendTime;

    public static MessageResponse of(Long memberId, String content, LocalDateTime sendTime) {
        return new MessageResponse(memberId, content, sendTime);
    }
}
