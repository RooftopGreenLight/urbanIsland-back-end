package rooftopgreenlight.urbanisland.domain.chat.service.dto;

import lombok.Data;

@Data
public class ChatRoomDto {
    private Long id;
    private Long rooftopId;
    private Long memberId;
    private Long ownerId;


    protected ChatRoomDto(Long id, Long rooftopId) {
        this.id = id;
        this.rooftopId = rooftopId;
    }

    public static ChatRoomDto of(Long id, Long rooftopId) {
        return new ChatRoomDto(id, rooftopId);
    }
}
