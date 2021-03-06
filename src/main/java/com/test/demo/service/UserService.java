package com.test.demo.service;

import com.test.demo.dao.UserMapper;
import com.test.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    //查询
    public List<User> getUserList(String userName){
       return  userMapper.getUserList(userName);
    }
    //添加
    public int addUser(User user){
        return  userMapper.addUser(user);
    }
    //修改
    public int upUser(User user){
        return  userMapper.upUser(user);
    }
    //查询单个
    public User getUserInfo(String userId){
        return  userMapper.getUserInfo(userId);
    }
    //删除
    public int delUser(String userId){
        return  userMapper.delUser(userId);
    }
}
