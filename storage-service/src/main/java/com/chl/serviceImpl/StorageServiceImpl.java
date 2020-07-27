package com.chl.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chl.dao.StorageDao;
import com.chl.util.RedisUtil;
import com.dataObject.Storage;
import com.interfaces.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author chl
 * @date 2020/7/24 17:32
 */
@Component
@Service(interfaceClass = StorageService.class)
public class StorageServiceImpl implements StorageService {
    @Autowired
    StorageDao storageDao;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public Storage getInfoById(Integer id) {
        return storageDao.getStorageById(id);
    }

    /**
     * 减库存
     *
     * @param id
     * @param num 分布式锁实现
     */
    @Override
    public boolean decreStorage(Integer id, Integer num) {
        storageDao.decreStorage(id, num);
        return true;
    }
}
