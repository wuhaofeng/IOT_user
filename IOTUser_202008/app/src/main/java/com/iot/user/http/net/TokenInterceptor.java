package com.iot.user.http.net;
import com.iot.user.constant.HttpHeader;
import com.iot.user.ui.activity.main.UnitMainActivity;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.Charset;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
public class TokenInterceptor implements Interceptor {
    public static boolean isRelogin = false;
    public TokenInterceptor(){
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpHeader httpHeader = HttpHeader.getDefaultHeader();
        Request tokenRequest = chain.request().newBuilder().
                addHeader(HttpHeader.PROD_NAME, httpHeader.getProdname()).
                addHeader(HttpHeader.OS_NAME, httpHeader.getOsname()).
                addHeader(HttpHeader.DEVEICE_NAME, httpHeader.getDevicename()).
                addHeader(HttpHeader.IMEI, httpHeader.getImei()).
                addHeader(HttpHeader.API_VERSION, httpHeader.getApiVersion()).
                addHeader(HttpHeader.APP_VERSION,httpHeader.getAppVersion()).
                addHeader(HttpHeader.CONTENT_TYPE, "application/json").build();
//        boolean netWorkConection = NetUtils.hasNetWorkConection(IOTApplication.getIntstance());
        try{
            Response originalResponse=chain.proceed(tokenRequest);
            /**通过如下的办法曲线取到请求完成的数据
             * 原本想通过  originalResponse.body().string()
             * 去取到请求完成的数据,但是一直报错,不知道是okhttp的bug还是操作不当
             * 然后去看了okhttp的源码,找到了这个曲线方法,取到请求完成的数据后,根据特定的判断条件去判断token过期
             */
            ResponseBody responseBody = originalResponse.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = null;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            if (charset==null){
                return originalResponse;
            }
            String bodyString = buffer.clone().readString(charset);
            try {
                JSONObject jsonObject = new JSONObject(bodyString);
                int code = 0;
                if(jsonObject.has("code")){
                    code = jsonObject.getInt("code");
                }
                if (code == 2 && !isRelogin){
                    /**当他未登录就要跳转到登陆页***/
                    if(UnitMainActivity.gtMsgHandler!=null){
                        isRelogin = true;
                        UnitMainActivity.gtMsgHandler.sendEmptyMessage(UnitMainActivity.EXIT_TO_LOGIN);
                    }
//                    else if(AlertNewActivity.exitHandler!=null){
//                        isRelogin = true;
//                        AlertNewActivity.exitHandler.sendEmptyMessage(AlertNewActivity.EXIT_TO_LOGIN);
//                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return originalResponse;
        }catch (Exception e){
            throw new IOException(e);
        }
    }
}

