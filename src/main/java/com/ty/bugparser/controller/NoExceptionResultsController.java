package com.ty.bugparser.controller;

import com.alibaba.fastjson.JSON;
import com.ty.bugparser.pojo.NoExceptionResults;
import com.ty.bugparser.pojo.OriginalTestResult;
import com.ty.bugparser.pojo.Testcase;
import com.ty.bugparser.service.NoExceptionResultsService;
import com.ty.bugparser.service.OriginalTestResultService;
import com.ty.bugparser.service.TestcaseService;
import com.ty.bugparser.utils.Executor;
import com.ty.bugparser.utils.Timer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/NoExceptionResults")
@Data
public class NoExceptionResultsController {

    @Autowired
    private NoExceptionResultsService noExceptionResultsService;

    @Autowired
    private Executor executor;

    @Autowired
    private TestcaseService testcaseService;

    @Autowired
    private OriginalTestResultService originalTestResultService;

    @Autowired
    private Timer timer;

    /**
     * 统计所有可疑用例数目，以及其中已分析和未分析的数目
     * @return 跳转到用例分析的页面
     */
    @RequestMapping("/Analyse")
    public String countNumber(HttpSession session, Model model) {
        // 获取全部结果
        List<NoExceptionResults> allResults = noExceptionResultsService.queryAllSuspiciousResults();
        int allNumber = allResults.size();
        int analysedNumber = 0;
        int noAnalysedNumber = 0;
        int todayAnalysedNumber = 0;
        String loginUser = (String)session.getAttribute("loginUser");

        // 遍历一遍结果，获取到统计数据
        for (NoExceptionResults record : allResults) {
            if (record.getAssignee() != null) {
                analysedNumber += 1;
                if (record.getSubmit_date() != 0L
                        && timer.isSameDayOfMillis(record.getSubmit_date(), System.currentTimeMillis())
                        && loginUser.equals(record.getAssignee())) {
                    todayAnalysedNumber += 1;
                }
            } else {
                noAnalysedNumber += 1;
            }
        }

        // 将结果写入model
        model.addAttribute("allNumber", allNumber);
        model.addAttribute("analysedNumber", analysedNumber);
        model.addAttribute("noAnalysedNumber", noAnalysedNumber);
        model.addAttribute("todayAnalysedNumber", todayAnalysedNumber);

        return "/BugAnalyse/NoExceptionBugAnalyse";
    }

    @RequestMapping("/queryAll")
    @ResponseBody
    public String queryAll() {
        List<NoExceptionResults> suspiciousResults = noExceptionResultsService.queryAllSuspiciousResults();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", suspiciousResults.size());
        map.put("data", suspiciousResults);

        return JSON.toJSONString(map);
    }

    @RequestMapping("/queryAllAnalysed")
    @ResponseBody
    public String queryAllAnalysed() {
        List<NoExceptionResults> suspiciousResults = noExceptionResultsService.queryAllAnalysed();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", suspiciousResults.size());
        map.put("data", suspiciousResults);

        return JSON.toJSONString(map);
    }

    @RequestMapping("/queryAllNoAnalysed")
    @ResponseBody
    public String queryAllNoAnalysed() {
        List<NoExceptionResults> suspiciousResults = noExceptionResultsService.queryAllNoAnalysed();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", suspiciousResults.size());
        map.put("data", suspiciousResults);

        return JSON.toJSONString(map);
    }

    /**
     * 从未分析的可疑用例中随机挑一个，并返回其相关的三个ID值，组装成Map返回
     * @return 将需要的四部分信息组装成Map返回
     */
    @RequestMapping("/getATestcase")
    @ResponseBody
    public String getATestcase() {

        int suspiciousId = noExceptionResultsService.queryRandomNoAnalysedSuspiciousId();
        int harnessId = noExceptionResultsService.queryHarnessIdBySuspiciousId(suspiciousId);
        int testcaseId = noExceptionResultsService.queryTestcaseIdByHarnessId(harnessId);
        String testcase = noExceptionResultsService.queryTestcaseCodeByTestcaseId(testcaseId);

        Map<String, String> map = new HashMap<>(4);
        map.put("suspiciousId", String.valueOf(suspiciousId));
        map.put("harnessId", String.valueOf(harnessId));
        map.put("testcaseId", String.valueOf(testcaseId));
        map.put("testcase", testcase);
        return JSON.toJSONString(map);
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

    /**
     * 将用例复原
     * @param testcaseId 要复原的用例ID
     * @return 复原后的测试用例代码
     */
    @RequestMapping("/recoverTestcase")
    @ResponseBody
    public String recoverTestcase(String testcaseId) {
        return noExceptionResultsService.queryTestcaseCodeByTestcaseId(Integer.parseInt(testcaseId));
    }

    /**
     * 提交用例，主要是给Sus表中填充字段assignee和submit_date，以及Cases表中填充精简后的字符串
     * @param suspiciousId 可疑用例的ID
     * @param testcaseId 可疑用例对应的Testcases表中的ID
     * @param simplifiedCase 精简后的测试用例代码
     * @param assignee 被委托人，即对该用例进行分析的人
     * @return flag 字符串，"0"表示提交失败，"1"表示提交成功
     */
    @RequestMapping("/submitTestcase")
    @ResponseBody
    public String submitTestcase(String suspiciousId,
                                 String testcaseId,
                                 String simplifiedCase,
                                 String assignee) {

        // 向SuspiciousResults的对应记录中填充assignee信息，将其视为分析过的
        NoExceptionResults noExceptionResults = noExceptionResultsService.querySuspiciousResultsById(Integer.parseInt(suspiciousId));
        int updateAssigneeResult = 0;
        if (noExceptionResults != null) {
            noExceptionResults.setAssignee(assignee);
            noExceptionResults.setSubmit_date(System.currentTimeMillis());
            updateAssigneeResult = noExceptionResultsService.updateSuspiciousResults(noExceptionResults);
        }

        // 向Testcases的对应记录中填充simplifiedCase信息
        Testcase testcase = testcaseService.queryTestcaseById(Integer.parseInt(testcaseId));
        int updateTestcaseResult = 0;
        if (testcase != null) {
            testcase.setSimplified_case(simplifiedCase);
            updateTestcaseResult = testcaseService.updateTestcase(testcase);
        }

        // 假如两个操作都成功了，先解锁，再返回 "1"，表示提交成功
        String flag = "0";
        if (updateAssigneeResult == 1 && updateTestcaseResult == 1) {
            flag = "1";
        }

        return flag;
    }

    @RequestMapping("/getOriginalResult/{suspiciousId}")
    @ResponseBody
    public String getOriginalTestResult(@PathVariable String suspiciousId) {
        // 获取结果列表
        int harnessId = noExceptionResultsService.queryHarnessIdBySuspiciousId(Integer.parseInt(suspiciousId));
        List<OriginalTestResult> originalTestResult = originalTestResultService.queryOriginalTestResultByHarnessId(harnessId);

        // 将出问题的那个结果的对象的isBug字段赋值为1
        for (OriginalTestResult testResult : originalTestResult) {
            if (testResult.getHarnessId() == harnessId) {
                testResult.setIsBug(1);
            }
        }

        Map<String, Object> map = new HashMap<>(4);
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", originalTestResult.size());
        map.put("data", originalTestResult);

        return JSON.toJSONString(map);
    }

}
