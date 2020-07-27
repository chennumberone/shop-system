package com.dataObject;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author chl
 * @date 2020/6/24 14:47
 */
@Data
public class Goods  {
    private Integer Id;
    private String name;
    private BigDecimal price;
    private String img_path;
}
