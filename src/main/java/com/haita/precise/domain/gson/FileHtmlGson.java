package com.haita.precise.domain.gson;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Data
@Setter
@Getter
public class FileHtmlGson {
    String fileName;
    String method;
    List<Line> lines;

    public FileHtmlGson() {
        this.lines = new LinkedList<>();
    }
}
