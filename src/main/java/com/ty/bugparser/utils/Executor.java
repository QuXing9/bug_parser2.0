package com.ty.bugparser.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.ty.bugparser.pojo.TestcaseID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Data
@Component
@ConfigurationProperties("user.executor")
@AllArgsConstructor
@NoArgsConstructor
public class Executor {

    private String prefix;
    private String bashFilePath;
    private String tempFileDir;

    /**
     * 将代码写入指定目录下的随机名称的临时文件，并返回该文件名
     * @return 文件名（完整地址+文件名）
     */
    public String writeInFile(String code) {
        String testcaseFileName = tempFileDir + UUID.randomUUID().toString().substring(0, 8) + ".js";
        FileWriter writer;
        try {
            writer = new FileWriter(testcaseFileName);
            writer.write(code);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testcaseFileName;
    }

    public String readATestcase(String testcaseFileName){
        String jsonStr = "";
        File file = new File(testcaseFileName);
        try {
            FileReader fileReader = new FileReader(file);
            Reader reader = new InputStreamReader(new FileInputStream(file),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void ReadTest(){
        String file_path = "C:\\Users\\10592\\Desktop\\test.json";
        String jsonstr = readATestcase(file_path);
        JSONObject jsonObject = JSONObject.parseObject(jsonstr);
        System.out.println(jsonObject);
    }

    public String createFile(){
        return tempFileDir + UUID.randomUUID().toString().substring(0, 8) + ".js";
    }

    public String createJson(){
        return tempFileDir + UUID.randomUUID().toString().substring(0, 8) + ".json";
    }

    public String executeProcess(String[] command){
        // 构建缓冲区
        ProcessBuilder pb;
        StringBuffer result = null;
        Process proc = null;
        BufferedReader stdInput = null;
        try {
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            result = new StringBuffer();

            // StringUtils.join(str, " ")
            pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            proc = pb.start();
            stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                result.append(s).append("\n");
            }

            stdInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 销毁子进程
            if (proc != null) {
                proc.destroy();
            }
            // 关闭流
            closeStream(stdInput);
        }

        // 返回执行结果
        return result.toString();
    }


    public void deleteTmpfile(String file_path){
        // 删除临时文件
        File tempFile = new File(file_path);
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }

    /*
        返回测试用例ID和测试用例内容
        格式为{"id":int, "testcase":string}
    */
    public String getATestcase(){
        // 创建临时文件，将测试用例写入该文件
        String testcaseFileName = createFile();

        String[] commands_get = {prefix, bashFilePath, "--get-testcase", testcaseFileName};

        executeProcess(commands_get);

        String content = readATestcase(testcaseFileName);

        deleteTmpfile(testcaseFileName);

        return content;
    }

    /*
        返回测试用例ID和测试用例内容
        格式为{"id":int, "testcase":string}
    */
    public String getATestcaseById(String testcaseId){

        String testcaseFileName = createJson();

        String[] commands = {prefix, bashFilePath, "--get-testcase", testcaseFileName, testcaseId};

        executeProcess(commands);

        String content = readATestcase(testcaseFileName);

        deleteTmpfile(testcaseFileName);

        return content;
    }

    /*
        提交测试用例
     */
    public Boolean submitTestcase(String testcaseId){
        String[] commands = {prefix, bashFilePath, "--commit", testcaseId};
        try {
            String result = executeProcess(commands);
            writeInFile(result);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /*
        返回用例执行结果和精简用例内容
        格式为{"executeResult":string,"testcaseContent":string}
     */
    public String simplyTestcase(String code) {
        // 创建临时文件，将测试用例写入该文件
        String testcaseFileName = writeInFile(code);

        String[] commands = {prefix, bashFilePath, "--reduce", testcaseFileName};

        String result = executeProcess(commands);

        String testcaseContent = readATestcase(testcaseFileName);

        deleteTmpfile(testcaseFileName);

        Map<String, String> map = new HashMap<String, String>();

        map.put("executeResult", result);

        map.put("testcaseContent", testcaseContent);

        return JSON.toJSONString(map);
    }

    /*
        返回用例执行结果
        格式为 result:str
    */
    public String executeScript(String code) {
        // 创建临时文件，将测试用例写入该文件
        String testcaseFileName = writeInFile(code);

        String[] commands = {prefix, bashFilePath, "--run", testcaseFileName};

        String result = executeProcess(commands);

        deleteTmpfile(testcaseFileName);

        return result;
    }


    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception ignored) {
            }
        }
    }
//
//    public static void main(String[] args) {
//        Executor executor = new Executor();
//        executor.ReadTest();
//    }
}

