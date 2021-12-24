package com.example.userservice.domain.type.userStatus;


import javax.persistence.AttributeConverter;

public class UserStatusAttributeConverter implements AttributeConverter<String, Integer> {

    private final static Integer NORMAL_VALUE = 1;
    private final static Integer SUSPENDED_VALUE = 2;
    private final static Integer RETIRED_VALUE = 3;

    @Override
    public Integer convertToDatabaseColumn(String status) {
        if (UserStatus.NORMAL.getDescription().equals(status)) {
            return NORMAL_VALUE;
        } else if (UserStatus.SUSPENDED.getDescription().equals(status)) {
            return SUSPENDED_VALUE;
        } else if (UserStatus.RETIRED.getDescription().equals(status)) {
            return RETIRED_VALUE;
        }
        // Todo 예외 처리
        return 0;
    }

    @Override
    public String convertToEntityAttribute(Integer code) {
        if (NORMAL_VALUE == code) {
            return UserStatus.NORMAL.getDescription();
        } else if (SUSPENDED_VALUE == code) {
            return UserStatus.SUSPENDED.getDescription();
        } else if (RETIRED_VALUE == code) {
            return  UserStatus.RETIRED.getDescription();
        }
        // Todo 에외 처리
        return "";
    }

}