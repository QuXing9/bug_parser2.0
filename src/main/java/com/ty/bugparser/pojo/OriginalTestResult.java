package com.ty.bugparser.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 本类对应于初始的差分测试结果对象
 * 主要的字段有：testbed, returncode, stdout, stderr, duration_ms
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OriginalTestResult {
    // 注意，这个harnessId是每一项自己的id，并非查询的传入参数
    private int harnessId;
    private String testbed;
    private String engine;
    private int returncode;
    private String stdout;
    private String stderr;
    private int duration_ms;
    // isBug字段不从数据库中查，而直接使用其默认值0，表示它不是bug；在Controller层会进行检查，将有问题的置为1，表示需要分析
    // 将根据该字段来在前端中调用回调以高亮显示该列。
    private int isBug;
}
