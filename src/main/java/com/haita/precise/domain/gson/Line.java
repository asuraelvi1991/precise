package com.haita.precise.domain.gson;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Line {
    int number;
    String content;
    int method;
}
