package com.chl.dao;

import com.dataObject.Goods;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chl
 * @date 2020/6/24 16:11
 */
@Repository
public interface GoodsDao {
    List<Goods> getGoodsDatas();
}
