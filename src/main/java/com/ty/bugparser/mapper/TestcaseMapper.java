package com.ty.bugparser.mapper;

import com.ty.bugparser.pojo.Testcase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TestcaseMapper {

    Testcase queryTestcaseById(int id);

    int updateTestcase(Testcase testcase);
}
