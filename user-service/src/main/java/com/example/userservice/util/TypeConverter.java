package com.example.userservice.util;

import com.example.userservice.domain.type.role.Role;

public class TypeConverter {

    public static String convertStringToRole(String description) {
        if (description.equals(Role.ADMIN.getDescription()))
            return Role.ADMIN.getWord();
        else
            return Role.PERSONAL.getWord();
    }
}
