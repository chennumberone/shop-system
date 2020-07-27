package com.interfaces;

import com.dataObject.User;
import com.result.GlobalException;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * @author chl
 * @date 2020/7/5 15:09
 */
public interface UserService {
    String getPwdByUsername(String pwd);

    boolean loginCheck(User user) throws GlobalException;

    void addSession(HttpServletResponse response,User user) throws GlobalException;

    BigDecimal queryMoneyByUsername(String username);

    User getUserByUserName(String username);

    void decreMoney(String username,BigDecimal money);
}
