package com.dataObject;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author chl
 * @date 2020/6/18 19:24
 */
@Data
public class User implements Serializable{
    private String username;
    private String password;
    private BigDecimal money;
    //分布式session存活的时间
    public final static Integer sessionTime = 60 * 30;
    //放入cookie的key名
    public final static String sessionKey = "SESSION_COOKIE_KEY";
}
