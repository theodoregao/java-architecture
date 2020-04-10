package com.sg.shopping.common.enums;

public enum PayMethod {
    WEIXIN(1, "weixin"),
    ALIPAY(2, "alipay");

    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
