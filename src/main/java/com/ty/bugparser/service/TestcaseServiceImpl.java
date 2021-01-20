package com.ty.bugparser.service;

import com.ty.bugparser.mapper.TestcaseMapper;
import com.ty.bugparser.pojo.Testcase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestcaseServiceImpl implements TestcaseService {

    @Autowired
    private TestcaseMapper testcaseMapper;

    @Override
    public Testcase queryTestcaseById(int id) {
        return testcaseMapper.queryTestcaseById(id);
    }

    @Override
    public int updateTestcase(Testcase testcase) {
        return testcaseMapper.updateTestcase(testcase);
    }

}
