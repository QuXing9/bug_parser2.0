package com.ty.bugparser.service;

import com.ty.bugparser.mapper.OriginalTestResultMapper;
import com.ty.bugparser.pojo.OriginalTestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OriginalTestResultServiceImpl implements OriginalTestResultService {

    @Autowired
    private OriginalTestResultMapper originalTestResultMapper;

    @Override
    public List<OriginalTestResult> queryOriginalTestResultByHarnessId(int harnessId) {
        return originalTestResultMapper.queryOriginalTestResultByHarnessId(harnessId);
    }
}
