package com.sg.shopping.common.enums;

public enum CommentLevel {
    GOOD(1, "good"),
    NORMAL(2, "normal"),
    BAD(3, "bad");

    public final Integer type;
    public final String level;

    CommentLevel(Integer type, String level) {
        this.type = type;
        this.level = level;
    }
}
