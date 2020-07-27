package com.result;

import lombok.Data;

/**
 * @author chl
 * @date 2020/6/26 17:13
 */
@Data
public class CommenReturnObject {
    //状态码
    //success 成功  data返回数据
    //fail 失败 返回错误状态码和信息
    private String status;

    private Object data;

    //定义通用成功接口
    public static CommenReturnObject create(Object result){
        return  CommenReturnObject.create(result,"success");
    }


    public static CommenReturnObject create(Object result,String status){
        CommenReturnObject commenReturnObject=new CommenReturnObject();
        commenReturnObject.setData(result);
        commenReturnObject.setStatus(status);
        return commenReturnObject;
    }




}
