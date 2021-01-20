package com.ty.bugparser.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Testcase {
    private int id;
    // 为了和数据库保持一致，虽然mybatis能够自动完成到驼峰式的转化，但是尽量避免歧义
    private int original_testcase;
    private String testcase;
    private String simplified_case;
}
