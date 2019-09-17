package com.poc.requestapproval.domain;

import java.util.Arrays;

public enum UserAuthorityType {
	ROLE_ADMIN("ROLE_ADMIN"),
	REQUESTER("0_REQUESTER"),
	FIRST_APPROVER("1_APPROVER"),
	SECOND_APPROVER("2_APPROVER"),
	THIRD_APPROVER("3_APPROVER");

	private String value;

	UserAuthorityType(String value) {
		this.value = value;
	}

	public static UserAuthorityType fromValue(String value) {
		for (UserAuthorityType authorityType : values()) {
			if (authorityType.value.equals(value)) {
				return authorityType;
			}
		}

		throw new IllegalArgumentException(
				"Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
	}

	@Override
	public String toString() {
		return value;
	}


}
