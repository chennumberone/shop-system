package com.interfaces;

import com.dataObject.User;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author chl
 * @date 2020/7/5 15:12
 */
public interface GoodsService {
    HashMap<String, Object> queryGoods(Integer pageNum, Integer pageSize);

    //判断余额是否不足
    boolean moneyIsEnough(BigDecimal money, HttpServletRequest request);

    //判断库存是否不足
    boolean storageIsEnough(Integer id);


    //减库存，减余额，增加积分
    boolean changeDb(BigDecimal money,Integer goodsId,User user);

    public User getUserBySession(HttpServletRequest request);
}
