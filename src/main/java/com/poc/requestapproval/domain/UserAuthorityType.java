package com.poc.requestapproval.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum UserAuthorityType {
	ROLE_ADMIN, REQUESTER_0, APPROVER_1, APPROVER_2, APPROVER_3, ROLE_USER;

	/**
	 * cached data only for roles that take part in the approval process
	 */
	private static final Map<Integer, UserAuthorityType> approvalRoles;

	static {
		approvalRoles = new HashMap<>();
		approvalRoles.put(1, APPROVER_1);
		approvalRoles.put(2, APPROVER_2);
		approvalRoles.put(3, APPROVER_3);
	}

	public static UserAuthorityType fromValue(String name) {
		for (UserAuthorityType authorityType : values()) {
			if (authorityType.name().equals(name)) {
				return authorityType;
			}
		}

		throw new IllegalArgumentException(
				"Unknown enum type " + name + ", Allowed values are " + Arrays.toString(values()));
	}

	public static Optional<UserAuthorityType> typeFromIndex(int index) {
		return Optional.ofNullable(approvalRoles.get(index));
	}
}
