package com.interfaces;

import com.dataObject.Storage;

/**
 * @author chl
 * @date 2020/7/24 17:30
 */
public interface StorageService {
    Storage getInfoById(Integer id);

    boolean decreStorage(Integer id,Integer num);
}
