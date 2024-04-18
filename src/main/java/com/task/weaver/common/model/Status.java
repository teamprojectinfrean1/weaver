package com.task.weaver.common.model;

import java.util.Locale;

public enum Status {
	TODO, INPROGRESS, DONE;

	public static Status fromName(String type) {
		return Status.valueOf(type.toUpperCase(Locale.ENGLISH));
	}
}
