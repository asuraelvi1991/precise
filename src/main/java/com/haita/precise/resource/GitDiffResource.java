package com.haita.precise.resource;

import com.haita.precise.domain.enums.RetCode;
import com.haita.precise.domain.gson.DiffResultGson;
import com.haita.precise.service.GitDiffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * git上传文件，根据tag号，分析出所有修改的文件和文件行数，并通过html标注出来
 */
@Slf4j
@RestController
public class GitDiffResource {
    @Autowired
    GitDiffService gitDiffService;

    @RequestMapping("hello")
    public String diffTag(@RequestParam(value = "tag",required = true)String  tag,
                          @RequestParam(value = "app",required = true)String  app){
        DiffResultGson gson = new DiffResultGson();

        if(tag==null||app==null){
            gson.setRetCode(RetCode.SUCCESS.getCode());
            gson.setRetCode(RetCode.SUCCESS.getMsg());
            return gson.toString();
        }
        gitDiffService.diff(tag,app ,gson );

        return gson.toString();
    }

}
