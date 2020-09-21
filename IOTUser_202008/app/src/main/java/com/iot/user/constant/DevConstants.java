package com.iot.user.constant;

public class DevConstants {

    /***设备类型**/
    /**
     * 101型号
     */
    public static final String DEV_TYPE_101 = "101";

    /**
     * 102型号
     */
    public static final String DEV_TYPE_102 = "102";

    /**
     * 201型号
     */
    public static final String DEV_TYPE_201 = "201";

    /**
     * 181型号
     */
    public static final String DEV_TYPE_181 = "181";

    /**
     * 182型号
     */
    public static final String DEV_TYPE_182 = "182";

    /**
     * 所有型号
     */
    public static final String DEV_TYPE_ALL = "";

    /******************设备状态******************/
    /**
     * 正常设备
     */
    public static final String DEV_STATUS_OK = "0";

    /**
     * 故障设备
     */
    public static final String DEV_STATUS_WARN = "1";

    /**
     * 报警设备
     */
    public static final String DEV_STATUS_ALER = "2";

    /**
     * 复合故障报警
     */
    public static final String DEV_STATUS_COMBINE = "3";

    /**
     * 全部设备
     */
    public static final String DEV_STATUS_ALL = "";

    /******************是否在线******************/
    /**
     * 所有状态设备
     */
    public static final String DEV_LINE_ON = "0";
    /**
     * 所有状态设备
     */
    public static final String DEV_LINE_OFF = "1";
    /**
     * 未决状态设备
     */
    public static final String DEV_LINE_UNDECIDE = "2";
    /**
     * 所有设备
     */
    public static final String DEV_LINE_ALL = "";

    /*******************订单*****************/
    /**
     * //0：全部
     */
    public static final int DEV_ORDER_ALL = 0;
    /**
     * 1：未支付
     */
    public static final int DEV_ORDER_UNPAYED = 1;
    /**
     *  2：已支付
     */
    public static final int DEV_ORDER_PAYED = 2;


    /***************设备注册状态***************/
    /**
     * 设备未注册
     */
    public static final int DEV_UNREGIST = 0;
    /**
     * 设备注册，未有主绑人
     */
    public static final int DEV_REGISTED_NO_MASTER = 111;

    /**
     * 设备注册，且有主绑人
     */
    public static final int DEV_REGISTED_HAS_MASTER = 112;

    /**
     * 当前设备已绑定
     */
    public static final int DEV_REGISTED_HAS_BIND = 114;


    /*********设备解绑********/
    /**
     * 设备解绑成功
     */
    public static final int DEV_UNBIND_OK = 0;
    /**
     * 需要先将设备管理权转让
     */
    public static final int DEV_UNBIND_FAIL_TRANSFORM_ADMIN = 43;

    /*************人与设备绑定关系************/

    public static final String DEV_BIND_TYPE_MAIN = "1";//1(主绑人)
    public static final String DEV_BIND_TYPE_FOCUS = "2";//  2(关注者)


    /*******************设备报警类型*******************/
    public static final int DEV_ALARM_GAS = 1 ;//燃气报警
    public static final int DEV_ALARM_SMOKE = 2 ;//烟雾报警
    public static final int DEV_ALARM_GAS_SMOKE = 3 ;//燃气烟雾报警
    public static final int DEV_ALARM_SIMULATION = 4 ;//模拟报警
    public static final int DEV_ALARM_GAS_SIMULATION = 5 ;//燃气+模拟报警

    /*******************设备故障类型*******************/


    /******实时消息类型****/
    public static final String MSG_USER = "1";//用户消息
    public static final String MSG_DEV = "2";//设备消息
    public static final String MSG_ALL = "" ;//设备消息


}
