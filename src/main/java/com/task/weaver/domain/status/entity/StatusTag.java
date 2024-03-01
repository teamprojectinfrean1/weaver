package com.task.weaver.domain.status.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "STATUS_TAG")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class StatusTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "status_tag_id", length = 100)
	private Long statusTagId;

	@Column(name = "content", length = 8)
	private String content;
}