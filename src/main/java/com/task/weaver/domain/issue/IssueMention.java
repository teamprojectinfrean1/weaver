package com.task.weaver.domain.issue;

import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "ISSUE_MENTION")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class IssueMention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_mention_id")
    private Long issueMentionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentioned_user_id")
    private User user;

    @Column(name ="is_check", columnDefinition = "TINYINT(1)")
    private boolean isCheck;
}
