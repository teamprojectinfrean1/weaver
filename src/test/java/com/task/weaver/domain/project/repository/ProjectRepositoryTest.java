//package com.task.weaver.domain.project.repository;
//
//
//import com.task.weaver.domain.project.entity.Project;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.stream.IntStream;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class ProjectRepositoryTest {
//
//    @Autowired
//    private ProjectRepository projectRepository;
//
//    @Test
//    public void 데이터_삽입_테스트() {
//        IntStream.rangeClosed(1, 100).forEach( i -> {
//            LocalDateTime now = LocalDateTime.now();
//            LocalDateTime startDate = now.plusWeeks(1);
//
//            Project project = Project.builder()
//                    .customUrl("Custom URL..." + i)
//                    .bannerUrl("Banner URL..." + i)
//                    .detail("Detail..." + i)
//                    .created(now)
//                    .startDate(now)
//                    .startDate(startDate)
//                    .name("Project Name..." + i)
//                    .isPublic(true)
//                    .build();
//            System.out.println(projectRepository.save(project));
//        });
//    }
//
//    @Test
//    public void 데이터_수정_테스트() {
//        Optional<Project> result = projectRepository.findById(100L);
//
//        if (result.isPresent()) {
//            Project project = result.get();
//            project.changeName("Change Name");
//            project.changePublic();
//            project.changeDetail("Change Detail");
//
//            System.out.println(projectRepository.save(project));
//        }
//    }
//
//    @Test
//    public void 데이터_삭제_테스트() {
//        Optional<Project> result = projectRepository.findById(100L);
//
//        if (result.isPresent()) {
//            Project project = result.get();
//            projectRepository.delete(project);
//        }
//    }
//}