package com.ty.bugparser.service;

import com.ty.bugparser.pojo.Testcase;
import org.springframework.stereotype.Service;

@Service
public interface TestcaseService {

    Testcase queryTestcaseById(int id);

    int updateTestcase(Testcase testcase);
}
