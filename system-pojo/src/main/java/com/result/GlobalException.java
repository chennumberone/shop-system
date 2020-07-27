package com.result;

import lombok.Data;

/**
 * @author chl
 * @date 2020/6/26 17:48
 */
@Data
public class GlobalException extends Exception {
    private EmCommenError emCommenError;

    public GlobalException(EmCommenError emCommenError){
        super();
        this.emCommenError=emCommenError;
    }

    public int getErrorCode(){
        return emCommenError.getErrorCode();
    }

    public String getErrorMsg(){
        return emCommenError.getErrorMsg();
    }
}
