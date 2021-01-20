package com.ty.bugparser.mapper;

import com.ty.bugparser.pojo.OriginalTestResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface OriginalTestResultMapper {
    List<OriginalTestResult> queryOriginalTestResultByHarnessId(int harnessId);
}
