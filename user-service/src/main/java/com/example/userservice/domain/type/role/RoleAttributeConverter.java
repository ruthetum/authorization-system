package com.example.userservice.domain.type.role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RoleAttributeConverter implements AttributeConverter<String, Integer> {

    private final static Integer ADMIN_VALUE = 1;
    private final static Integer PERSONAL_VALUE = 2;

    @Override
    public Integer convertToDatabaseColumn(String role) {
        if (Role.ADMIN.getDescription().equals(role)) {
            return ADMIN_VALUE;
        } else if (Role.PERSONAL.getDescription().equals(role)) {
            return PERSONAL_VALUE;
        }
        // Todo 예외 처리
        return 0;
    }

    @Override
    public String convertToEntityAttribute(Integer code) {
        if (ADMIN_VALUE == code) {
            return Role.ADMIN.getDescription();
        } else if (PERSONAL_VALUE == code) {
            return Role.PERSONAL.getDescription();
        }
        // Todo 에외 처리
        return "";
    }

}