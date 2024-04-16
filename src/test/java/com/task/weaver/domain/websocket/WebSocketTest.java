package com.task.weaver.domain.websocket;
import com.task.weaver.domain.member.user.service.UserService;
import com.task.weaver.domain.websocket.service.ChattingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class WebSocketTest {
    private final UserService userService;
    private final ChattingService chattingService;

    @Autowired
    public WebSocketTest(UserService userService, ChattingService chattingService) {
        this.userService = userService;
        this.chattingService = chattingService;
    }

//    @Test
//    public void test() {
//        ResponseCreateChattingRoom chattingRoom = chattingService.createChattingRoom(1L);
//        System.out.println(chattingRoom.getChattingRoomId());
//    }
}
