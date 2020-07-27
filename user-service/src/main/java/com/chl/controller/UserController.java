package com.chl.controller;


import com.chl.serviceImpl.UserServiceImpl;
import com.interfaces.UserService;
import com.dataObject.User;
import com.result.CommenReturnObject;
import com.result.EmCommenError;
import com.result.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author chl
 * @date 2020/6/18 19:33
 */

@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @RequestMapping("login")
    @ResponseBody
    public CommenReturnObject login(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    HttpServletResponse response
                                    ) throws GlobalException {
        User user=userService.getUserByUserName(username);
        boolean loginStatus = userService.loginCheck(user);
        if (loginStatus) {
            //实现分布式session
             userService.addSession(response,user);

            //登录成功
            return CommenReturnObject.create("登录成功");
        } else {
            throw new GlobalException(EmCommenError.LOGIN_FAIL);
        }
    }

    @RequestMapping("test")
    @ResponseBody
    public String  test(){
        return "yes";
    }

}
