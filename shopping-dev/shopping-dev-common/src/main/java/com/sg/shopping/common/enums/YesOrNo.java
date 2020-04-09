package com.sg.shopping.common.enums;

public enum YesOrNo {
    NO(0, "no"),
    YES(1, "yes");

    public final Integer type;
    public final String yesOrNo;

    YesOrNo(Integer type, String yesOrNo) {
        this.type = type;
        this.yesOrNo = yesOrNo;
    }
}
