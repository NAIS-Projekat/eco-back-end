package com.eco.environet.util;

import com.eco.environet.users.model.Role;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumMapper {

    public static String convertToTitleCase(String role) {
        return Arrays.stream(role.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
