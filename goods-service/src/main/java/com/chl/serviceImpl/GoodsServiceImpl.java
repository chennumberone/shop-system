package com.chl.serviceImpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.chl.dao.GoodsDao;
import com.chl.util.RedisUtil;
import com.dataObject.Storage;
import com.dataObject.User;
import com.interfaces.GoodsService;
import com.dataObject.Goods;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.interfaces.PointService;
import com.interfaces.StorageService;
import com.interfaces.UserService;
import com.result.EmCommenError;
import com.result.GlobalException;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * @author chl
 * @date 2020/6/24 16:12
 */
@Component
@Service(interfaceClass = GoodsService.class)
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    GoodsDao goodsDao;


    @Reference(timeout = 10000)
    UserService userService;


    @Reference
    StorageService storageService;

    @Reference
    PointService pointService;

    @Override
    public HashMap<String, Object> queryGoods(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsDao.getGoodsDatas();
        System.out.println(list);

        PageInfo<Goods> pageInfo = new PageInfo<>(list);

        HashMap<String, Object> map = new HashMap<>();
        map.put("data", pageInfo.getList());
        map.put("code", 0);
        map.put("msg", "ok");
        map.put("len", pageInfo.getTotal());
        return map;
    }

    /**
     * 余额是否不足
     *
     * @param money
     * @return
     */
    @Override
    public boolean moneyIsEnough(BigDecimal money, HttpServletRequest request) {
        User user = getUserBySession(request);
        if (user == null) {
            return false;
        }
        System.out.println("缓存中查询的user对象：" + user);
        //查询余额
        BigDecimal bigDecimal = userService.queryMoneyByUsername(user.getUsername());
        //余额不足
        if (bigDecimal.compareTo(money) < 0) {
            System.out.println("余额不足");
            return false;
        }
        return true;
    }

    /**
     * session中获取对象信息
     *
     * @param request
     * @return
     */
    @Override
    public User getUserBySession(HttpServletRequest request) {
        //session获取用户对象
        //先获取cookie
        Cookie[] cookies = request.getCookies();
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

    /**
     * 判断库存是否不足
     * 高并发下怎么解决超卖的问题？
     *
     * @param id
     * @return
     */
    @Override
    public boolean storageIsEnough(Integer id) {
        Storage storage = storageService.getInfoById(id);
        if (storage.getNum() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 下单的时候改变数据
     * 分布式事务处理
     * @param money
     * @param goodsId
     * @return
     */
    @Override
    @GlobalTransactional(timeoutMills = 3000000,rollbackFor = Exception.class)
    //globalexception修饰的方法体内不能用try catch
    public boolean changeDb(BigDecimal money, Integer goodsId, User user) {
            //减余额
            userService.decreMoney(user.getUsername(), money);
            //减库存
            storageService.decreStorage(goodsId, 1);
            //增加积分
//            int i=1/0;
            pointService.increScore(user.getUsername(), 50);
            System.out.println(user.getUsername()+"购买成功--》改变数据成功");
            return true;
    }
}
