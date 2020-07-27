package com.chl.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chl.dao.UserDao;

import com.chl.util.RedisUtil;
import com.dataObject.User;
import com.interfaces.UserService;
import com.result.EmCommenError;
import com.result.GlobalException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;

import java.util.UUID;

/**
 * @author chl
 * @date 2020/6/18 19:14
 */
@Component
@Service(interfaceClass = UserService.class,timeout = 10000)
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    RedisUtil redisUtil;


    @Override
    public String getPwdByUsername(String username) {
        return userDao.getPwdByUsername(username);
    }

    //登录密码验证
    @Override
    public boolean loginCheck(User user) throws GlobalException {
        if (user == null || StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            throw new GlobalException(EmCommenError.LOGIN_FAIL);
        }
        String username = user.getUsername();
        String pwd = user.getPassword();
        String password = userDao.getPwdByUsername(username);
        return password.equals(pwd);
    }

    /**
     * 增加session，redis方式
     *
     * @param user
     */
    @Override
    public void addSession(HttpServletResponse response, User user) throws GlobalException {
        if (user == null) {
            throw new GlobalException(EmCommenError.SERVER_ERROR);
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        //放入缓存，作为session记录
        redisUtil.set("session:" + token, user, User.sessionTime);
        //放入客户端
        Cookie cookie = new Cookie(User.sessionKey, token);
        cookie.setMaxAge(User.sessionTime);
        cookie.setPath("/");
        response.addCookie(cookie);
        System.out.println("增加cookie");
    }

    /**
     * 查询余额
     *
     * @param username
     * @return
     */
    @Override
    public BigDecimal queryMoneyByUsername(String username) {
        return userDao.queryMoneyByUsername(username);
    }

    @Override
    public User getUserByUserName(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public void decreMoney(String username, BigDecimal money) {
        Double money1=Double.parseDouble(money.toString());
        userDao.decreMoney(username, money1);
    }

}
