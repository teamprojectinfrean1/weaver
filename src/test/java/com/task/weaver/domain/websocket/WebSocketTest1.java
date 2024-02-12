package com.task.weaver.domain.websocket;

import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.response.ResponseUser;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.service.UserService;
import com.task.weaver.domain.websocket.chat.ChatMessage;
import com.task.weaver.domain.websocket.chat.service.ChatMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class WebSocketTest1 {
    UserService userService;
    ChatMessageService chatMessageService;
    @Test
    public void testUserWithChat() {

        RequestCreateUser user1= RequestCreateUser.builder()
                .nickname("testUser")
                .mail("830ji@naver.com")
                .password("1234567890")
                .build();

        RequestCreateUser user2= RequestCreateUser.builder()
                .nickname("testUse2")
                .mail("830ji2@naver.com")
                .password("1234567890")
                .build();

        userService.addUser(user1);
        userService.addUser(user2);

        ResponseUser user11 = userService.getUser("testUser");
        ResponseUser user22 = userService.getUser("testUser2");

        String senderId = user11.getUser_id().toString();
        String recipientId = user22.getUser_id().toString();

        List<ChatMessage> messages = chatMessageService.findChatMessages(senderId, recipientId);
        for (ChatMessage message : messages) {
            System.out.println(message);
        }
    }
}

/**
 * @GetMapping("/messages/{senderId}/{recipientId}")
 *     public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
 *                                                               @PathVariable String recipientId) {
 *         return ResponseEntity
 *                 .ok(chatMessageService.findChatMessages(senderId, recipientId));
 *     }
 */
