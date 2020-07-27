package com.chl.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.interfaces.UserService;
import com.chl.util.RedisUtil;
import com.dataObject.User;
import com.result.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.spring5.context.SpringContextUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author chl
 * @date 2020/6/26 19:17
 */
@Component
public class UserLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    RedisUtil redisUtil;

    @Reference
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws GlobalException {
        //除了login不检查
        if(request.getRequestURI().equals("/toLogin")||request.getRequestURI().equals("/login")){
            return true;
        }
        //先获取cookie
        Cookie[] cookies = request.getCookies();
        if(cookies==null||cookies.length==0)return false;
        String value=null;
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(User.sessionKey)){
                value=cookie.getValue();
            }
        }
        //缓存中寻找
        JSONObject jsonObject= (JSONObject) redisUtil.get("session:"+value);
        //需要转换一下
        User user= JSONObject.toJavaObject(jsonObject,User.class);

        if(user==null){
            return false;
        }
        else{
            String token = UUID.randomUUID().toString().replace("-", "");
            //放入缓存，作为session记录
            redisUtil.set("session:" + token, user, User.sessionTime);
            //放入客户端
            Cookie cookie = new Cookie(User.sessionKey, token);
            cookie.setMaxAge(User.sessionTime);
            cookie.setPath("/");
            response.addCookie(cookie);
            System.out.println("增加cookie where goods");
            return true;
        }
    }
}
