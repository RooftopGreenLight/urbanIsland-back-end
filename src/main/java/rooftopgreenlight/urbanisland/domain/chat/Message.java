package rooftopgreenlight.urbanisland.domain.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;
    private String content;
    private LocalDateTime sendTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Builder(builderMethodName = "createMessage")
    public Message(String content, LocalDateTime sendTime) {
        this.content = content;
        this.sendTime = sendTime;
    }

    public void updateMember(Member member) {
        this.member = member;
    }
    public void updateChatRoom(ChatRoom chatRoom) {this.chatRoom = chatRoom;}
}
