package com.chl.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chl.util.RedisUtil;
import com.dataObject.User;
import com.result.EmCommenError;
import com.result.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author chl
 * @date 2020/6/26 18:33
 */
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            AccessLimit accessLimit = ((HandlerMethod) handler).getMethodAnnotation(AccessLimit.class);
            //没有该注解
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();

            User user = getUser(request,response);

            String key = request.getRequestURI()+"_"+ user.getUsername();
            Integer count= (Integer) redisUtil.get(key);
            if(count==null){
                redisUtil.set(key,1,seconds);
            }else if(count<maxCount){
                redisUtil.incr(key,1);
                System.out.println("操作第"+count+"次");
            }else{
                render(response,EmCommenError.OPERATE_OFEN.getErrorMsg());
                return false;
            }
            return true;
        }
        return true;
    }


    private void render(HttpServletResponse response, String err)throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(err.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        //先获取cookie
        Cookie[] cookies = request.getCookies();
        System.out.println("getUser cookies:" + cookies);
        if (cookies == null || cookies.length == 0) return null;
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
        return user;
    }
}
