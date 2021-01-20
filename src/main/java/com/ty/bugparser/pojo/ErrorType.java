package com.ty.bugparser.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ErrorType {
    private int id;
    private int error_type;
    private String engine;
    private String error_info;
    private String error_api;
    private int count;
}
