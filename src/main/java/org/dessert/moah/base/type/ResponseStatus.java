package org.dessert.moah.base.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    SUCCESS("success"),
    FAIL("fail");

    private final String description;
}
