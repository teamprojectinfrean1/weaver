package com.task.weaver.domain.websocket.service.impl;

import com.task.weaver.common.exception.chatting.ChattingRoomNotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.websocket.dto.ChattingRequest;
import com.task.weaver.domain.websocket.dto.ChattingResponse;
import com.task.weaver.domain.websocket.dto.ChattingRoomResponse;
import com.task.weaver.domain.websocket.entity.Chatting;
import com.task.weaver.domain.websocket.entity.ChattingRoom;
import com.task.weaver.domain.websocket.repository.ChattingRoomRepository;
import com.task.weaver.domain.websocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingServiceImpl implements ChattingService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    @Override
    public List<ChattingResponse> getChattings(Long chattingRoom_id) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoom_id)
                .orElseThrow(() -> new ChattingRoomNotFoundException(new Throwable(String.valueOf(chattingRoom_id))));

        List<ChattingResponse> chattingResponseList = new ArrayList<>();

        for(Chatting chatting : chattingRoom.getChattings()){
            ChattingResponse chattingResponse = new ChattingResponse(chatting);
            chattingResponseList.add(chattingResponse);
        }

        return chattingResponseList;
    }

    @Override
    @Transactional
    public void save(Long chattingId, ChattingRequest chattingRequest) {
        Long user_id = chattingRequest.getSenderId();

        User sender = userRepository.findById(user_id)
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(user_id))));
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingId)
                .orElseThrow(() -> new ChattingRoomNotFoundException(new Throwable(String.valueOf(chattingId))));

        Chatting chatting = Chatting.builder()
                .user(sender)
                .chattingRoom(chattingRoom)
                .content(chattingRequest.getContent())
                .build();
        chattingRoom.createChatting(chatting);
//        chattingRoomRepository.save(chattingRoom);  //이미 영속화된 chattingroom을 위의 createChatting으로 변경했기에 동작할 필요 없는 부분.
    }

    @Override
    public ChattingRoomResponse createChattingRoom(Long project_id) {
        Project project = projectRepository.findById(project_id)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(project_id))));

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .name(project.getName() + " chattingRoom")
                .build();

        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);
        return ChattingRoomResponse.builder()
                .chattingRoomId(savedChattingRoom.getId())
                .build();
    }
}
