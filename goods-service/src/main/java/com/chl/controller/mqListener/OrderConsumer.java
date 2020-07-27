package com.chl.controller.mqListener;

import com.dataObject.User;
import com.interfaces.GoodsService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author chl
 * @date 2020/7/25 13:03
 */
@RocketMQMessageListener(topic = "dealOrder",consumerGroup = "${rocketmq.consumer.group}")
@Component
public class OrderConsumer implements RocketMQListener<Object> {
    @Autowired
    GoodsService goodsService;


    /**
     * 处理订单，修改各个服务中数据库的信息
     * @param o
     */
    @Override
    public void onMessage(Object o) {
        System.out.println("接受到消息：");
        HashMap<String,Object> map= (HashMap<String, Object>) o;
        System.out.println("接收到的对象："+map);
        BigDecimal money= (BigDecimal) map.get("price");
        Integer id= (Integer) map.get("id");
        User user= (User) map.get("user");
        goodsService.changeDb(money,id,user);
    }
}
