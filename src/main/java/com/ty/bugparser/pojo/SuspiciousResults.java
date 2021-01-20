package com.ty.bugparser.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuspiciousResults {
    private int id;
    private String bugType;
    private String engine;
    // 为了和数据库保持一致，虽然mybatis能够自动完成到驼峰式的转化，但是尽量避免歧义
    private int output_id;
    private int bugLabel;
    private String reason;
    private String remarks;
    private String assignee;
    private int classifyId;
    private long submit_date;
}
