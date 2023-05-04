package rooftopgreenlight.urbanisland.domain.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "chat_room")
public class ChatRoom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private Long rid; // INQUIRY -> rooftopId, RESERVATION -> reservationId

    private Long memberId;
    private Long ownerId;

    @Embedded
    private ChatRoomType roomType;

    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages = new ArrayList<>();

    @Builder(builderMethodName = "createChatRoom")
    public ChatRoom(Long rid, Long memberId, Long ownerId, ChatRoomType roomType) {
        this.rid = rid;
        this.memberId = memberId;
        this.ownerId = ownerId;
        this.roomType = roomType;
    }
}
