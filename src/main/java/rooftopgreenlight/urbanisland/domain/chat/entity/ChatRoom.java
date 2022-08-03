package rooftopgreenlight.urbanisland.domain.chat.entity;

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

    private Long rooftopId;
    private Long memberId;

    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages = new ArrayList<>();

    @Builder(builderMethodName = "createChatRoom")
    public ChatRoom(Long rooftopId, Long memberId) {
        this.rooftopId = rooftopId;
        this.memberId = memberId;
    }
}
