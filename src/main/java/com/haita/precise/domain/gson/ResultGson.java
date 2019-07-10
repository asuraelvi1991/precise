package com.haita.precise.domain.gson;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class ResultGson extends BaseGson {
    private String retCode;
    private String retMsg;

}
