package com.sg.shopping.common.enums;

public enum Sex {
    FEMALE(0, "female"),
    MALE(1, "male"),
    SECRET(2, "secret");

    public final Integer type;
    public final String sex;

    Sex(Integer type, String sex) {
        this.type = type;
        this.sex = sex;
    }
}
