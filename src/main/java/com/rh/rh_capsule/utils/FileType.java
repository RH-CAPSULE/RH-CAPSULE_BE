package com.rh.rh_capsule.utils;

import lombok.Getter;

@Getter
public enum FileType {
    IMAGE("image"),
    AUDIO("audio");

    private final String type;

    FileType(String type) {
        this.type = type;
    }
}
