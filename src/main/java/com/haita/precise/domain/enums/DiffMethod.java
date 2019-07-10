package com.haita.precise.domain.enums;

public enum DiffMethod {
    ADD(1),
    INCREASE(2),
    DELETE(3),
    DELETEALL(4);

    private int code;

    DiffMethod(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static String valueOfCode(int code){
        for(DiffMethod method : DiffMethod.values()){
            if(code==method.getCode()){
                return method.name();
            }
        }
        return null;
    }

}
