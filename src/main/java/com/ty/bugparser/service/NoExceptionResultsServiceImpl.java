package com.ty.bugparser.service;

import com.ty.bugparser.mapper.NoExceptionResultsMapper;
import com.ty.bugparser.pojo.NoExceptionResults;
import com.ty.bugparser.pojo.Testcase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoExceptionResultsServiceImpl implements NoExceptionResultsService{

    @Autowired
    private NoExceptionResultsMapper noExceptionResultsMapper;

    @Override
    public List<NoExceptionResults> queryAllSuspiciousResults() {
        return noExceptionResultsMapper.queryAllSuspiciousResults();
    }

    @Override
    public List<NoExceptionResults> queryAllAnalysed() {
        return noExceptionResultsMapper.queryAllAnalysed();
    }

    @Override
    public List<NoExceptionResults> queryAllNoAnalysed() {
        return noExceptionResultsMapper.queryAllNoAnalysed();
    }

    @Override
    public NoExceptionResults querySuspiciousResultsById(int id) {
        return noExceptionResultsMapper.querySuspiciousResultsById(id);
    }

    @Override
    public int queryRandomNoAnalysedSuspiciousId() {
        return noExceptionResultsMapper.queryRandomNoAnalysedSuspiciousId();
    }

    @Override
    public int queryHarnessIdBySuspiciousId(int suspiciousId) {
        return noExceptionResultsMapper.queryHarnessIdBySuspiciousId(suspiciousId);
    }

    @Override
    public int queryTestcaseIdByHarnessId(int harnessId) {
        return noExceptionResultsMapper.queryTestcaseIdByHarnessId(harnessId);
    }

    @Override
    public String queryTestcaseCodeByTestcaseId(int testcaseId) {
        return noExceptionResultsMapper.queryTestcaseCodeByTestcaseId(testcaseId);
    }

    @Override
    public int updateSuspiciousResults(NoExceptionResults noExceptionResults) {
        return noExceptionResultsMapper.updateSuspiciousResults(noExceptionResults);
    }

}
