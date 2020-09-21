package com.iot.user.constant;

/**
 *@name http 错误码常量类
 *@author qingchen
 * @version 1.0 2017/8/27
 */
public class HttpErrorCode {

    /**
     * HTTP请求成功
     */
    public static final int RESPONSE_SUCCESS = 0;

    /**
     * HTTP请求失败
     */
    public static final int RESPONSE_FAIL = -1;

    /**
     * 管理员解绑设备，需要先将设备管理权转让
     */
    public static final int RESPONSE_NEED_TRANSFORM = 43;



}
