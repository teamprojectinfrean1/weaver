-- 더미 사용자 데이터 삽입
INSERT INTO `USER` (user_id, id, nickname, is_online, email, `password`, profile_image, main_project_id, regdate, moddate)
VALUES
    (UNHEX(REPLACE(UUID(), '-', '')), 'user1', 'John Doe', true, 'jcjk0302@gmail.com', 'password123', 'profile1.jpg', NULL, NOW(), NOW()),
    (UNHEX(REPLACE(UUID(), '-', '')), 'user2', 'Jane Doe', false, 'jane@example.com', 'password456', 'profile2.jpg', NULL, NOW(), NOW()),
    (UNHEX(REPLACE(UUID(), '-', '')), 'user3', 'Alice', true, 'alice@example.com', 'password789', NULL, NULL, NOW(), NOW());

-- -- 더미 프로젝트 데이터 삽입
-- INSERT INTO PROJECT (project_id, name, detail, start_date, end_date, create_date, user_id, last_update_user_id, regdate, moddate)
-- VALUES
--     (UNHEX(REPLACE(UUID(), '-', '')), 'Project A', 'Details of Project A', NOW(), NOW(), NOW(), (SELECT user_id FROM `USER` WHERE id = 'user1' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user1' LIMIT 1), NOW(), NOW()),
--     (UNHEX(REPLACE(UUID(), '-', '')), 'Project B', 'Details of Project B', NOW(), NOW(), NOW(), (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), NOW(), NOW());
--
-- -- 더미 프로젝트 멤버 데이터 삽입
-- INSERT INTO PROJECT_MEMBER (project_id, user_id, regdate, moddate)
-- VALUES
--     ((SELECT project_id FROM PROJECT WHERE name = 'Project A' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user1' LIMIT 1), NOW(), NOW()),
--     ((SELECT project_id FROM PROJECT WHERE name = 'Project A' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), NOW(), NOW()),
--     ((SELECT project_id FROM PROJECT WHERE name = 'Project B' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), NOW(), NOW()),
--     ((SELECT project_id FROM PROJECT WHERE name = 'Project B' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user3' LIMIT 1), NOW(), NOW());
--
-- -- 더미 채팅방 데이터 삽입
-- INSERT INTO CHATTING_ROOM (chatting_room_id, name, project_id, regdate, moddate)
-- VALUES
--     (NULL, 'General Chat', (SELECT project_id FROM PROJECT WHERE name = 'Project A' LIMIT 1), NOW(), NOW()),
--     (NULL, 'Random Chat', (SELECT project_id FROM PROJECT WHERE name = 'Project B' LIMIT 1), NOW(), NOW());
--
-- -- 더미 채팅 데이터 삽입
-- INSERT INTO CHATTING (chatting_id, content, user_id, chatting_room_id, regdate, moddate)
-- VALUES
--     (NULL, 'Hello, everyone!', (SELECT user_id FROM `USER` WHERE id = 'user1' LIMIT 1), (SELECT chatting_room_id FROM CHATTING_ROOM WHERE name = 'General Chat' LIMIT 1), NOW(), NOW()),
--     (NULL, 'How is everyone doing?', (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), (SELECT chatting_room_id FROM CHATTING_ROOM WHERE name = 'General Chat' LIMIT 1), NOW(), NOW()),
--     (NULL, 'This project looks interesting.', (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), (SELECT chatting_room_id FROM CHATTING_ROOM WHERE name = 'Random Chat' LIMIT 1), NOW(), NOW());
--
-- -- 더미 상태 데이터 삽입
-- INSERT INTO STATUS (content, regdate, moddate)
-- VALUES
--     ('TODO', NOW(), NOW()),
--     ('INPROGRESS', NOW(), NOW()),
--     ('DONE', NOW(), NOW());
--
-- -- 더미 태스크 데이터 삽입
-- INSERT INTO TASK (task_id, project_id, status_id, creator_user_id, last_update_user_id, task_title, task_content, start_date, end_date, edit_delete_permission, regdate, moddate)
-- VALUES
--     (UNHEX(REPLACE(UUID(), '-', '')), (SELECT project_id FROM PROJECT WHERE name = 'Project A' LIMIT 1), (SELECT status_id FROM TASK_STATUS WHERE tag = 'TODO' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user1' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user1' LIMIT 1), 'Task 1', 'Details of Task 1', NOW(), NOW(), 'allowed', NOW(), NOW()),
--     (UNHEX(REPLACE(UUID(), '-', '')), (SELECT project_id FROM PROJECT WHERE name = 'Project B' LIMIT 1), (SELECT status_id FROM TASK_STATUS WHERE tag = 'INPROGRESS' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), 'Task 2', 'Details of Task 2', NOW(), NOW(), 'allowed', NOW(), NOW());
--
-- -- 더미 태스크 권한 데이터 삽입
-- INSERT INTO TASK_AUTHORITY (task_authority_id, project_id, user_id, task_id, code, regdate, moddate)
-- VALUES
--     (NULL, (SELECT project_id FROM PROJECT WHERE name = 'Project A' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user1' LIMIT 1), (SELECT task_id FROM TASK WHERE task_title = 'Task 1' LIMIT 1), 'admin', NOW(), NOW()),
--     (NULL, (SELECT project_id FROM PROJECT WHERE name = 'Project B' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), (SELECT task_id FROM TASK WHERE task_title = 'Task 2' LIMIT 1), 'member', NOW(), NOW());
--
-- -- 더미 태스크 상태 데이터 삽입
-- INSERT INTO TASK_STATUS (task_tag_id, task_id, status_id, tag, regdate, moddate)
-- VALUES
--     (NULL, (SELECT task_id FROM TASK WHERE task_title = 'Task 1' LIMIT 1), (SELECT status_id FROM STATUS WHERE content = 'TODO' LIMIT 1), 'TODO', NOW(), NOW()),
--     (NULL, (SELECT task_id FROM TASK WHERE task_title = 'Task 2' LIMIT 1), (SELECT status_id FROM STATUS WHERE content = 'INPROGRESS' LIMIT 1), 'INPROGRESS', NOW(), NOW());
--
-- -- 더미 이슈 데이터 삽입
-- INSERT INTO `ISSUE` (issue_id, task_id, creator_id, manager_id, title, content, end_date, start_date, status, regdate, moddate)
-- VALUES
--     (UNHEX(REPLACE(UUID(), '-', '')), (SELECT task_id FROM TASK WHERE task_title = 'Task 1' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user1' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), 'Issue 1', 'Details of Issue 1', NOW(), NOW(), 'TODO', NOW(), NOW()),
--     (UNHEX(REPLACE(UUID(), '-', '')), (SELECT task_id FROM TASK WHERE task_title = 'Task 2' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), (SELECT user_id FROM `USER` WHERE id = 'user3' LIMIT 1), 'Issue 2', 'Details of Issue 2', NOW(), NOW(), 'INPROGRESS', NOW(), NOW());
--
-- -- 더미 코멘트 데이터 삽입
-- INSERT INTO `COMMENT` (comment_id, user_id, issue_id, body, regdate, moddate)
-- VALUES
--     (NULL, (SELECT user_id FROM `USER` WHERE id = 'user1' LIMIT 1), (SELECT issue_id FROM `ISSUE` WHERE title = 'Issue 1' LIMIT 1), 'Comment 1 on Issue 1', NOW(), NOW()),
--     (NULL, (SELECT user_id FROM `USER` WHERE id = 'user2' LIMIT 1), (SELECT issue_id FROM `ISSUE` WHERE title = 'Issue 2' LIMIT 1), 'Comment 1 on Issue 2', NOW(), NOW());
