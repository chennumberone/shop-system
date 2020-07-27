package com.chl.config;

import com.result.CommenReturnObject;
import com.result.EmCommenError;
import com.result.GlobalException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @author chl
 * @date 2020/6/26 18:10
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value=Exception.class)
    public CommenReturnObject exceptionHandle(Exception e){

        //打印错误信息
        e.printStackTrace();

        HashMap<String,Object> map=new HashMap<>();
        if(e instanceof GlobalException){
             GlobalException ex= (GlobalException) e;
             map.put("errorCode",ex.getErrorCode());
             map.put("errorMsg",ex.getErrorMsg());
             return CommenReturnObject.create(map,"fail");
        }else{
            map.put("errorCode", EmCommenError.SERVER_ERROR.getErrorCode());
            map.put("errorMsg",EmCommenError.SERVER_ERROR.getErrorMsg());
            return CommenReturnObject.create(map,"fail");
        }
    }

}
