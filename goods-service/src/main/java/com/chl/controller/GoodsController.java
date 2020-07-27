package com.chl.controller;

import com.chl.config.AccessLimit;

import com.dataObject.User;
import com.interfaces.GoodsService;
import com.chl.util.RedisUtil;
import com.result.CommenReturnObject;
import com.result.EmCommenError;
import com.result.GlobalException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author chl
 * @date 2020/6/24 16:15
 */
@Controller
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    /**
     * 获取商品列表信息
     *
     * @param pageNum
     * @param pageSize
     * @param response
     * @return
     */
    @AccessLimit(seconds = 30, maxCount = 5)
    @RequestMapping("query")
    @ResponseBody
    public HashMap<String, Object> getData(@RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println(pageNum + "," + pageSize);
        HashMap<String, Object> map = goodsService.queryGoods(pageNum, pageSize);
        return map;
    }






    /**
     * 购买商品
     * 1. 判断用户余额是否不足,库存是否不足
     * 1.1 失败-->前端返回信息
     * 1.2 成功（分布式事务控制）
     * 1.2.1 减少余额，减少库存，增加用户积分
     *
     * @param
     * @return
     */
    @AccessLimit(seconds = 30, maxCount = 5)
    @RequestMapping("buy")
    @ResponseBody
    public CommenReturnObject bugGoods(HttpServletRequest request) throws GlobalException {

        BigDecimal price = new BigDecimal(request.getParameter("money"));

        Integer id = Integer.parseInt(request.getParameter("id"));

        //分布式锁实现辅助参数
        String uuid = UUID.randomUUID().toString();
        try {
            boolean rs = redisUtil.setnx(id + "", uuid, 10);
            if (!rs) {
                //并发时阻塞在这里
                return CommenReturnObject.create(EmCommenError.SYSTEM_BUSY.getErrorMsg());
            }

            //1.判断库存是否不足
            if (!goodsService.storageIsEnough(id)) {
                throw new GlobalException(EmCommenError.Storage_NOT_ENOUGH);
            }

            //2.判断用户余额是否不足
            if (!goodsService.moneyIsEnough(price, request)) {
                throw new GlobalException(EmCommenError.MONEY_NOT_ENOUGH);
            }

            //减余额，减库存，增加用户积分
//            boolean flag = goodsService.changeDb(price, id, request);
//            if (!flag) {
//                throw new GlobalException(EmCommenError.BUY_CHANGE_DATA_WRONG);
//            }

            //用mq方式
            System.out.println("mq发送");
            User user=goodsService.getUserBySession(request);
            HashMap hashMap=new HashMap();
            hashMap.put("price",price);
            hashMap.put("id",id);
            hashMap.put("user",user);
            rocketMQTemplate.convertAndSend("dealOrder",hashMap);
            System.out.println("通过mq发送"+hashMap);

        } finally {
            //如果还是该线程的锁则释放
            if (uuid.equals(redisUtil.get(id + ""))) {
                redisUtil.del(id + "");
            }
        }

        return CommenReturnObject.create("成功");
    }


    /**
     * redis集群测试
     *
     * @return
     */
    @RequestMapping("redis")
    @ResponseBody
    public String test() throws GlobalException {
        redisUtil.set("CHL", "best person");
        throw new GlobalException(EmCommenError.SERVER_ERROR);
    }


}
