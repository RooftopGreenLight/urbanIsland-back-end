package rooftopgreenlight.urbanisland.dto.chat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatRequest {
    private Long memberId;
    private Long roomId;
    private String content;

    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime sendTime;

    protected ChatRequest(Long memberId, Long roomId, String content, LocalDateTime sendTime) {
        this.memberId = memberId;
        this.roomId = roomId;
        this.content = content;
        this.sendTime = sendTime;
    }

    public static ChatRequest of(Long memberId, Long roomId, String content, LocalDateTime sendTime) {
        return new ChatRequest(memberId, roomId, content, sendTime);
    }
}
