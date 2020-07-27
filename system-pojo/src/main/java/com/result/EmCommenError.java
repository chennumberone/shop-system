package com.result;

/**
 * @author chl
 * @date 2020/6/26 17:56
 */
public enum EmCommenError {
    SERVER_ERROR(00000,"服务端异常"),
    LOGIN_FAIL(10001,"登录失败"),
    OPERATE_OFEN(20001,"操作频繁"),
    MONEY_NOT_ENOUGH(30001,"余额不足"),
    Storage_NOT_ENOUGH(40001,"商品库存不足"),
    BUY_CHANGE_DATA_WRONG(50001,"系统繁忙，购买错误"),
    SYSTEM_BUSY(60001,"系统繁忙,请稍后再试")
    ;
    private int errorCode;
    private String errorMsg;

    private EmCommenError(int errorCode, String errorMsg) {
        this.errorCode=errorCode;
        this.errorMsg=errorMsg;
    }
    public int getErrorCode(){
        return errorCode;
    }
    public String getErrorMsg(){
        return errorMsg;
    }
}
