package com.ty.bugparser.service;

import com.ty.bugparser.pojo.ErrorType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ErrorTypeService {

    List<ErrorType> queryAllErrorType();

}
