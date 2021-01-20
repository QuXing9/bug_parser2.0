package com.ty.bugparser.service;

import com.ty.bugparser.mapper.SuspiciousResultsMapper;
import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.pojo.Testcase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SuspiciousResultsServiceImpl implements SuspiciousResultsService{

    @Autowired
    private SuspiciousResultsMapper suspiciousResultsMapper;

    @Override
    public List<SuspiciousResults> queryAllSuspiciousResults() {
        return suspiciousResultsMapper.queryAllSuspiciousResults();
    }

    @Override
    public List<SuspiciousResults> queryAllAnalysed() {
        return suspiciousResultsMapper.queryAllAnalysed();
    }

    @Override
    public List<SuspiciousResults> queryAllNoAnalysed() {
        return suspiciousResultsMapper.queryAllNoAnalysed();
    }

    @Override
    public SuspiciousResults querySuspiciousResultsById(int id) {
        return suspiciousResultsMapper.querySuspiciousResultsById(id);
    }

    @Override
    public int queryRandomNoAnalysedSuspiciousId() {
        return suspiciousResultsMapper.queryRandomNoAnalysedSuspiciousId();
    }

    @Override
    public int queryHarnessIdBySuspiciousId(int suspiciousId) {
        return suspiciousResultsMapper.queryHarnessIdBySuspiciousId(suspiciousId);
    }

    @Override
    public int queryTestcaseIdByHarnessId(int harnessId) {
        return suspiciousResultsMapper.queryTestcaseIdByHarnessId(harnessId);
    }

    @Override
    public String queryTestcaseCodeByTestcaseId(int testcaseId) {
        return suspiciousResultsMapper.queryTestcaseCodeByTestcaseId(testcaseId);
    }

    @Override
    public String queryBeforeSimplifiedTestcaseByTestcaseId(int testcaseId) {
        return suspiciousResultsMapper.queryBeforeSimplifiedTestcaseByTestcaseId(testcaseId);
    }

    @Override
    public int updateSuspiciousResults(SuspiciousResults suspiciousResults) {
        return suspiciousResultsMapper.updateSuspiciousResults(suspiciousResults);
    }

    @Override
    public List<Testcase> querySuspiciousTestcaseByKeyword(String keyword) {
        return suspiciousResultsMapper.querySuspiciousTestcaseByKeyword(keyword);
    }

    @Override
    public List<SuspiciousResults> querySuspiciousResultsByClassifyId(int classifyId) {
        return suspiciousResultsMapper.querySuspiciousResultsByClassifyId(classifyId);
    }
}
