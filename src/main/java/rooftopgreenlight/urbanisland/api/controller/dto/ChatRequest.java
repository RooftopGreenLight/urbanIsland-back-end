package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatRequest {
    private Long roomId;
    private Long memberId;
    private String content;
}
