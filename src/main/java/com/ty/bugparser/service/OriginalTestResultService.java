package com.ty.bugparser.service;

import com.ty.bugparser.pojo.OriginalTestResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OriginalTestResultService {
    List<OriginalTestResult> queryOriginalTestResultByHarnessId(int harnessId);
}
