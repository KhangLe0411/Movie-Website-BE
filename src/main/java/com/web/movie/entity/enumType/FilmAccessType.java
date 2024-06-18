package com.web.movie.entity.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FilmAccessType {
    FREE("Miễn phí"),
    PAID("Trả phí");

    private final String value;
}
