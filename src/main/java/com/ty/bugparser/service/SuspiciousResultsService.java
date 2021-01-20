package com.ty.bugparser.service;

import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.pojo.Testcase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SuspiciousResultsService {

    List<SuspiciousResults> queryAllSuspiciousResults();

    List<SuspiciousResults> queryAllAnalysed();

    List<SuspiciousResults> queryAllNoAnalysed();

    SuspiciousResults querySuspiciousResultsById(int id);

    /**
     * 获取一个随机的没有分析过的Suspicious表中的id
     * @return int型的id
     */
    int queryRandomNoAnalysedSuspiciousId();

    /**
     * 根据Suspicious中的id获取对应的Harness表中的id (也就是Suspicious表中的output_id)
     * @param suspiciousId
     * @return
     */
    int queryHarnessIdBySuspiciousId(int suspiciousId);

    /**
     * 根据Harness中的id获取对应的Testcases表中的id
     * @param harnessId
     * @return
     */
    int queryTestcaseIdByHarnessId(int harnessId);

    /**
     * 根据Testcases中的id获取到对应的testcase代码内容
     * @param testcaseId
     * @return
     */
    String queryTestcaseCodeByTestcaseId(int testcaseId);

    /**
     * 根据Testcases中的id获取到对应的auto_simplified_testcase代码内容
     * @param testcaseId
     * @return auto_simplified_testcase代码内容
     */
    String queryBeforeSimplifiedTestcaseByTestcaseId(int testcaseId);

    int updateSuspiciousResults(SuspiciousResults suspiciousResults);

    List<Testcase> querySuspiciousTestcaseByKeyword(String keyword);

    List<SuspiciousResults> querySuspiciousResultsByClassifyId(int classifyId);

}
