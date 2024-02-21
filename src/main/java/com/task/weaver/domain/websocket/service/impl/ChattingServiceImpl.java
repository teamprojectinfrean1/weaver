package com.task.weaver.domain.websocket.service.impl;

import com.task.weaver.common.exception.chatting.ChattingRoomNotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.domain.chattingRoomMember.ChattingRoomMember;
import com.task.weaver.domain.chattingRoomMember.ChattingRoomMemberRepository;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.projectmember.ProjectMember;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.websocket.dto.request.RequestCreateChatting;
import com.task.weaver.domain.websocket.dto.response.ResponseGetChattings;
import com.task.weaver.domain.websocket.dto.response.ResponseCreateChattingRoom;
import com.task.weaver.domain.websocket.entity.Chatting;
import com.task.weaver.domain.websocket.entity.ChattingRoom;
import com.task.weaver.domain.websocket.repository.ChattingRepository;
import com.task.weaver.domain.websocket.repository.ChattingRoomRepository;
import com.task.weaver.domain.websocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingServiceImpl implements ChattingService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ChattingRepository chattingRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingRoomMemberRepository chattingRoomMemberRepository;

    @Override
    public List<ResponseGetChattings> getChattings(Long chattingRoom_id) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoom_id)
                .orElseThrow(() -> new ChattingRoomNotFoundException(new Throwable(String.valueOf(chattingRoom_id))));

        List<ResponseGetChattings> responseGetChattingsList = new ArrayList<>();

        for(Chatting chatting : chattingRoom.getChattings()){
            ResponseGetChattings responseGetChattings = new ResponseGetChattings(chatting);
            responseGetChattingsList.add(responseGetChattings);
        }

        return responseGetChattingsList;
    }

    @Override
    public void deleteChatting(Long chatting_id) {
        chattingRepository.deleteById(chatting_id);
    }

    @Override
    @Transactional
    public void save(Long chattingId, RequestCreateChatting requestCreateChatting) {
        Long user_id = requestCreateChatting.getSenderId();

        User sender = userRepository.findById(user_id)
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(user_id))));
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingId)
                .orElseThrow(() -> new ChattingRoomNotFoundException(new Throwable(String.valueOf(chattingId))));

        Chatting chatting = Chatting.builder()
                .user(sender)
                .chattingRoom(chattingRoom)
                .content(requestCreateChatting.getContent())
                .build();
        chattingRoom.createChatting(chatting);
//        chattingRoomRepository.save(chattingRoom);  //이미 영속화된 chattingroom을 위의 createChatting으로 변경했기에 동작할 필요 없는 부분.
    }

    @Override
    public ResponseCreateChattingRoom createChattingRoom(Long project_id) {
        Project project = projectRepository.findById(project_id)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(project_id))));

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .name(project.getName() + " chattingRoom")
                .build();

        for(ProjectMember projectMember : project.getProjectMember()){  //프로젝트에 참여된 member들 가져옴
            ChattingRoomMember chattingRoomMember = ChattingRoomMember.builder()     //다대다 관계 chattingRoomMember를 만든다.
                    .chattingRoom(chattingRoom)
                    .user(projectMember.getUser())
                    .build();

            chattingRoomMemberRepository.save(chattingRoomMember);

            chattingRoom.getChattingRoomMemberList().add(chattingRoomMember);
        }

        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);
        return new ResponseCreateChattingRoom(savedChattingRoom);
    }
}
