package com.task.weaver.domain.status.entity;

import com.task.weaver.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "STATUS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Status extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "status_id")
	private Long statusId;

	@Column(name = "content", length = 20)
	private String content;
}