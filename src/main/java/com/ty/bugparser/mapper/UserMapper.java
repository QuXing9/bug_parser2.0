package com.ty.bugparser.mapper;

import com.ty.bugparser.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    User getUserByUsernameAndPassword(String username, String password);

    int updateUser(User user);
}
