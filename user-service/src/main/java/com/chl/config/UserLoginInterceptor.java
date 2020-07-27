package com.chl.config;

import com.alibaba.fastjson.JSONObject;


import com.chl.serviceImpl.UserServiceImpl;
import com.chl.util.RedisUtil;

import com.dataObject.User;

import com.result.EmCommenError;
import com.result.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chl
 * @date 2020/6/26 19:17
 */
@Component
public class UserLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserServiceImpl userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws GlobalException, GlobalException {
        //除了login不检查
        if (request.getRequestURI().equals("/toLogin") || request.getRequestURI().equals("/login")) {
            return true;
        }
        //先获取cookie
        Cookie[] cookies = request.getCookies();
        System.out.println("cookies:" + cookies);
        if (cookies == null || cookies.length == 0) return false;
        String value = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(User.sessionKey)) {
                value = cookie.getValue();
            }
        }
        //缓存中寻找
        JSONObject jsonObject = (JSONObject) redisUtil.get("session:" + value);
        //需要转换一下
        User user = JSONObject.toJavaObject(jsonObject, User.class);

        if (user == null) {
            return false;
        } else {
            //延长有效期
            userService.addSession(response,user);
            return true;
        }
    }
}
