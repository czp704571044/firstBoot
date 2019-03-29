package com.test.demo.dao;

import com.test.demo.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface UserMapper {
    //查询
    List<User> getUserList(String userName);
    //添加
   int addUser(User user);
   //修改
   int upUser(User user);
   //查询单个
    User getUserInfo(String userId);
   //删除
    int delUser(String userId);
    //登录

}
