package com.ty.bugparser.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.ty.bugparser.pojo.OriginalTestResult;
import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.pojo.Testcase;
import com.ty.bugparser.pojo.TestcaseID;
import com.ty.bugparser.service.OriginalTestResultService;
import com.ty.bugparser.service.SuspiciousResultsService;
import com.ty.bugparser.service.TestcaseService;
import com.ty.bugparser.utils.Executor;
import com.ty.bugparser.utils.Timer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/SuspiciousResults")
@ConfigurationProperties("user.gettestcase")
@Data
public class SuspiciousResultsController {

    @Autowired
    private SuspiciousResultsService suspiciousResultsService;

    @Autowired
    private Executor executor;

    @Autowired
    private TestcaseService testcaseService;

    @Autowired
    private OriginalTestResultService originalTestResultService;

    @Autowired
    private Timer timer;

    /**
     * lockedSuspiciousIds:临时缓存，用来防止多用户重复读同一条数据
     * key：SuspiciousId
     * value：该Id被读取时的时间（毫秒数）
     */
    private static Map<Integer, Long> lockedSuspiciousIds = new HashMap<>();

    // 这两个值将从配置文件注入
    private int timeLimitHours;
    private int maxUsersCount;

    @RequestMapping("/Analyse")
    public String countNumber(HttpSession session, Model model) {
        return "/BugAnalyse/BugAnalyse";
    }

    @RequestMapping("/getATestcase")
    @ResponseBody
    public String getATestcase() {
        // lazy-check策略：只在每一次读用例之前，检查一下是否有锁定超时的情况：有的话解锁；没有的话再正常读；
        List<Integer> overtimeSuspiciousIds = new ArrayList<>();
        // timeLimitHours：超时时间（单位：小时）
        for (Map.Entry<Integer, Long> entry : lockedSuspiciousIds.entrySet()) {
            if (Math.abs(entry.getValue() - System.currentTimeMillis()) > timeLimitHours * 60 * 60 * 1000) {
                overtimeSuspiciousIds.add(entry.getKey());
            }
        }
        for (Integer sId : overtimeSuspiciousIds) {
            lockedSuspiciousIds.remove(sId);
        }

        //得到的是用例ID和用例内容，需要拆解
        String content = executor.getATestcase();

        JSONObject jsonObject = JSONArray.parseObject(content);

        int testcaseID = (int) jsonObject.get("id");
        String testcaseContent = (String) jsonObject.get("testcase");
        String result = executor.executeScript(testcaseContent);

        int count = 0;
        do {
            if (count++ > maxUsersCount) {
                testcaseID = -1;
                break;
            }
        } while(lockedSuspiciousIds.containsKey(testcaseID));

        if (testcaseID != -1) {
            lockedSuspiciousIds.put(testcaseID, System.currentTimeMillis());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", testcaseID);
        map.put("testcase", testcaseContent);
        map.put("result", result);

        return JSON.toJSONString(map);
    }

    @RequestMapping("/getATestcaseById/{testcaseId}")
    @ResponseBody
    public String getATestcaseById(@PathVariable String testcaseId) {
        String result = executor.getATestcaseById(testcaseId);
        JSONObject jsonObject = JSONArray.parseObject(result);

        TestcaseID object = new TestcaseID();
        object.setId((Integer) jsonObject.get("id"));
        object.setTestcase((String) jsonObject.get("testcase"));

        List<TestcaseID> objects = new ArrayList<>();

        objects.add(object);

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", objects.size());
        map.put("data", objects);

        return JSON.toJSONString(map);
    }

    @RequestMapping("/getBeforeSimplifiedTestcase")
    @ResponseBody
    public String getBeforeSimplifiedTestcase(String testcaseId) {
        String result = getATestcaseById(testcaseId);
        JSONObject jsonObject = JSONArray.parseObject(result);
        String message = (String) jsonObject.get("data");
        JSONArray jsonArray = JSONArray.parseArray(message);
        JSONObject object = (JSONObject) jsonArray.get(0);
        return (String) object.get("testcase");
    }


    /**
     * 精简用例，对传入测试用例进行精简，并返回精简后的结果
     * @param code 测试用例的代码
     * @return 脚本执行该测试用例的结果字符串
     */
    @RequestMapping("/simplify")
    @ResponseBody
    public String simplify(String code) {
        return executor.simplyTestcase(code);
    }

    /**
     * 执行脚本，对传入测试用例进行测试，并返回脚本执行的结果
     * @param code 测试用例的代码
     * @return 脚本执行该测试用例的结果字符串
     */
    @RequestMapping("/run")
    @ResponseBody
    public String run(String code) {
        return executor.executeScript(code);
    }

    @RequestMapping("/sumbit/{testcaseId}")
    @ResponseBody
    public Boolean sumbit(@PathVariable String testcaseId){
        lockedSuspiciousIds.remove(Integer.valueOf(testcaseId));
        return executor.submitTestcase(testcaseId);
    }

    /**
     * 将用例复原
     * @param testcaseId 要复原的用例ID
     * @return 复原后的测试用例代码
     */
    @RequestMapping("/recoverTestcase")
    @ResponseBody
    public String recoverTestcase(String testcaseId) {
        return suspiciousResultsService.queryTestcaseCodeByTestcaseId(Integer.parseInt(testcaseId));
    }

}
