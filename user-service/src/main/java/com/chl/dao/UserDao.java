package com.chl.dao;

import com.dataObject.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author chl
 * @date 2020/6/18 19:26
 */
@Repository
public interface UserDao {
    String getPwdByUsername(String username);

    BigDecimal queryMoneyByUsername(String username);
    User getUserByUsername(String username);

    void decreMoney(@Param("username") String username, @Param("money") Double money);
}
