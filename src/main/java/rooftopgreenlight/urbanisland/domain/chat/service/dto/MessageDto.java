package rooftopgreenlight.urbanisland.domain.chat.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageDto {
    private Long id;
    private String content;
    private LocalDateTime sendTime;
    private Long memberId;

    public static MessageDto of(Long id, String content, LocalDateTime sendTime, Long memberId) {
        return new MessageDto(id, content, sendTime, memberId);
    }
}
