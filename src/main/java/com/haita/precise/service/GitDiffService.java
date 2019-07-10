package com.haita.precise.service;

import com.haita.precise.domain.enums.DiffMethod;
import com.haita.precise.domain.enums.RetCode;
import com.haita.precise.domain.gson.DiffResultGson;
import com.haita.precise.domain.gson.FileHtmlGson;
import com.haita.precise.domain.gson.Line;
import com.haita.precise.util.FreeMarkerUtil;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GitDiffService {

    public void diff(String tag, String app, DiffResultGson resultGson) {
        //1.执行git diff tag 生成文件
        if (!genDiffFile(tag, resultGson)) return;

        //2.解析文件返回 内容
        dealGitDiff("gitdiff", resultGson);

        AtomicInteger count = new AtomicInteger();
        //3.获取源文件
        resultGson.getFiles().stream()
                .forEach(file -> {

                    //执行命令复制文件
                    if(!copy(file)) return;
                    String name = file.substring(file.lastIndexOf("/")+1,file.length());
                    if(name==null) return;
                    File javaFile = new File("./temp/"+name);

                    try (BufferedReader bufferedReader =
                                 new BufferedReader(
                                         new InputStreamReader(
                                                 new FileInputStream(javaFile)));){
                        String temp = null;
                        int lineNumber =0;
                        FileHtmlGson gson = new FileHtmlGson();
                        //名字
                        gson.setFileName(name);
                        //方法
                        gson.setMethod(DiffMethod.valueOfCode(resultGson.getMethods().get(count.get())));
                        int formerCount = 0;
                        while ((temp = bufferedReader.readLine()) != null) {
                            //行号
                            lineNumber++;
                            //新建行

                            if(Optional.ofNullable(resultGson.getLinesChangedFormerNo().get(file))
                                    .orElse(new LinkedList<>())
                                    .contains(lineNumber)){
                                Line line = new Line();
                                line.setNumber(lineNumber++);
                                line.setContent(resultGson.getLinesChangedCurrent().get(file).get(formerCount++));
                                //former
                                line.setMethod(1);
                                gson.getLines().add(line);

                            }
                            Line line = new Line();
                            line.setNumber(lineNumber);
                            line.setContent(temp);
                            if(Optional.ofNullable(resultGson.getLinesChangedCurrentNo().get(file))
                                    .orElse(new LinkedList<>())
                                    .contains(lineNumber)){
                                line.setMethod(2);
                            }else{
                                line.setMethod(0);
                            }
                            gson.getLines().add(line);
                        }

                        Writer outputStream = new FileWriter(new File("./html/"+name+".html"));
                        FreeMarkerUtil.getTemplate("color.ftlh", gson, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        count.getAndIncrement();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }
        );

    }

    private boolean copy( String file) {
        String command = "sh getSrc.sh " + file;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process pro = runtime.exec(command);
            int status = pro.waitFor();
            if (status != 0) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean genDiffFile(String tag, DiffResultGson resultGson) {
        String command = "sh git.sh " + tag;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process pro = runtime.exec(command);
            int status = pro.waitFor();
            if (status != 0) {
                return false;
            }

        } catch (IOException e) {
            resultGson.setRetCode(RetCode.EXCEPTION.getCode());
            resultGson.setRetMsg(RetCode.EXCEPTION.getMsg());
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            resultGson.setRetCode(RetCode.EXCEPTION.getCode());
            resultGson.setRetMsg(RetCode.EXCEPTION.getMsg());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void dealGitDiff(String file, DiffResultGson resultGson) {
        File f = new File(file);
        int minusCount = 0;
        int plusCount = 0;
        int minusStart = 0;
        int plusStart = 0;
        try (BufferedReader bufferedReader =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream(f)));) {
            String temp = null;
            String fileName = null;
            String fileFormer = null;
            String fileCur = null;

            int pline = 0, mline = 0, mC = 0, pC = 0;
            List<Integer> listP = new LinkedList<>();
            List<Integer> listM = new LinkedList<>();
            List<String> listPContent = new LinkedList<>();
            List<String> listMContent = new LinkedList<>();
            while ((temp = bufferedReader.readLine()) != null) {
                if (temp.startsWith("diff --git")) {
                    AddParam(resultGson, fileName, fileFormer, fileCur,
                            listP, listM, listPContent, listMContent);

                    String[] s = temp.split(" ");
                    int n = s.length;
                    if (n > 0) {
                        fileName = s[n - 1];
                        fileName = fileName.substring(1, fileName.length()).trim();
                        resultGson.getFiles().add(
                                fileName);
                    }

                    listP = new LinkedList<>();
                    listM = new LinkedList<>();

                }
                //@@ -0,0 +1,20 @@
                if (temp.trim().startsWith("@@") && temp.trim().endsWith("@@")) {
                    minusStart = Integer.valueOf(temp.substring(temp.indexOf("-") + 1, temp.indexOf(",", 0)));
                    minusCount = Integer.valueOf(temp.substring(temp.indexOf(",") + 1,
                            temp.indexOf(" ", temp.indexOf(","))));
                    plusStart = Integer.valueOf(temp.substring(temp.indexOf("+") + 1,
                            temp.indexOf(",", temp.indexOf("+"))));
                    plusCount = Integer.valueOf(temp.substring(temp.indexOf(",", temp.indexOf("+")) + 1,
                            temp.indexOf(" ", temp.indexOf("+"))));
                    pline = 0;
                    mline = 0;
                    mC = 0;
                    pC = 0;
                }


                if (temp.startsWith("---")) {
                    String t = temp.split(" ")[1];
                    fileFormer = t.substring(1, t.length()).trim();
                } else if (temp.startsWith("+++")) {
                    String t = temp.split(" ")[1];
                    fileCur = t.substring(1, t.length()).trim();
                } else if (temp.startsWith("-")) {
                    listM.add(minusStart + mC);
                    listMContent.add(temp.substring(1, temp.length()));
                    mC++;
                    pC++;
                } else if (temp.startsWith("+")) {
                    listP.add(plusStart + pC);
                    listPContent.add(temp.substring(1, temp.length()));
                    pC++;
                } else if (temp.startsWith(" ")) {
                    pC++;
                    mC++;
                }

            }
            AddParam(resultGson, fileName, fileFormer, fileCur, listP,
                    listM, listPContent, listMContent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void AddParam(DiffResultGson resultGson, String fileName,
                          String fileFormer, String fileCur,
                          List<Integer> listP, List<Integer> listM,
                          List<String> listPContent, List<String> listMContent) {
        if (!listP.isEmpty() && fileName != null) {
            resultGson.getLinesChangedCurrentNo().put(fileName, listP);
            resultGson.getLinesChangedCurrent().put(fileName, listPContent);

        }

        if (!listM.isEmpty() && fileName != null) {
            resultGson.getLinesChangedCurrentNo().put(fileName, listM);
            resultGson.getLinesChangedCurrent().put(fileName, listMContent);

        }


        //添加方法
        if (fileFormer != null && fileCur != null) {
            if (fileFormer.equals(fileCur)) {
                resultGson.getMethods().add(DiffMethod.INCREASE.getCode());
            } else if (fileFormer.contains("dev/null")) {
                resultGson.getMethods().add(DiffMethod.ADD.getCode());
            } else {
                resultGson.getMethods().add(DiffMethod.DELETEALL.getCode());
            }

        }
    }
}
