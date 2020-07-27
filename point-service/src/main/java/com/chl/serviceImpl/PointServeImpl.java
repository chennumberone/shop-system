package com.chl.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chl.dao.PointDao;
import com.interfaces.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chl
 * @date 2020/7/24 19:20
 */
@Component
@Service(interfaceClass = PointService.class)
public class PointServeImpl implements PointService {
    @Autowired
    PointDao pointDao;

    @Override
    public void increScore(String username, Integer addScore) {
        pointDao.increScore(username, addScore);
    }
}
