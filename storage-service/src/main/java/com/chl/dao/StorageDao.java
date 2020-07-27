package com.chl.dao;

import com.dataObject.Storage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author chl
 * @date 2020/7/24 17:34
 */
@Repository
public interface StorageDao {
    Storage getStorageById(Integer id);

    void decreStorage(@Param("id") Integer id,@Param("num")Integer num);
}
