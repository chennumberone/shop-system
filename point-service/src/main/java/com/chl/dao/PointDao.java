package com.chl.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author chl
 * @date 2020/7/24 19:17
 */
@Repository
public interface PointDao {
    void increScore(@Param("username") String username, @Param("addScore")Integer addScore);
}
