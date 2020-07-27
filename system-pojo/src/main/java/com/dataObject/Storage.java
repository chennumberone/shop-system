package com.dataObject;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chl
 * @date 2020/7/24 17:30
 */
@Data
public class Storage implements Serializable {
    private Integer id;
    private Integer num;
}
