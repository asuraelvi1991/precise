package com.haita.precise.domain.enums;

public enum RetCode {
    SUCCESS("000","成功"),
    FAILURE("004","失败"),
    PARAM_ERROR("001","参数错误"),
    EXCEPTION("009","异常");

    String code;
    String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    RetCode(String code, String msg) {

        this.code = code;
        this.msg = msg;
    }
}
