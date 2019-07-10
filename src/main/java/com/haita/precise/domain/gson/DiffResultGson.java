package com.haita.precise.domain.gson;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
@Data
@Getter
public class DiffResultGson extends ResultGson {
    //修改的所有文件
    private List<String> files;
    //当前修改的行数
    private Map<String, List<Integer>> linesChangedCurrentNo;
    private Map<String, List<String>> linesChangedCurrent;

    //之前文件和当前文件区别的地方
    private Map<String, List<Integer>> linesChangedFormerNo;
    private Map<String, List<String>> linesChangedFormer;

    //每个文件的方法，分为新增，修改，删除
    private List<Integer> methods;

    public DiffResultGson() {
        this.files = new LinkedList<>();
        this.methods = new LinkedList<>();
        this.linesChangedCurrent = new HashMap<>();
        this.linesChangedFormer = new HashMap<>();
        this.linesChangedCurrentNo = new HashMap<>();
        this.linesChangedFormerNo = new HashMap<>();
    }
}
