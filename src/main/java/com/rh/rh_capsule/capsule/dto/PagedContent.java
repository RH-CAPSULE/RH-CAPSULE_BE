package com.rh.rh_capsule.capsule.dto;

import java.util.List;

public record PagedContent<T>(
        List<T> content,
        Integer prev,
        Integer next
) {
}
