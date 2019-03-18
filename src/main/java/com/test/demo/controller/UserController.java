package com.test.demo.controller;

import com.test.demo.model.User;
import com.test.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUserList",method = RequestMethod.GET)
    public List<User> selectUserList(String userName){
        return  userService.getUserList(userName);
    }
    @RequestMapping("/addUser")
    public void addUser(User user){
        userService.addUser(user);
    }
}
