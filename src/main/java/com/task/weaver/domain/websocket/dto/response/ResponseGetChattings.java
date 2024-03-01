package com.task.weaver.domain.websocket.dto.response;

import com.task.weaver.domain.websocket.entity.Chatting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseGetChattings {
    private Long chatting_id;
    private String writer_name;
    private String chat_text;
    private LocalDateTime write_time;

    public ResponseGetChattings(Chatting chatting){
        this.chatting_id = chatting.getId();
        this.writer_name = chatting.getUser().getNickname();
        this.chat_text = chatting.getContent();
        this.write_time = chatting.getRegDate();
    }
}
