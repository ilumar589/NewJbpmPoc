package com.poc.requestapproval.domain;

import javax.persistence.AttributeConverter;

public class UserAuthorityTypeConverter implements AttributeConverter<UserAuthorityType, String> {
	@Override
	public String convertToDatabaseColumn(UserAuthorityType userAuthorityType) {
		return userAuthorityType.toString();
	}

	@Override
	public UserAuthorityType convertToEntityAttribute(String s) {
		return UserAuthorityType.valueOf(s);
	}
}
