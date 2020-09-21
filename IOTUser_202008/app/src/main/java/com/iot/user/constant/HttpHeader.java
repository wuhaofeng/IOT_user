package com.iot.user.constant;

import com.iot.user.app.IOTApplication;
import com.iot.user.utils.HttpHeaderUtil;

/**
 *@name http 请求头
 *@author qingchen
 * @version 1.0
 */
public class HttpHeader {

    /**
     * 产品名
     */
    private String prodname;
    /**
     * 系统名称
     */
    private String osname;
    /**
     * 设备名称
     */
    private String devicename;
    /**
     * 设备IMEI编码
     */
    private String imei;
    /**
     * 传输内容格式
     */
    private String contenttype;
    /**
     * API版本号
     */
    private String apiVersion;
    /**
     * APP版本号
     */
    private String appVersion;

    public static final String PROD_NAME = "prod-name";

    public static final String OS_NAME = "os-name";

    public static final String DEVEICE_NAME = "device-name";

    public static final String IMEI = "imei";

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String API_VERSION = "API-Version";

    public static final String APP_VERSION = "APP-Version";

    private static HttpHeader defaultHeader = null;

    public HttpHeader() {

    }

    public HttpHeader(String prodname, String osname, String devicename, String imei, String contenttype, String apiVersion, String appVersion) {
        this.prodname = prodname;
        this.osname = osname;
        this.devicename = devicename;
        this.imei = imei;
        this.contenttype = contenttype;
        this.apiVersion = apiVersion;
        this.appVersion = appVersion;
    }

    public synchronized static final HttpHeader getDefaultHeader(){
        if(defaultHeader == null){
            synchronized (HttpHeader.class){
                defaultHeader = new HttpHeader(HttpHeaderUtil.getProdname(),
                        HttpHeaderUtil.getOsname(),
                        HttpHeaderUtil.getDevicename(),
                        HttpHeaderUtil.getImei(IOTApplication.getIntstance()),
                        HttpHeaderUtil.getContenttype(),
                        HttpHeaderUtil.getAPINAME(),
                        HttpHeaderUtil.getProdversion(IOTApplication.getIntstance()));
            }
        }
        return defaultHeader;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public String getOsname() {
        return osname;
    }

    public void setOsname(String osname) {
        this.osname = osname;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
